/*
 * Copyright (c) Forge Development LLC
 * Copyright (c) Gamebuster
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.eventbus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/*
 * An implementation of the Cache class that uses a ConcurrentHashMap for the backing map.
 * This allows us to use no locks when calling the get method.
 * However, it has re-entrant issues when writing. So we guard that using a synchronized block
 */
class CacheConcurrent<K,V> implements Cache<K, V> {
    private Object lock = new Object();
    private final Map<K, V> map;

    CacheConcurrent() {
        this.map = new ConcurrentHashMap<>(32);
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public <I> V computeIfAbsent(K key, Supplier<I> factory, Function<I, V> finalizer) {
        // This is a put once map, so lets try checking if the map has this value.
        // Should be thread safe to read without lock as any writes will be guarded
        var ret = get(key);

        // If the map had a value, return it.
        if (ret != null)
            return ret;

        // Let's pre-compute our new value. This could take a while, as well as recursively call this
        // function. as such, we need to make sure we don't hold a lock when we do this
        var intermediate = factory.get();

        // We are actually gunna modify the map now, so prevent other threads form doing so
        synchronized (lock) {
            // Check if some other thread already created a value
            ret = map.get(key);
            if (ret == null) {
                // Run any finalization we need, this was added because ClassLoaderFactory will actually define the class here
                ret = finalizer.apply(intermediate);
                // Update the map
                map.put(key, ret);
            }
            return ret;
        }
    }
}
