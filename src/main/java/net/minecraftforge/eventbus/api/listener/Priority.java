/*
 * Copyright (c) Forge Development LLC
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.eventbus.api.listener;

/**
 * Some common priority values, spread out evenly across the range of a Java signed byte, factoring in the special
 * {@link Priority#MONITOR} priority.
 */
public final class Priority {
    private Priority() {}

    /**
     * Runs first
     */
    public static final int HIGHER = 10000;

    /**
     * Runs before {@link #NORMAL} but after {@link #HIGHER}
     */
    public static final int HIGH = 5000;

    /**
     * The default priority
     */
    public static final int NORMAL = 0;

    /**
     * Runs after {@link #NORMAL} but before {@link #LOWER}
     */
    public static final int LOW = -5000;

    /**
     * The last priority that can mutate the event instance
     */
    public static final int LOWER = -10000;

    /**
     * A special priority that is only used for monitoring purposes and typically doesn't allow cancelling or mutation.
     * <p>Monitoring listeners are always called last - even if the event is cancelled.</p>
     */
    public static final int MONITOR = Integer.MIN_VALUE;
}
