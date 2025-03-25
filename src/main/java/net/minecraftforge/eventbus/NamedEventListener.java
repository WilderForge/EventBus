/*
 * Copyright (c) Forge Development LLC
 * Copyright (c) Gamebuster
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.eventbus;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventListener;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public class NamedEventListener implements IEventListener {
    public static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("eventbus.namelisteners", "false"));
    static IEventListener namedWrapper(IEventListener listener, Supplier<String> name) {
        if (!DEBUG) return listener;
        return new NamedEventListener(listener, name.get());
    }

    private final IEventListener wrap;
    private final String name;

    public NamedEventListener(IEventListener wrap, final String name) {
        this.wrap = wrap;
        this.name = name;
    }

    @Override
    public String listenerName() {
        return this.name;
    }

    @Override
    public void invoke(final Event event) {
        this.wrap.invoke(event);
    }

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return wrap.getAnnotation(annotationClass);
	}

	@Override
	public Annotation[] getAnnotations() {
		return wrap.getAnnotations();
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return wrap.getDeclaredAnnotations();
	}

	@Override
	public SubscribeEvent subscribeInfo() {
		return wrap.subscribeInfo();
	}

	@Override
	public Method listeningMethod() {
		return wrap.listeningMethod();
	}
}
