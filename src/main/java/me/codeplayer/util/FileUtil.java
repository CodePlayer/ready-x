package me.codeplayer.util;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.*;
import javax.annotation.Nullable;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * 用于文件操作的公共工具类
 *
 * @author Ready
 * @since 2013-4-9
 */
public abstract class FileUtil {

	/**
	 * 用于表示文件大小的单位
	 */
	private static final String[] FILE_UNITS = { "Byte", "KB", "MB", "GB", "TB", "PB", "EB" };
	/** 单位：自动判断 */
	public static final int UNIT_AUTO = 0;
	/** 单位：字节 */
	public static final int UNIT_BYTE = 1;
	/** 单位：KB */
	public static final int UNIT_KB = 2;
	/** 单位：MB */
	public static final int UNIT_MB = 3;
	/** 单位：GB */
	public static final int UNIT_GB = 4;
	/** 单位：TB */
	public static final int UNIT_TB = 5;
	/** 单位：PB */
	public static final int UNIT_PB = 6;
	/** 单位：EB */
	public static final int UNIT_EB = 7;

	private static RoundingMode fileSizeRoundingMode = RoundingMode.FLOOR;

	public static void setFileSizeRoundingMode(RoundingMode roundingMode) {
		fileSizeRoundingMode = Objects.requireNonNull(roundingMode);
	}

	/**
	 * 根据文件名称返回对应的扩展名在字符串中的索引值
	 *
	 * @param filename 指定的文件名
	 * @return 返回扩展名分隔符'.'对应的索引值，如果不存在则返回 -1
	 */
	public static int indexOfExtension(String filename) {
		return indexOfExtension(filename, true);
	}

	/**
	 * 根据文件名称返回对应的扩展名在字符串中的索引值
	 *
	 * @param filename 指定的文件名
	 * @return 返回扩展名分隔符'.'对应的索引值，如果不存在则返回 -1
	 */
	public static int indexOfExtension(String filename, boolean backScanDirSeparator) {
		final int pos = filename.lastIndexOf('.');
		if (backScanDirSeparator && pos != -1) {
			if (filename.indexOf('/', pos + 1) != -1) {
				return -1;
			}
			if (filename.indexOf('\\', pos + 1) != -1) {
				return -1;
			}
		}
		return pos;
	}

	/**
	 * 根据文件路径获取对应的文件扩展名<br>
	 * 如果没有指定的后缀，则返回空字符串""
	 *
	 * @param path 指定的文件路径
	 * @param removeDot 是否移除点号
	 * @throws NullPointerException 如果参数 {@code path } 为null
	 * @since 0.0.1
	 */
	public static String getExtension(String path, boolean removeDot) throws NullPointerException {
		int pos = indexOfExtension(path);
		if (pos == -1) {
			return "";
		} else {
			return path.substring(removeDot ? pos + 1 : pos);
		}
	}

	/**
	 * 根据文件路径获取对应的文件扩展名(例如：".jpg"、".gif"等)<br>
	 * 如果没有指定的后缀，则返回空字符串""
	 *
	 * @param path 指定的文件路径
	 * @throws NullPointerException 如果参数 {@code path } 为null
	 * @since 0.0.1
	 */
	public static String getExtension(String path) {
		return getExtension(path, false);
	}

	/**
	 * 获取指定文件路径中的文件名称部分
	 *
	 * @param path 指定的文件路径
	 * @throws NullPointerException 如果参数 {@code path } 为null
	 * @since 0.0.1
	 */
	public static String getFileName(String path) {
		return getFileName(path, false);
	}

	/**
	 * 获取指定文件路径中的文件名称部分
	 *
	 * @param path 指定的文件路径
	 * @param withoutExt 是否需要去除文件扩展名
	 * @throws NullPointerException 如果参数 {@code path } 为null
	 * @since 0.0.1
	 */
	public static String getFileName(String path, boolean withoutExt) {
		String str = new File(path).getName();
		int pos = str.lastIndexOf('/');
		if (pos != -1) {
			int pos2 = str.indexOf('\\', pos + 1);
			str = str.substring(Math.max(pos, pos2) + 1);
		} else {
			pos = str.lastIndexOf('\\');
			if (pos != -1) {
				str = str.substring(pos + 1);
			}
		}
		if (withoutExt) {
			pos = indexOfExtension(str, false);
			if (pos > -1) {
				str = str.substring(0, pos);
			}
		}
		return str;
	}

