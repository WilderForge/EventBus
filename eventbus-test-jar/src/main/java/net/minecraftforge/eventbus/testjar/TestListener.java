/*
 * Copyright (c) Forge Development LLC
 * Copyright (c) Gamebuster
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.eventbus.testjar;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventListener;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TestListener implements IEventListener {
    private Object instance;
    private Method method;

    TestListener(Object instance, Method method) {
        this.instance = instance;
    }

    @Override
    public void invoke(final Event event) {
        instance.equals(event);
    }

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return method.getAnnotation(annotationClass);
	}

	@Override
	public Annotation[] getAnnotations() {
		return method.getAnnotations();
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return method.getDeclaredAnnotations();
	}

	@Override
	public SubscribeEvent subscribeInfo() {
		return method.getDeclaredAnnotation(SubscribeEvent.class);
	}
}
