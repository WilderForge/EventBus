/*
 * Copyright (c) Forge Development LLC
 * Copyright (c) Gamebuster
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.eventbus.api;


/**
 * Event listeners are wrapped with implementations of this interface
 */
public interface IEventListener {
    void invoke(Event event);

    default String listenerName() {
        return getClass().getName();
    }
}