	/**
	 * 获取具有随机文件名的文件对象，文件名会根据当前时间采用随机算法自动生成，并且内部保证本地没有重名的文件
	 *
	 * @param path 文件路径
	 * @param prefix 文件名的补充前缀(可以为null)
	 * @param suffix 文件名后缀，例如 ".png" 或 "png"
	 * @since 0.0.1
	 */
	public static File getRandomFile(String path, @Nullable String prefix, @Nullable String suffix, Date now) {
		String fileName = FastDateFormat.getInstance("yyyyMMdd-HHmmssSSS").format(now);
		if (prefix != null) {
			fileName = prefix + fileName;
		}
		if (suffix == null) {
			suffix = "";
		} else if (!suffix.isEmpty()) { // 如果有后缀就+"."
			if (suffix.charAt(0) != '.') {
				suffix = '.' + suffix;
			}
		}
		String destFileName = fileName + suffix;
		File file = new File(path, destFileName);
		while (file.exists()) {
			destFileName = fileName + '-' + RandomUtil.getIntString(4) + suffix;
			file = new File(path, destFileName);
		}
		return file;
	}

	/**
	 * 获取随机文件名，根据当前时间采用随机算法自动生成，并且内部保证本地没有重复文件名的文件
	 *
	 * @param prefix 文件名前缀(可以为null)
	 * @since 0.0.1
	 */
	public static File getRandomFile(String path, String prefix, String suffix) {
		return getRandomFile(path, prefix, suffix, new Date());
	}

	/**
	 * 获取随机文件名，根据当前时间采用随机算法自动生成，并且内部保证本地没有重复文件名的文件
	 *
	 * @since 0.0.1
	 */
	public static File getRandomFile(String path, String suffix) {
		return getRandomFile(path, null, suffix);
	}

	/**
	 * 根据文件的字节数量计算出改文件为多少"Byte"/"KB"/"MB"/"GB"/"TB"/"PB"<br>
	 * 如果unit为 UNIT_AUTO(0)，则根据文件大小自动选择单位，如果存在小数均保留<code>scale</code>位小数
	 *
	 * @param fileSize 指定的文件大小
	 * @param unit 指定的文件单位
	 * @param scale 保留的小数位数
	 * @param roundingMode 舍入模式
	 * @since 0.0.1
	 */
	public static String calcFileSize(long fileSize, int unit, int scale, RoundingMode roundingMode) {
		if (fileSize < 0) {
			throw new IllegalArgumentException("fileSize can not less than 0: " + fileSize);
		}
		if (unit < UNIT_AUTO || unit > UNIT_EB) {
			throw new IllegalArgumentException(String.valueOf(unit));
		}
		if (unit != UNIT_AUTO) {
			int shift = unit - 1;
			return divide(fileSize, 1L << (10 * shift), scale, roundingMode) + FILE_UNITS[shift];
		}
		// 如果没有传入单位，则自动识别
		int shift = 0;
		long unitBytes = 1;
		long nextUnitBytes = unitBytes << 10;
		while (fileSize >= nextUnitBytes) {
			unitBytes = nextUnitBytes;
			nextUnitBytes <<= 10;
			shift++;
		}
		return divide(fileSize, unitBytes, scale, roundingMode) + FILE_UNITS[shift];
	}

	/**
	 * 根据文件的字节数量计算出改文件为多少 "Byte"/"KB"/"MB"/"GB"/"TB"/"PB"<br>
	 * 如果 unit 为 UNIT_AUTO(0)，则根据文件大小自动选择单位，如果存在小数均保留 <code>scale</code> 位小数
	 *
	 * @param fileSize 指定的文件大小
	 * @param unit 指定的文件单位
	 * @param scale 保留的小数位数（超出默认向下舍去）
	 * @see #calcFileSize(long, int, int, RoundingMode)
	 * @see #setFileSizeRoundingMode(RoundingMode)
	 * @since 0.0.1
	 */
	public static String calcFileSize(long fileSize, int unit, int scale) {
		return calcFileSize(fileSize, unit, scale, fileSizeRoundingMode);
	}

