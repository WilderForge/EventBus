/*
 * Copyright (c) Forge Development LLC
 * Copyright (c) Gamebuster
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.eventbus.api;

import java.util.List;

@FunctionalInterface
public interface IEventBusFireOrder {

	public List<IEventListener> reorder(Event event, List<IEventListener> listeners);
	
}
