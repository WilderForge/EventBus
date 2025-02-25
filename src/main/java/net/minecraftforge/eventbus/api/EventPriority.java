/*
 * Copyright (c) Forge Development LLC
 * Copyright (c) Gamebuster
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.eventbus.api;

/**
 * Different priorities for {@link Event} listeners.
 *
 * {@link #NORMAL} is the default level for a listener registered without a priority.
 *
 * @see SubscribeEvent#priority()
 */
public interface EventPriority {
    /**
     * Priority of event listeners, listeners will be sorted with respect to this priority level.
     *
     * Note:
     *   Due to using a ArrayList in the ListenerList,
     *   these need to stay in a contiguous index starting at 0. {Default ordinal}
     */
    public static final int LOWER = -10000; //First to execute
    public static final int LOW = -5000;
    public static final int NORMAL = 0;
    public static final int HIGH = 5000;
    public static final int HIGHER = 10000;
    /**
     * When in this state, {@link Event#setCanceled(boolean)} will throw an exception if called with any value.
     */
    public static final int MONITOR = Integer.MAX_VALUE; //Last to execute

}
