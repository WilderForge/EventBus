/*
 * Copyright (c) Forge Development LLC
 * Copyright (c) Gamebuster
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.eventbus.service;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import net.mine_diver.spasm.api.transform.ClassTransformer;
import net.mine_diver.spasm.api.transform.TransformationResult;
import net.minecraftforge.eventbus.EventBusEngine;

public class SpASMEntrypointTransformer implements ClassTransformer {

	private static final EventBusEngine ENGINE = new EventBusEngine();
	
	@Override
	public @NotNull TransformationResult transform(@NotNull ClassLoader classLoader, @NotNull ClassNode classNode) {
		Type type = Type.getType(classNode.name);
		
		if(ENGINE.handlesClass(type)) {
			ENGINE.processClass(classNode, type);
			return TransformationResult.SUCCESS;
		}
		
		return TransformationResult.PASS;
	}

}
