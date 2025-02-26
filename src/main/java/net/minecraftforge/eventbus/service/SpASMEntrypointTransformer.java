/*
 * Copyright (c) Forge Development LLC
 * Copyright (c) Gamebuster
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.eventbus.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import net.mine_diver.spasm.api.transform.ClassTransformer;
import net.mine_diver.spasm.api.transform.TransformationResult;
import net.minecraftforge.eventbus.EventBusEngine;
import net.minecraftforge.eventbus.ModLauncherFactory;

public class SpASMEntrypointTransformer implements ClassTransformer {

	private final EventBusEngine ENGINE = new EventBusEngine();
	
	public SpASMEntrypointTransformer() {
		//System.out.println("CACHE: " + InternalUtils.cachePublic());
		ModLauncherFactory.hasPendingWrapperClass(""); //NEEDED SO CLASSLOAD ORDER IS CORRECT. IF REMOVED ASM TRANSFORMATION WILL FAIL
	}
	
	@Override
	public @NotNull TransformationResult transform(@NotNull ClassLoader classLoader, @NotNull ClassNode classNode) {
		Type type = Type.getObjectType(classNode.name);
		
		//System.out.println("Checking " + type + " on loader " + classLoader);
		if(handlesClass(classLoader, type, classNode)) {
			//System.out.println("Accepts " + type);
			try {
				ENGINE.processClass(classNode, type);
			}
			catch(Throwable t) {
				//System.out.println("Failed " + type);
				t.printStackTrace();
				//return TransformationResult.PASS;
				throw t;
			}
			//System.out.println("Processed " + type);
			return TransformationResult.SUCCESS;
		}
		
		//System.out.println("Skipped " + type);
		return TransformationResult.PASS;
	}

	private boolean handlesClass(ClassLoader loader, Type type, ClassNode node) {
		return ENGINE.handlesClass(type) && !type.toString().equals("Lnet/minecraftforge/eventbus/ModLauncherFactory;")
		&& (
			type.toString().equals("Lnet/minecraftforge/eventbus/api/Event;") ||
			getSuperClasses(loader, node).contains("net/minecraftforge/eventbus/api/Event") ||
			hasAnnotatedMethods(node)
		);
			
	}
	
	private HashSet<String> getSuperClasses(ClassLoader loader, ClassNode node) {
		HashSet<String> superClasses = new HashSet<>();
		String superName = node.superName;
		ClassNode supr = node;
		while(superName != null && supr != null) {
			superName = supr.superName;
			if(superName == null) {
				break;
			}
			superClasses.add(superName);
			supr = typeToClassNode(loader, Type.getObjectType(superName));
		}

		/*
		String msg = "========Superclasses of " + node.name + " (" + superClasses.size() + ")========";
		System.out.println(msg);
		for(String superClass : superClasses) {
			System.out.println(superClass);
		}
		for(int i = 0; i < msg.length(); i++) {
			System.out.print('=');
		}
		System.out.println();
		*/
		return superClasses;
	}
	
	private boolean hasAnnotatedMethods(ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.visibleAnnotations == null) {
				continue;
			}
			for(AnnotationNode annotation : method.visibleAnnotations) {
				if ("Lnet/minecraftforge/eventbus/api/SubscribeEvent;".equals(annotation.desc)) {
					return true;
				}
			}
		}
		return false;
	}
	
    public static ClassNode typeToClassNode(ClassLoader loader, Type type) {
        // Get the internal name (descriptor) of the class
        String className = type.getInternalName();

        // Convert the internal name into the fully qualified class name
        String classFileName = className + ".class";

        // Use ClassLoader to load the class file as a byte array
        byte[] classBytes = loadClassBytes(loader, classFileName);

        if(classBytes == null) {
        	return null;
        }
        // Create a ClassReader to read the class bytecode
        ClassReader classReader = new ClassReader(classBytes);

        // Create a ClassNode to hold the class information
        ClassNode classNode = new ClassNode();

        // Parse the class bytecode into the ClassNode
        classReader.accept(classNode, 0);

        return classNode;
    }
    
    public static byte[] loadClassBytes(ClassLoader loader, String classFileName) {
        // Load the class file bytes from the classpath (or from any other source)
        // This is a simple implementation that loads the class file from the system classpath
    	try {
    		InputStream bytes = loader.getResourceAsStream(classFileName);
    		if(bytes != null) {
    			return bytes.readAllBytes();
    		}
    		else {
    			//System.out.println("Couldn't obtain bytes for " + classFileName);
    		}
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
}
