package me.codeplayer.util;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import sun.misc.Unsafe;

import static java.lang.invoke.MethodType.methodType;

/**
 * 使用 底层 API 的 Java 高性能 辅助工具类。该工具类具有实验性质。
 *
 * <p> 请<b>注意</b>：为了确保高性能，本类中的方法会尽量避免数组的复制开销，因此请务必谨慎使用，后续<b>不要</b>修改传入或返回的 数组数据！
 *
 * @see com.alibaba.fastjson2.util.JDKUtils
 */
@SuppressWarnings({ "JavaLangInvokeHandleSignature", "unchecked" })
public class JavaX {

	public static final int javaVersion = parseJavaVersion(System.getProperty("java.version"));
	/** 指示当前 Java 版本是否为 Java 9+ 版本 */
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
	/**
	 * <ul>
	 * <li>在 JDK 9+ 并且 <code> String.COMPACT_STRINGS = true </code>时，通过 <code> STRING_CREATOR_JDK11.apply( bytes, coder ) </code> 构造字符串可以<b>避免</b> byte[] 的复制开销。
	 * <p><b>注意</b>：请自行确保 <code> bytes </code> 和 <code> coder </code> 数据的正确性，并且后续<b>不能</b>修改 <code> bytes </code>！
	 * </li>
	 * <li>如未满足上述条件， 则 <code> STRING_VALUE.apply( bytes, coder ) </code>  等价于 <code> new String( bytes ) </code>
	 * </ul>
	 *
	 * @see String#String(byte[])
	 */
	public static final BiFunction<byte[], Byte, String> STRING_CREATOR_JDK11;
	/**
	 * <ul>
	 * <li>在 JDK 9+ 并且 <code> String.COMPACT_STRINGS = true </code>时，通过 <code> STRING_CODER.applyAsInt( str ) </code> 可以直接获取底层  <code> String.coder </code>。
	 * <li>如未满足上述条件， 则 <code> STRING_CODER.applyAsInt( str ) </code>  等价于 {@link #UTF16}
	 * </ul>
	 *
	 * @see #LATIN1
	 * @see #UTF16
	 */
	public static final ToIntFunction<String> STRING_CODER;
	/**
	 * <ul>
	 * <li>在 JDK 9+ 并且 <code> String.COMPACT_STRINGS = true </code>时，通过 <code> STRING_VALUE.apply( str ) </code> 可以直接获取底层  <code> String.value </code> 的 <code> byte[] </code> <b>引用</b>。
	 * <p><b>注意</b>：外部【不能】修改引用的字节数组！！
	 * </li>
	 * <li>如未满足上述条件， 则 <code> STRING_VALUE.apply( str ) </code>  等价于 <code> str.getByte() </code>
	 * </ul>
	 *
	 * @see String#getBytes()
	 */
	public static final Function<String, byte[]> STRING_VALUE;

	@Nullable
	static final MethodHandle METHOD_HANDLE_HAS_NEGATIVE;
	@Nullable
	static final Predicate<byte[]> PREDICATE_IS_ASCII;

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

		{
			Predicate<byte[]> isAscii = null;
			// isASCII
			MethodHandle handle = null;
			Class<?> classStringCoding = null;
			if (JVM_VERSION >= 17) {
				try {
					handle = trustedLookup.findStatic(
							classStringCoding = String.class,
							"isASCII",
							MethodType.methodType(boolean.class, byte[].class)
					);
				} catch (Throwable e) {
					initErrorLast = e;
				}
			}

			if (handle == null && JVM_VERSION >= 11) {
				try {
					classStringCoding = Class.forName("java.lang.StringCoding");
					handle = trustedLookup.findStatic(
							classStringCoding,
							"isASCII",
							MethodType.methodType(boolean.class, byte[].class)
					);
				} catch (Throwable e) {
					initErrorLast = e;
				}
			}

			if (handle != null) {
				try {
					MethodHandles.Lookup lookup = trustedLookup(classStringCoding);
					CallSite callSite = LambdaMetafactory.metafactory(
							lookup,
							"test",
							methodType(Predicate.class),
							methodType(boolean.class, Object.class),
							handle,
							methodType(boolean.class, byte[].class)
					);
					isAscii = (Predicate<byte[]>) callSite.getTarget().invokeExact();
				} catch (Throwable e) {
					initErrorLast = e;
				}
			}

			PREDICATE_IS_ASCII = isAscii;
		}

