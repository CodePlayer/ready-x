package me.codeplayer.util;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.BiFunction;

import org.apache.commons.lang3.StringUtils;

import static java.lang.invoke.MethodType.methodType;

public class Unsafes {

	public static final int javaVersion = parseJavaVersion(System.getProperty("java.version"));

	private static final BiFunction<char[], Boolean, String> newSharedString;

	static {
		BiFunction<char[], Boolean, String> constructor = null;
		if (javaVersion == 8) {
			try {
				Field privateLookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP"); // get the required field via reflections
				privateLookupField.setAccessible(true); // set it accessible
				final MethodHandles.Lookup privateLookup = (MethodHandles.Lookup) privateLookupField.get(MethodHandles.lookup());

				final MethodHandle lookupConstructor = privateLookup.findConstructor(
						MethodHandles.Lookup.class,
						methodType(void.class, Class.class, int.class)
				);
				final MethodHandles.Lookup trustedLookup = (MethodHandles.Lookup) lookupConstructor.invoke(String.class, -1);

				MethodHandle stringConstructor = trustedLookup.findConstructor(
						String.class, methodType(void.class, char[].class, boolean.class)
				);
				final CallSite callSite = LambdaMetafactory.metafactory(
						trustedLookup,
						"apply",
						methodType(BiFunction.class),
						methodType(Object.class, Object.class, Object.class),
						stringConstructor,
						methodType(String.class, char[].class, boolean.class)
				);

				constructor = (BiFunction<char[], Boolean, String>) callSite.getTarget().invokeExact();
			} catch (Throwable ignored) {
			}
		}
		newSharedString = constructor;
	}

	public static int parseJavaVersion(String javaVersionProperty) {
		if (javaVersionProperty.startsWith("1.")) {
			int pos = javaVersionProperty.indexOf('.', 3);
			return Integer.parseInt(javaVersionProperty.substring(2, pos));
		}
		return Integer.parseInt(StringUtils.substringBefore(javaVersionProperty, '.'));
	}

	public static String sharedString(final char[] chars) {
		if (newSharedString != null) {
			try {
				System.out.println(Arrays.toString(chars));
				return newSharedString.apply(chars, true);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
		return new String(chars);
	}

}