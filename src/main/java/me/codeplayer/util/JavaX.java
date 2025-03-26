package me.codeplayer.util;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.util.function.*;

import org.apache.commons.lang3.StringUtils;
import sun.misc.Unsafe;

import static java.lang.invoke.MethodType.methodType;

/**
 * @see com.alibaba.fastjson2.util.JDKUtils
 */
@SuppressWarnings({ "JavaLangInvokeHandleSignature", "unchecked" })
public class JavaX {

	public static final int javaVersion = parseJavaVersion(System.getProperty("java.version"));
	public static final boolean isJava9OrHigher = javaVersion >= 9;

	public static final Unsafe UNSAFE;

	public static final int JVM_VERSION;
	public static final Byte LATIN1 = 0;
	public static final Byte UTF16 = 1;

	public static final long FIELD_STRING_VALUE_OFFSET;
	public static volatile boolean FIELD_STRING_VALUE_ERROR;

	public static final boolean ANDROID;
	public static final boolean GRAAL;
	public static final boolean OPENJ9;

	// GraalVM not support
	// Android not support
	public static final BiFunction<char[], Boolean, String> STRING_CREATOR_JDK8;
	public static final BiFunction<byte[], Byte, String> STRING_CREATOR_JDK11;
	public static final ToIntFunction<String> STRING_CODER;
	public static final Function<String, byte[]> STRING_VALUE;

	public static final MethodHandles.Lookup IMPL_LOOKUP;
	static volatile MethodHandle CONSTRUCTOR_LOOKUP;
	static volatile boolean CONSTRUCTOR_LOOKUP_ERROR;
	static volatile Throwable initErrorLast;

	static {
		Unsafe unsafe;
		long offset;
		try {
			Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafeField.setAccessible(true);
			unsafe = (Unsafe) theUnsafeField.get(null);
			offset = unsafe.arrayBaseOffset(byte[].class);
		} catch (Throwable e) {
			throw new UnsupportedOperationException("init unsafe error", e);
		}
		UNSAFE = unsafe;

		if (offset == -1) {
			throw new UnsupportedOperationException("init JavaX error", initErrorLast);
		}

		int jvmVersion = -1;
		boolean openj9 = false, android = false, graal = false;
		try {
			String jmvName = System.getProperty("java.vm.name");
			openj9 = jmvName.contains("OpenJ9");
			android = "Dalvik".equals(jmvName);
			graal = System.getProperty("org.graalvm.nativeimage.imagecode") != null;
			if (openj9 || android || graal) {
				FIELD_STRING_VALUE_ERROR = true;
			}

			String javaSpecVer = System.getProperty("java.specification.version");
			// android is 0.9
			if (javaSpecVer.startsWith("1.")) {
				javaSpecVer = javaSpecVer.substring(2);
			}
			if (javaSpecVer.indexOf('.') == -1) {
				jvmVersion = Integer.parseInt(javaSpecVer);
			}
		} catch (Throwable e) {
			initErrorLast = e;
		}

		OPENJ9 = openj9;
		ANDROID = android;
		GRAAL = graal;

		JVM_VERSION = jvmVersion;

		if (JVM_VERSION == 8) {
			Field field;
			long fieldOffset = -1;
			if (!ANDROID) {
				try {
					field = String.class.getDeclaredField("value");
					field.setAccessible(true);
					fieldOffset = UNSAFE.objectFieldOffset(field);
				} catch (Exception ignored) {
					FIELD_STRING_VALUE_ERROR = true;
				}
			}

			FIELD_STRING_VALUE_OFFSET = fieldOffset;

		} else {
			Field fieldValue;
			long fieldValueOffset = -1;
			if (!ANDROID) {
				try {
					fieldValue = String.class.getDeclaredField("value");
					fieldValueOffset = UNSAFE.objectFieldOffset(fieldValue);
				} catch (Exception ignored) {
					FIELD_STRING_VALUE_ERROR = true;
				}
			}
			FIELD_STRING_VALUE_OFFSET = fieldValueOffset;
		}

		BiFunction<char[], Boolean, String> stringCreatorJDK8 = null;
		BiFunction<byte[], Byte, String> stringCreatorJDK11 = null;
		ToIntFunction<String> stringCoder = null;
		Function<String, byte[]> stringValue = null;

		MethodHandles.Lookup trustedLookup = null;
		if (!ANDROID) {
			try {
				Class<MethodHandles.Lookup> lookupClass = MethodHandles.Lookup.class;
				Field implLookup = lookupClass.getDeclaredField("IMPL_LOOKUP");
				long fieldOffset = UNSAFE.staticFieldOffset(implLookup);
				trustedLookup = (MethodHandles.Lookup) UNSAFE.getObject(lookupClass, fieldOffset);
			} catch (Throwable ignored) {
				// ignored
			}
			if (trustedLookup == null) {
				trustedLookup = MethodHandles.lookup();
			}
		}
		IMPL_LOOKUP = trustedLookup;

		Boolean compact_strings = null;
		try {
			if (JVM_VERSION == 8) {
				MethodHandles.Lookup lookup = trustedLookup(String.class);

				MethodHandle handle = lookup.findConstructor(
						String.class, methodType(void.class, char[].class, boolean.class)
				);

				CallSite callSite = LambdaMetafactory.metafactory(
						lookup,
						"apply",
						methodType(BiFunction.class),
						methodType(Object.class, Object.class, Object.class),
						handle,
						methodType(String.class, char[].class, boolean.class)
				);
				stringCreatorJDK8 = (BiFunction<char[], Boolean, String>) callSite.getTarget().invokeExact();
			}

			boolean lookupLambda = false;
			if (JVM_VERSION > 8 && !android) {
				try {
					Field compact_strings_field = String.class.getDeclaredField("COMPACT_STRINGS");
					long fieldOffset = UNSAFE.staticFieldOffset(compact_strings_field);
					compact_strings = UNSAFE.getBoolean(String.class, fieldOffset);
				} catch (Throwable e) {
					initErrorLast = e;
				}
				lookupLambda = compact_strings != null && compact_strings;
			}

			if (lookupLambda) {
				MethodHandles.Lookup lookup = trustedLookup.in(String.class);
				MethodHandle handle = lookup.findConstructor(
						String.class, methodType(void.class, byte[].class, byte.class)
				);
				CallSite callSite = LambdaMetafactory.metafactory(
						lookup,
						"apply",
						methodType(BiFunction.class),
						methodType(Object.class, Object.class, Object.class),
						handle,
						methodType(String.class, byte[].class, Byte.class)
				);
				stringCreatorJDK11 = (BiFunction<byte[], Byte, String>) callSite.getTarget().invokeExact();

				MethodHandle coder = lookup.findSpecial(
						String.class,
						"coder",
						methodType(byte.class),
						String.class
				);
				CallSite applyAsInt = LambdaMetafactory.metafactory(
						lookup,
						"applyAsInt",
						methodType(ToIntFunction.class),
						methodType(int.class, Object.class),
						coder,
						methodType(byte.class, String.class)
				);
				stringCoder = (ToIntFunction<String>) applyAsInt.getTarget().invokeExact();

				MethodHandle value = lookup.findSpecial(
						String.class,
						"value",
						methodType(byte[].class),
						String.class
				);
				CallSite apply = LambdaMetafactory.metafactory(
						lookup,
						"apply",
						methodType(Function.class),
						methodType(Object.class, Object.class),
						value,
						methodType(byte[].class, String.class)
				);
				stringValue = (Function<String, byte[]>) apply.getTarget().invokeExact();
			}
		} catch (Throwable e) {
			initErrorLast = e;
			if (stringCreatorJDK8 == null) {
				stringCreatorJDK8 = (chars, share) -> new String(chars);
			}
			stringValue = String::getBytes;
		}
		if (stringCreatorJDK11 == null) {
			stringCreatorJDK11 = (bytes, coder) -> new String(bytes);
		}
		if (stringCoder == null) {
			stringCoder = str -> 1;
		}

		STRING_CREATOR_JDK8 = stringCreatorJDK8;
		STRING_CREATOR_JDK11 = stringCreatorJDK11;
		STRING_CODER = stringCoder;
		STRING_VALUE = stringValue;
	}

