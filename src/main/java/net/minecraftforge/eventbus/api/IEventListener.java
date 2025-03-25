/*
 * Copyright (c) Forge Development LLC
 * Copyright (c) Gamebuster
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.eventbus.api;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * Event listeners are wrapped with implementations of this interface
 */
public interface IEventListener extends AnnotatedElement {
    void invoke(Event event);
    
    SubscribeEvent subscribeInfo();
    
    Method listeningMethod();
    
    default String listenerName() {
        return getClass().getName();
    }
}
