/*
 * Copyright (c) Forge Development LLC
 * Copyright (c) Gamebuster
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.eventbus.api;

public interface IEventBusInvokeDispatcher {
    void invoke(IEventListener listener, Event event);
}
