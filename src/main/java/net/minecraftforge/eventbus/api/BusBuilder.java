/*
 * Copyright (c) Forge Development LLC
 * Copyright (c) Gamebuster
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.eventbus.api;

import java.util.Comparator;

import net.minecraftforge.eventbus.BusBuilderImpl;

/**
 * Build a bus
 */
public interface BusBuilder {
    public static BusBuilder builder() {
        return new BusBuilderImpl();
    }

    /* true by default */
    BusBuilder setTrackPhases(boolean trackPhases);
    BusBuilder setExceptionHandler(IEventExceptionHandler handler);
    BusBuilder startShutdown();
    BusBuilder checkTypesOnDispatch();
    BusBuilder markerType(Class<?> type);
    BusBuilder setComparator(Comparator<IEventListener> comparator);

    /* Use ModLauncher hooks when creating ASM handlers. */
    BusBuilder useModLauncher();

    IEventBus build();
}