	/**
	 * 根据文件的字节数量计算出改文件为多少"Byte"/"KB"/"MB"/"GB"/"TB"/"PB"<br>
	 * 如果 unit 为 UNIT_AUTO(0)，则根据文件大小自动选择单位，如果存在小数则保留两位小数（超出默认向下舍去）
	 *
	 * @param fileSize 指定的文件大小
	 * @param unit 指定的文件单位
	 * @since 0.0.1
	 */
	public static String calcFileSize(long fileSize, int unit) {
		return calcFileSize(fileSize, unit, 2);
	}

	/**
	 * 文件大小除以指定度量，返回保留 scale 位小数的值（向下舍去）
	 *
	 * @param fileSize 文件大小
	 * @param divisor 指定文件单位的字节数
	 * @param scale 保留的小数位数
	 * @param roundingMode 舍入模式
	 * @since 3.0.0
	 */
	public static String divide(long fileSize, long divisor, int scale, RoundingMode roundingMode) {
		return BigDecimal.valueOf(fileSize).divide(BigDecimal.valueOf(divisor), scale, roundingMode).toString();
	}

	/**
	 * 文件大小除以指定度量，返回保留 scale 位小数的值（默认向下舍去）
	 *
	 * @param fileSize 文件大小
	 * @param divisor 指定文件单位的字节数
	 * @param scale 保留的小数位数
	 * @see #divide(long, long, int, RoundingMode)
	 * @see #setFileSizeRoundingMode(RoundingMode)
	 * @since 0.0.1
	 */
	public static String divide(long fileSize, long divisor, int scale) {
		return divide(fileSize, divisor, scale, fileSizeRoundingMode);
	}

	/**
	 * 删除指定路径的文件。<br>
	 * 如果该文件不存在则返回0<br>
	 * 如果该文件存在并成功删除则返回1<br>
	 * 其他情况返回-1
	 *
	 * @param filePath 文件路径(包含文件名)
	 * @since 0.0.1
	 */
	public static int deleteFile(String filePath) {
		return deleteFile(new File(filePath));
	}

	/**
	 * 删除指定路径的文件。<br>
	 * 如果该文件不存在则返回0<br>
	 * 如果该文件存在并成功删除则返回1<br>
	 * 其他情况(例如没有删除权限)返回-1
	 *
	 * @param directoryPath 文件所在的文件夹路径
	 * @param fileName 文件名
	 * @since 0.0.1
	 */
	public static int deleteFile(String directoryPath, String fileName) {
		return deleteFile(new File(directoryPath, fileName));
	}

	/**
	 * 删除指定的文件。<br>
	 * 如果该文件不存在则返回0<br>
	 * 如果该文件存在并成功删除则返回1<br>
	 * 其他情况(例如没有删除权限)返回-1
	 *
	 * @since 0.0.1
	 */
	private static int deleteFile(File file) {
		int result = -1;
		if (file.exists()) {
			if (file.delete()) {
				result = 1;
			}
		} else {
			result = 0;
		}
		return result;
	}

	/**
	 * 初步检测目标文件是否存在且可读
	 */
	public static void checkReadable(final File toRead) throws IOException {
		if (!toRead.exists()) {
			throw new NoSuchFileException(toRead.getAbsolutePath());
		}
		// 如果目标文件是一个目录
		if (toRead.isDirectory()) {
			throw new AccessDeniedException("File is a directory:" + toRead.getAbsolutePath());
		}
		// 如果目标文件不可写入数据
		if (!toRead.canRead()) {
			throw new AccessDeniedException("Unable to read file:" + toRead.getAbsolutePath());
		}
	}