	public static char[] getCharArray(String str) {
		// GraalVM not support
		// Android not support
		if (!FIELD_STRING_VALUE_ERROR) {
			try {
				return (char[]) UNSAFE.getObject(str, FIELD_STRING_VALUE_OFFSET);
			} catch (Exception ignored) {
				FIELD_STRING_VALUE_ERROR = true;
			}
		}
		return str.toCharArray();
	}

	public static MethodHandles.Lookup trustedLookup(Class<?> objectClass) {
		if (!CONSTRUCTOR_LOOKUP_ERROR) {
			try {
				int TRUSTED = -1;

				MethodHandle constructor = CONSTRUCTOR_LOOKUP;
				if (JVM_VERSION < 15) {
					if (constructor == null) {
						constructor = IMPL_LOOKUP.findConstructor(
								MethodHandles.Lookup.class,
								methodType(void.class, Class.class, int.class)
						);
						CONSTRUCTOR_LOOKUP = constructor;
					}
					int FULL_ACCESS_MASK = 31; // for IBM Open J9 JDK
					return (MethodHandles.Lookup) constructor.invoke(
							objectClass,
							OPENJ9 ? FULL_ACCESS_MASK : TRUSTED
					);
				} else {
					if (constructor == null) {
						constructor = IMPL_LOOKUP.findConstructor(
								MethodHandles.Lookup.class,
								methodType(void.class, Class.class, Class.class, int.class)
						);
						CONSTRUCTOR_LOOKUP = constructor;
					}
					return (MethodHandles.Lookup) constructor.invoke(objectClass, null, TRUSTED);
				}
			} catch (Throwable ignored) {
				CONSTRUCTOR_LOOKUP_ERROR = true;
			}
		}

		return IMPL_LOOKUP.in(objectClass);
	}

	public static String asciiStringJDK8(byte[] bytes, int offset, int strlen) {
		char[] chars = new char[strlen];
		for (int i = 0; i < strlen; ++i) {
			chars[i] = (char) bytes[offset + i];
		}
		return STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
	}

	public static String latin1StringJDK8(byte[] bytes, int offset, int strlen) {
		char[] chars = new char[strlen];
		for (int i = 0; i < strlen; ++i) {
			chars[i] = (char) (bytes[offset + i] & 0xff);
		}
		return STRING_CREATOR_JDK8.apply(chars, Boolean.TRUE);
	}

	public static int parseJavaVersion(String javaVersionProperty) {
		if (javaVersionProperty.startsWith("1.")) {
			int pos = javaVersionProperty.indexOf('.', 3);
			return Integer.parseInt(javaVersionProperty.substring(2, pos));
		}
		return Integer.parseInt(StringUtils.substringBefore(javaVersionProperty, '.'));
	}

}