		{
			MethodHandle handle = null;
			if (JVM_VERSION >= 11) {
				try {
					Class<?> classStringCoding = Class.forName("java.lang.StringCoding");
					handle = trustedLookup.findStatic(
							classStringCoding,
							"hasNegatives",
							MethodType.methodType(boolean.class, byte[].class, int.class, int.class)
					);
				} catch (Throwable e) {
					initErrorLast = e;
				}
			}
			METHOD_HANDLE_HAS_NEGATIVE = handle;
		}

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
		}
		if (stringValue == null) {
			stringValue = String::getBytes;
		}
		if (stringCreatorJDK8 == null) {
			stringCreatorJDK8 = (chars, share) -> new String(chars);
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

	@Nullable
	static char[] fastGetCharArray(String str) {
		// GraalVM not support
		// Android not support
		if (!FIELD_STRING_VALUE_ERROR) {
			try {
				return (char[]) UNSAFE.getObject(str, FIELD_STRING_VALUE_OFFSET);
			} catch (Exception ignored) {
				FIELD_STRING_VALUE_ERROR = true;
			}
		}
		return null;
	}

	public static char[] getCharArray(String str) {
		char[] chars = fastGetCharArray(str);
		return chars == null ? str.toCharArray() : chars;
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

	/**
	 * 检查给定字节数组是否只包含 ASCII 字符
	 *
	 * @param bytes 待检查的字节数组，不能为 null
	 * @param start 检查的起始位置（包含）
	 * @param end 检查的结束位置（不包含）
	 * @return 如果只包含 ASCII 字符则返回 true，否则返回 false
	 */
	public static boolean isASCII(byte[] bytes, int start, int end) {
		// JVM 底层对该方法有指令集优化，优先使用 JVM 内置方法进行检测
		if (METHOD_HANDLE_HAS_NEGATIVE != null) {
			try {
				return !(boolean) METHOD_HANDLE_HAS_NEGATIVE.invokeExact(bytes, start, end - start);
			} catch (Throwable ignored) {
				// ignored
			}
		}
		return isASCII0(bytes, start, end);
	}

	static boolean isASCII0(byte[] bytes, int start, int end) {
		while (start < end) {
			if (bytes[start++] < 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查给定字节数组是否只包含 ASCII 字符
	 *
	 * @param bytes 待检查的字节数组，不能为 null
	 * @return 如果只包含 ASCII 字符则返回 true，否则返回 false
	 */
	public static boolean isASCII(byte[] bytes) {
		if (PREDICATE_IS_ASCII != null) {
			return PREDICATE_IS_ASCII.test(bytes);
		}
		return isASCII(bytes, 0, bytes.length);
	}

	/** JDK 9+时，此值与 <code> String.COMPACT_STRINGS </code> 保持一致 */
	static final boolean supportLatin1 = STRING_CODER.applyAsInt("") == LATIN1;

	/**
	 * 检查给定字符串是否只包含 ASCII 字符
	 *
	 * @param str 待检查的字符串，不能为 null
	 * @return 如果字符串只包含 ASCII 字符则返回 true，否则返回 false
	 */
	public static boolean isASCII(String str) {
		return isJava9OrHigher
				? (STRING_CODER.applyAsInt(str) == LATIN1 || !supportLatin1) && isASCII(STRING_VALUE.apply(str))
				: isASCIIOnJdk8(str);
	}

	private static boolean isASCIIOnJdk8(String str) {
		final char[] chars = fastGetCharArray(str);
		if (chars != null) {
			for (char ch : chars) {
				if (ch >= 0x80) { // @see sun.nio.cs.US_ASCII.Encoder.canEncode()
					return false;
				}
			}
			return true;
		}
		return StandardCharsets.US_ASCII.newEncoder().canEncode(str);
	}

	/**
	 * 将字符串转换为 UTF-8 编码的字节数组
	 * <p> 【注意】：在 JDK 9+，返回的字节数组可能是字符串底层数组的<b>引用</b>，只能读取、<b>不可</b>修改，否则可能引发错误！！！
	 *
	 * @param str 待转换的字符串，不能为空
	 * @return UTF-8编码的字节数组
	 * @see String#getBytes(Charset)
	 */
	public static byte[] getUtf8Bytes(@Nonnull String str) {
		if (STRING_CODER.applyAsInt(str) == LATIN1) {
			final byte[] bytes = STRING_VALUE.apply(str);
			if (isASCII(bytes)) {
				return bytes;
			}
		}
		return str.getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * 将字符串转换为 UTF-8 编码的字节数组
	 * <p> 【注意】：在 JDK 9+，返回的字节数组可能是字符串底层数组的<b>引用</b>，只能读取、<b>不可</b>修改，否则可能引发错误！！！
	 *
	 * @param str 待转换的字符串，不能为空
	 * @return UTF-8编码的字节数组
	 * @see String#getBytes(Charset)
	 */
	public static byte[] getBytes(@Nonnull String str, @Nonnull Charset charset) {
		if (STRING_CODER.applyAsInt(str) == LATIN1) {
			if (charset == StandardCharsets.UTF_8) {
				final byte[] bytes = STRING_VALUE.apply(str);
				if (isASCII(bytes)) {
					return bytes;
				}
			} else if (charset == StandardCharsets.ISO_8859_1) {
				return STRING_VALUE.apply(str);
			}
		} else if (isJava9OrHigher && charset == StandardCharsets.UTF_16) {
			return STRING_VALUE.apply(str);
		}
		return str.getBytes(charset);
	}

	/**
	 * JDK 9+时，此值与 <code> String.COMPACT_STRINGS </code> 保持一致。
	 * <p> JDK 8 时，此值恒为 false
	 */
	public static boolean supportLatin1() {
		return supportLatin1;
	}

	/**
	 * 将指定字符集的 字节数组 构造为对应的 字符串
	 * <p> 【注意】：在 JDK 9+ 时，为提高性能，返回的字符串内部可能直接使用传入的 <code> bytes </code> 数组的 <b>引用</b>，请自行确保后续<b>不要</b>修改入参 <code> bytes </code> 的数据！！！
	 *
	 * @param bytes 字节数组
	 * @param charset 字符集
	 * @return 使用指定字符集解码字节数组后得到的字符串
	 */
	public static String newString(final byte[] bytes, Charset charset) {
		// 如果字节数组是 ISO_8859_1(Latin1) => Latin1，则无需检查字符
		// 如果是 UTF-8、US_ASCII => Latin1，则只有全部是 ASCII 才兼容
		// 此处的逻辑参考了 JDK 代码 new String( byte[] bytes, Charset charset  ) 的实现
		if (supportLatin1 && (charset == StandardCharsets.ISO_8859_1 ||
				((charset == StandardCharsets.UTF_8 || charset == StandardCharsets.US_ASCII) && isASCII(bytes))
		)) {
			return STRING_CREATOR_JDK11.apply(bytes, LATIN1);
		}
		return new String(bytes, charset);
	}

	public static int parseJavaVersion(String javaVersionProperty) {
		if (javaVersionProperty.startsWith("1.")) {
			int pos = javaVersionProperty.indexOf('.', 3);
			return JavaHelper.parseInt(javaVersionProperty, 2, pos);
		}
		return StringX.substringBefore(javaVersionProperty, '.', Slice::asInteger);
	}

}