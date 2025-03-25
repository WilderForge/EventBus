/*
 * Copyright (c) Forge Development LLC
 * Copyright (c) Gamebuster
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.eventbus;

import net.minecraftforge.eventbus.api.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import static org.objectweb.asm.Type.getMethodDescriptor;

public class ASMEventHandler implements IEventListener {
    private final IEventListener handler;
    private final SubscribeEvent subInfo;
    private final String readable;
    private final Type filter;
    private final Method method;

    public ASMEventHandler(IEventListenerFactory factory, Object target, Method method, boolean isGeneric) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        handler = factory.create(method, target);
        this.method = method;

        subInfo = method.getAnnotation(SubscribeEvent.class);
        readable = "ASM: " + target + " " + method.getName() + getMethodDescriptor(method);
        Type filter = null;
        if (isGeneric) {
            Type type = method.getGenericParameterTypes()[0];
            if (type instanceof ParameterizedType) {
                filter = ((ParameterizedType)type).getActualTypeArguments()[0];
                if (filter instanceof ParameterizedType) // Unlikely that nested generics will ever be relevant for event filtering, so discard them
                    filter = ((ParameterizedType)filter).getRawType();
                else if (filter instanceof WildcardType wfilter) {
                    // If there's a wildcard filter of Object.class, then remove the filter.
                    if (wfilter.getUpperBounds().length == 1 && wfilter.getUpperBounds()[0] == Object.class && wfilter.getLowerBounds().length == 0) {
                        filter = null;
                    }
                }
            }
        }
        this.filter = filter;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void invoke(Event event) {
        if (!event.isCanceled() || subInfo.receiveCanceled()) {
            if (filter == null || filter == ((IGenericEvent)event).getGenericType())
                handler.invoke(event);
        }
    }

    public Integer getPriority() {
        return subInfo.priority();
    }

    public String toString() {
        return readable;
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
		return subInfo;
	}
}