	/**
	 * 初步检测目标文件是否可写入（不可写入，将直接报错）
	 */
	public static void checkWritable(final File toWrite, final boolean override) throws IOException {
		if (toWrite.exists()) {
			// 如果目标文件是一个目录
			if (toWrite.isDirectory()) {
				throw new AccessDeniedException(toWrite.getAbsolutePath());
			}
			// 如果目标文件不可写入数据
			if (!toWrite.canWrite()) {
				throw new AccessDeniedException("Unable to write to file:" + toWrite);
			}
			// 如果目标文件不允许被覆盖
			if (!override) {
				throw new FileAlreadyExistsException("File already exists:" + toWrite);
			}
		}
	}

	/**
	 * 检测目标文件路径是否可写，并为写入做准备（当所在目录不存在时将自动创建）
	 *
	 * @param target 目标文件
	 * @param override 如果已存在同名的文件，是否允许覆盖
	 */
	public static void checkAndPrepareForWrite(File target, boolean override) throws IOException {
		checkWritable(target, override);
		// 如果目标文件所在的目录不存在，则创建
		ensureParentDirExists(target);
	}

	/**
	 * 将指定的文件输入流写入到目标文件中
	 *
	 * @param is 指定的文件输入流
	 * @param dest 目标文件
	 * @since 0.0.1
	 */
	static void copyInternal(InputStream is, File dest) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(dest);
			writeStream(is, fos);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		} finally {
			closeSilently(is, fos);
		}
	}

	/**
	 * 通过文件流复制文件到指定路径
	 *
	 * @param is 指定的输入文件流
	 * @param dest 指定的目标文件对象
	 * @param override 如果目标文件已存在，是否允许覆盖
	 * @since 0.0.1
	 */
	public static void copyFile(InputStream is, File dest, boolean override) throws IOException {
		checkAndPrepareForWrite(dest, override);
		copyInternal(is, dest);
	}

	/**
	 * 将指定的文件复制到指定文件对象所表示的位置
	 *
	 * @param src 源文件对象
	 * @param dest 目标文件对象
	 * @param override 如果目标文件已存在，是否允许覆盖
	 * @since 0.0.1
	 */
	public static void copyFile(File src, File dest, boolean override) throws IOException {
		checkReadable(src);
		checkAndPrepareForWrite(dest, override);
		copyFileInternal(src, dest);
	}

	static void copyFileInternal(File src, File dest) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(src);
			copyInternal(fis, dest);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		} finally {
			closeSilently(fis);
		}
	}

	/**
	 * 将指定的文件复制到指定文件对象所表示的位置<br>
	 * 如果目标文件已存在，将引发异常
	 *
	 * @param src 源文件对象
	 * @param dest 目标文件对象
	 * @since 0.0.1
	 */
	public static void copyFile(File src, File dest) throws IOException {
		copyFile(src, dest, false);
	}

	/**
	 * 将指定的文件复制到指定的目标路径
	 *
	 * @param src 源文件路径
	 * @param dest 目标文件路径
	 * @since 0.0.1
	 */
	public static void copyFile(String src, String dest, boolean override) throws IOException {
		copyFile(new File(src), new File(dest), override);
	}

	/**
	 * 将指定的文件复制到指定的目标路径
	 *
	 * @param src 源文件路径
	 * @param dest 目标文件路径
	 * @since 0.0.1
	 */
	public static void copyFile(String src, String dest) throws IOException {
		copyFile(src, dest, false);
	}

	/**
	 * 将指定的文件复制到指定的目录，保持其原文件名
	 *
	 * @param file 指定的文件
	 * @param destDiretory 指定的目录
	 * @param override 如果已存在同名的文件，是否允许覆盖
	 */
	public static void copyFileToDirectory(File file, File destDiretory, boolean override) throws IOException {
		if (destDiretory.exists()) {
			// 如果目标文件是一个目录
			if (!destDiretory.isDirectory()) {
				throw new NotDirectoryException(destDiretory.getPath());
			}
			// 如果目标文件不可写入数据
			if (!destDiretory.canWrite()) {
				throw new AccessDeniedException("Unable to write to file：" + destDiretory);
			}
		}
		// 如果目标文件不允许被覆盖
		copyFile(file, new File(destDiretory, file.getName()), override);
	}

	/**
	 * 将指定的文件复制到指定的目录，保持其原文件名<br>
	 * 如果目标文件夹已存在同名的文件，则引发异常
	 *
	 * @param file 指定的文件
	 * @param diretory 指定的目录
	 * @since 0.0.1
	 */
	public static void copyFileToDirectory(File file, File diretory) throws IOException {
		copyFileToDirectory(file, diretory, false);
	}

	/**
	 * 将指定的文件复制到指定的目录，保持其原文件名
	 *
	 * @param file 指定的文件
	 * @param destDiretory 指定的目录
	 * @param override 如果已存在同名的文件，是否允许覆盖
	 * @since 0.0.1
	 */
	public static void copyFileToDirectory(String file, String destDiretory, boolean override) throws IOException {
		copyFileToDirectory(new File(file), new File(destDiretory), override);
	}

	/**
	 * 将指定的文件复制到指定的目录，保持其原文件名<br>
	 * 如果目标文件夹已存在同名的文件，则引发异常
	 *
	 * @param file 指定的文件
	 * @param destDiretory 指定的目录
	 * @since 0.0.1
	 */
	public static void copyFileToDirectory(String file, String destDiretory) throws IOException {
		copyFileToDirectory(file, destDiretory, false);
	}

	/**
	 * 移动指定的文件到目标文件路径
	 *
	 * @param src 指定的文件
	 * @param dest 目标文件
	 * @param override 如果已存在同名的文件，是否允许覆盖
	 * @since 0.0.1
	 */
	public static void moveFile(File src, File dest, boolean override) throws IOException {
		checkReadable(src);
		checkAndPrepareForWrite(dest, override);
		moveFileInternal(src, dest, override);
	}

	/**
	 * 确保指定文件所在的目录已存在（如果不存在，则创建）
	 *
	 * @param target 指定文件
	 */
	public static void ensureParentDirExists(File target) {
		File parent = target.getParentFile();
		if (!parent.exists() && !parent.mkdirs()) {
			throw new UncheckedIOException(new AccessDeniedException(parent.getAbsolutePath()));
		}
	}

	/**
	 * 移动指定的文件到目标文件路径<br>
	 * 如果目标文件夹已存在同名的文件，则引发异常
	 *
	 * @param src 指定的文件
	 * @param dest 目标文件
	 * @since 0.0.1
	 */
	public static void moveFile(File src, File dest) throws IOException {
		moveFile(src, dest, false);
	}

	/**
	 * 移动指定的文件到目标文件路径
	 *
	 * @param path 指定的文件
	 * @param destPath 目标文件
	 * @param override 如果已存在同名的文件，是否允许覆盖
	 * @since 0.0.1
	 */
	public static void moveFile(String path, String destPath, boolean override) throws IOException {
		moveFile(new File(path), new File(destPath), override);
	}

	/**
	 * 移动指定的文件到目标文件路径<br>
	 * 如果目标文件夹已存在同名的文件，则引发异常
	 *
	 * @param path 指定的文件
	 * @param dest 目标文件
	 * @since 0.0.1
	 */
	public static void moveFile(String path, String dest) throws IOException {
		moveFile(new File(path), new File(dest), false);
	}

	/**
	 * 移动指定的文件到目标文件夹
	 *
	 * @param file 指定的文件
	 * @param destDirectory 目标文件夹
	 * @param override 如果已存在同名的文件，是否允许覆盖
	 * @throws IllegalArgumentException 如果指定的文件或目录不可写，或指定的目录不是目录
	 * @since 0.0.1
	 */
	public static void moveFileToDirectory(File file, File destDirectory, boolean override) throws IllegalArgumentException, IOException {
		checkReadable(file);
		final File dest = new File(destDirectory, file.getName());
		checkAndPrepareForWrite(dest, override);
		moveFileInternal(file, dest, override);
	}

	static void moveFileInternal(File src, File dest, boolean override) throws FileAlreadyExistsException {
		if (!src.renameTo(dest)) {
			if (!override) {
				throw new FileAlreadyExistsException("Move file failed:[" + src + "] => [" + dest + ']');
			}
			copyFileInternal(src, dest);
			src.delete();
		}
	}

	/**
	 * 移动指定的文件到目标文件夹<br>
	 * 如果目标文件夹已存在同名的文件，则引发异常
	 *
	 * @param file 指定的文件
	 * @param directory 目标文件夹
	 * @since 0.0.1
	 */
	public static void moveFileToDirectory(File file, File directory) throws IOException {
		moveFileToDirectory(file, directory, false);
	}

	/**
	 * 移动指定的文件到目标文件夹
	 *
	 * @param path 指定的文件
	 * @param directory 目标文件夹
	 * @param override 如果已存在同名的文件，是否允许覆盖
	 * @since 0.0.1
	 */
	public static void moveFileToDirectory(String path, String directory, boolean override) throws IOException {
		moveFileToDirectory(new File(path), new File(directory), override);
	}

	/**
	 * 移动指定的文件到目标文件夹<br>
	 * 如果目标文件夹已存在同名的文件，则引发异常
	 *
	 * @param path 指定的文件
	 * @param directory 目标文件夹
	 * @since 0.0.1
	 */
	public static void moveFileToDirectory(String path, String directory) throws IOException {
		moveFileToDirectory(path, directory, false);
	}

	/**
	 * 将指定的输入流写入到指定的输出流中<br>
	 * 注意：该方法内部只负责写入，不负责关闭相关流资源
	 *
	 * @param in 指定的输入流
	 * @param out 指定的输出流
	 * @since 0.0.1
	 */
	public static void writeStream(InputStream in, OutputStream out) throws IOException {
		if (in instanceof FileInputStream && out instanceof FileOutputStream) {
			writeStream((FileInputStream) in, (FileOutputStream) out);
			return;
		}
		int length;
		final byte[] buffer = new byte[8192];
		while ((length = in.read(buffer)) != -1) {
			out.write(buffer, 0, length);
		}
		out.flush();
	}

	/**
	 * 将指定的输入流写入到指定的输出流中<br>
	 * 注意该方法内部只负责写入，不负责关闭相关流资源
	 *
	 * @param fis 指定的文件输入流
	 * @param fos 指定的文件输出流
	 * @since 0.4.2
	 */
	public static void writeStream(FileInputStream fis, FileOutputStream fos) throws IOException {
		FileChannel inChannel = fis.getChannel();
		FileChannel outChannel = fos.getChannel();
		final long total = inChannel.size();
		long copied = 0;
		try {
			do {
				// 复制超大文件时，可能无法一次性复制完毕，所以需要完善分段传输检测
				copied += inChannel.transferTo(copied, total, outChannel);
			} while (copied < total);
		} finally {
			closeSilently(outChannel);
			closeSilently(inChannel);
		}
	}

	/**
	 * 关闭指定的输入输出流<br>
	 * 内部会先关闭输出流，再关闭输入流
	 *
	 * @param in 输入流
	 * @param out 输出流
	 * @since 3.0.0
	 */
	public static void close(@Nullable InputStream in, @Nullable OutputStream out) throws IOException {
		try {
			close(out);
		} finally {
			close(in);
		}
	}

	/**
	 * 关闭指定的资源
	 *
	 * @param closeable 资源
	 * @since 3.0.0
	 */
	public static void close(@Nullable Closeable closeable) throws IOException {
		if (closeable != null) {
			closeable.close();
		}
	}

	/**
	 * 关闭指定的资源
	 *
	 * @param closeable 资源
	 * @since 3.0.0
	 */
	public static void closeSilently(@Nullable Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ignore) {
			}
		}
	}

	/**
	 * 静默地关闭指定的输入输出流<br>
	 * 内部会先关闭输出流，再关闭输入流
	 *
	 * @param in 输入流
	 * @param out 输出流
	 * @since 3.0.0
	 */
	public static void closeSilently(@Nullable InputStream in, @Nullable OutputStream out) {
		closeSilently(out);
		closeSilently(in);
	}

	/**
	 * 将指定文件复制到指定的目录，并且采用随机的文件名，方法内部会尽可能地确保文件名称不会重复
	 *
	 * @param file 指定的文件对象
	 * @param destDir 目标目录
	 * @return 返回复制后的目标文件对象
	 * @since 0.0.1
	 */
	public static File copyFileToDirectoryWithRandomFileName(File file, String destDir) throws IOException {
		File dest = getRandomFile(destDir, getExtension(file.getName()));
		copyFile(file, dest);
		return dest;
	}

	/**
	 * 将指定文件移动到指定的目录，并且采用随机的文件名，方法内部会尽可能地确保文件名称不会重复
	 *
	 * @param file 指定的文件对象
	 * @param destDir 目标目录
	 * @return 返回移动后的目标文件对象
	 * @since 0.0.1
	 */
	public static File moveFileToDirectoryWithRandomFileName(File file, String destDir) throws IOException {
		File dest = getRandomFile(destDir, getExtension(file.getName()));
		moveFile(file, dest);
		return dest;
	}

	/**
	 * 将指定文件复制到指定的目录，并且采用随机的文件名、指定的文件后缀，方法内部会尽可能地确保文件名称不会重复
	 *
	 * @param file 指定的文件对象
	 * @param destDir 目标目录
	 * @param prefix 目标文件的文件名前缀(可以为null)
	 * @param suffix 目标文件的文件后缀。null、""、"gif"、".gif"等形式均可，前两者表示没有后缀，后两者表示指定的后缀。
	 * @return 返回复制后的目标文件对象
	 * @since 0.0.1
	 */
	public static File copyFileToDirectoryWithRandomFileName(File file, String destDir, String prefix, String suffix) throws IOException {
		File dest = getRandomFile(destDir, prefix, suffix);
		copyFile(file, dest);
		return dest;
	}

	/**
	 * 将指定文件复制到指定的目录，并且采用随机的文件名、指定的文件后缀，方法内部会尽可能地确保文件名称不会重复
	 *
	 * @param file 指定的文件对象
	 * @param destDir 目标目录
	 * @param suffix 目标文件的文件后缀。null、""、"gif"、".gif"等形式均可，前两者表示没有后缀，后两者表示指定的后缀。
	 * @return 返回复制后的目标文件对象
	 * @since 0.0.1
	 */
	public static File copyFileToDirectoryWithRandomFileName(File file, String destDir, String suffix) throws IOException {
		return copyFileToDirectoryWithRandomFileName(file, destDir, null, suffix);
	}

	/**
	 * 将指定文件移动到指定的目录，并且采用随机的文件名、指定的文件后缀，方法内部会尽可能地确保文件名称不会重复
	 *
	 * @param file 指定的文件对象
	 * @param destDir 目标目录
	 * @param prefix 目标文件的文件名前缀(可以为null)
	 * @param suffix 目标文件的文件后缀。null、""、"gif"、".gif"等形式均可，前两者表示没有后缀，后两者表示指定的后缀。
	 * @return 返回移动后的目标文件对象
	 * @since 0.0.1
	 */
	public static File moveFileToDirectoryWithRandomFileName(File file, String destDir, String prefix, String suffix) throws IOException {
		File dest = getRandomFile(destDir, prefix, suffix);
		moveFile(file, dest);
		return dest;
	}

	/**
	 * 将指定文件移动到指定的目录，并且采用随机的文件名、指定的文件后缀，方法内部会尽可能地确保文件名称不会重复
	 *
	 * @param file 指定的文件对象
	 * @param destDir 目标目录
	 * @param suffix 目标文件的文件后缀。null、""、"gif"、".gif"等形式均可，前两者表示没有后缀，后两者表示指定的后缀。
	 * @return 返回移动后的目标文件对象
	 * @since 0.0.1
	 */
	public static File moveFileToDirectoryWithRandomFileName(File file, String destDir, String suffix) throws IOException {
		return moveFileToDirectoryWithRandomFileName(file, destDir, null, suffix);
	}

	/**
	 * 将相对于 classpath 的文件路径解析为对应的文件对象
	 *
	 * @param pathname 相对于classpath的文件路径（请不要带 {@code "classpath:"} 前缀）
	 */
	public static File parseClassPathFile(String pathname) {
		final char char0 = pathname.charAt(0);
		if (char0 != '/' && char0 != '\\') {
			pathname = '/' + pathname;
		}
		java.net.URL resource = FileUtil.class.getResource(pathname);
		return resource == null ? null : new File(resource.getPath());
	}

	/**
	 * 根据指定的文件路径获取对应的File对象
	 *
	 * @param pathname 指定的文件路径
	 * @param inClassPath 是否相对于classpath类路径下
	 * @since 0.3.1
	 */
	public static File getFile(String pathname, boolean inClassPath) {
		return inClassPath ? parseClassPathFile(pathname) : new File(pathname);
	}

	/**
	 * 解析指定的类路径文件名称，并返回对应的文件路径
	 *
	 * @param pathname 文件名可以为"classpath:"开头，内部会自动判断并将其转换为相应的绝对路径
	 * @param checkExists 是否检查文件是否存在，如果为 true，指定的文件不存在时将报错
	 * @since 3.0.0
	 */
	public static File parseFile(String pathname, boolean checkExists) {
		final String classPathPrefix = "classpath:";
		File file = pathname.startsWith(classPathPrefix) ? parseClassPathFile(pathname.substring(classPathPrefix.length())) : new File(pathname);
		if (checkExists && !file.exists()) {
			throw new IllegalArgumentException("File not found：[" + file.getPath() + ']');
		}
		return file;
	}

	/**
	 * 读取指定的文件内容
	 *
	 * @return 返回文件内容字符串，内部换行符为'\n'
	 * @since 0.3.1
	 */
	public static String readContent(File file) throws IOException {
		checkReadable(file);
		BufferedReader reader = null;
		long length = (file.length() >>> 3) + 1;
		final StringBuilder sb = new StringBuilder((int) length);
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			if (line != null) {
				sb.append(line);
			}
			while ((line = reader.readLine()) != null) {
				sb.append('\n').append(line);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			closeSilently(reader);
		}
		return sb.toString();
	}

	/**
	 * 读取指定的文件内容
	 *
	 * @param inClassPath 是否相对于classpath类路径下
	 * @return 返回文件内容字符串，内部换行符为'\n'
	 * @since 0.3.1
	 */
	public static String readContent(String pathname, boolean inClassPath) throws IOException {
		return readContent(getFile(pathname, inClassPath));
	}

	/**
	 * 向文件中写入指定的数据
	 *
	 * @since 3.0.0
	 */
	public static void writeContent(File file, final InputStream is, boolean append) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file, append)) {
			writeStream(is, fos);
		}
	}

	/**
	 * 向文件中写入指定的数据
	 *
	 * @since 3.0.0
	 */
	public static void writeContent(File file, final byte[] data, boolean append) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file, append)) {
			fos.write(data);
		}
	}

	/**
	 * 向文件中写入指定的文本内容
	 *
	 * @since 3.0.0
	 */
	public static void writeContent(File file, final String data, boolean append) throws IOException {
		ensureParentDirExists(file);
		try (FileWriter writer = new FileWriter(file, append)) {
			writer.write(data);
		}
	}

	/**
	 * 读取指定名称的 ".properties" 文件
	 *
	 * @param file 指定的文件
	 * @return 对应的 Properties 对象(出于泛型兼容考虑 ， 以 {@code Map< String, String >} 形式返回)。如果指定的文件不存在，则返回 null
	 * @since 3.0.0
	 */
	public static Map<String, String> readProperties(File file) {
		try (FileReader reader = new FileReader(file)) {
			Properties prop = new Properties();
			prop.load(reader);
			return X.castType(prop);
		} catch (FileNotFoundException fe) {
			return null; // 如果文件不存在，则返回 null
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * 读取指定名称的".properties"文件
	 *
	 * @param pathname 指定的文件路径
	 * @param inClassPath 是否相对于classpath类路径下
	 * @return 对应的 Properties 对象(出于泛型兼容考虑 ， 以 Map < String, String > 形式返回)。如果指定的文件不存在，则返回 null
	 * @since 0.3.1
	 */
	public static Map<String, String> readProperties(String pathname, boolean inClassPath) {
		return readProperties(FileUtil.getFile(pathname, inClassPath));
	}

}