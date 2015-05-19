package me.ready.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.ready.e.LogicException;

/**
 * 用于文件操作的公共工具类
 * 
 * @author Ready
 * @date 2013-4-9
 */
public abstract class FileUtil {

	/**
	 * 用于表示文件大小的单位
	 */
	private static final String[] FILE_UNITS = new String[] { "Byte", "KB", "MB", "GB", "TB", "PB" };
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

	/**
	 * 根据文件路径获取对应的文件扩展名<br>
	 * 如果没有指定的后缀，则返回空字符串""
	 * 
	 * @param path 指定的文件路径
	 * @param removeDot 是否移除点号
	 * @return
	 */
	public static final String getExtension(String path, boolean removeDot) {
		int pos = path.lastIndexOf('.');
		if (pos == -1) {
			return "";
		} else if (removeDot) {
			return path.substring(pos + 1);
		} else {
			return path.substring(pos);
		}
	}

	/**
	 * 根据文件路径获取对应的文件扩展名(例如：".jpg"、".gif"等)<br>
	 * 如果没有指定的后缀，则返回空字符串""
	 * 
	 * @param path 指定的文件路径
	 * @return
	 */
	public static final String getExtension(String path) {
		return getExtension(path, false);
	}

	/**
	 * 获取指定文件路径中的文件名称部分
	 * 
	 * @param path 指定的文件路径
	 * @return
	 */
	public static final String getFileName(String path) {
		return getFileName(path, false);
	}

	/**
	 * 获取指定文件路径中的文件名称部分
	 * 
	 * @param path 指定的文件路径
	 * @param withoutExt 是否需要去除文件扩展名
	 * @return
	 */
	public static final String getFileName(String path, boolean withoutExt) {
		String str = new File(path).getName();
		if (withoutExt) {
			int pos = str.lastIndexOf(',');
			if (pos > -1) {
				str = str.substring(0, pos);
			}
		}
		return str;
	}

	/**
	 * 获取随机文件名，根据当前时间采用随机算法自动生成，并且内部保证本地没有重复文件名的文件
	 * 
	 * @param path
	 * @param prefix 文件名前缀(可以为null)
	 * @param suffix
	 * @return
	 */
	public static final File getRandomFile(String path, String prefix, String suffix) {
		String fileName = new SimpleDateFormat("yyyyMMdd-HHmmssSSS").format(new Date());
		if (prefix != null) {
			fileName = prefix + fileName;
		}
		if (suffix == null) {
			suffix = "";
		} else if (suffix.length() > 0) { // 如果有后缀就+"."
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
	 * @param path
	 * @param suffix
	 * @return
	 */
	public static final File getRandomFile(String path, String suffix) {
		return getRandomFile(path, null, suffix);
	}

	/**
	 * 根据文件的字节数量计算出改文件为多少"Byte"/"KB"/"MB"/"GB"/"TB"/"PB"<br>
	 * 如果unit为UNIT_AUTO(0)，则根据文件大小自动选择单位，如果存在小数均保留<code>scale</code>位小数<br>
	 * 
	 * @param fileSize 指定的文件大小
	 * @param unit 指定的文件单位
	 * @param scale 保留的小数位数
	 * @return
	 */
	public static String calcFileSize(long fileSize, int unit, int scale) {
		if (fileSize < 0) {
			throw new IllegalArgumentException("需要计算的文件大小不能为负数:" + fileSize);
		}
		if (unit < UNIT_AUTO || unit > UNIT_PB) {
			throw new IllegalArgumentException("无法识别的文件大小单位：" + unit);
		}
		if (unit != UNIT_AUTO) {
			int shift = unit - 1;
			return divide(fileSize, 1 << (10 * shift), scale) + FILE_UNITS[shift];
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
		return divide(fileSize, unitBytes, scale) + FILE_UNITS[shift];
	}

	/**
	 * 根据文件的字节数量计算出改文件为多少"Byte"/"KB"/"MB"/"GB"/"TB"/"PB"<br>
	 * 如果unit为UNIT_AUTO(0)，则根据文件大小自动选择单位，如果存在小数均保留两位小数<br>
	 * 
	 * @param fileSize 指定的文件大小
	 * @param unit 指定的文件单位
	 * @return
	 */
	public static String calcFileSize(long fileSize, int unit) {
		return calcFileSize(fileSize, unit, 2);
	}

	/**
	 * 文件大小除以指定度量，返回保留scale位小数的值
	 * 
	 * @param fileSize 文件大小
	 * @param divisor 指定文件单位的字节数
	 * @param scale 保留的小数位数
	 * @return
	 */
	public static String divide(long fileSize, long divisor, int scale) {
		return new BigDecimal(fileSize).divide(new BigDecimal(divisor), scale, RoundingMode.HALF_UP).toString();
	}

	/**
	 * 删除指定路径的文件。<br>
	 * 如果该文件不存在则返回0<br>
	 * 如果该文件存在并成功删除则返回1<br>
	 * 其他情况返回-1
	 * 
	 * @param filePath 文件路径(包含文件名)
	 * @return
	 */
	public static int deleteFile(String filePath) {
		return deleteFile(new File(filePath));
	}

	/**
	 * 删除指定路径的文件。<br>
	 * 如果该文件不存在则返回0<br>
	 * 如果该文件存在并成功删除则返回1<br>
	 * 其他情况返回-1
	 * 
	 * @param directoryPath 文件所在的文件夹路径
	 * @param fileName 文件名
	 * @return
	 */
	public static int deleteFile(String directoryPath, String fileName) {
		return deleteFile(new File(directoryPath, fileName));
	}

	/**
	 * 删除指定的文件。<br>
	 * 如果该文件不存在则返回0<br>
	 * 如果该文件存在并成功删除则返回1<br>
	 * 其他情况返回-1
	 * 
	 * @param file
	 * @return
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
	 * 将指定的文件输入流写入到目标文件中
	 * 
	 * @param is 指定的文件输入流
	 * @param target 目标文件
	 */
	private static final void copy(InputStream is, File target) {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = new FileOutputStream(target);
			bos = new BufferedOutputStream(fos);
			writeStream(is, bos);
		} catch (Exception e) {
			throw new LogicException(e);
		} finally {
			closeResources(is, bos);
		}
	}

	/**
	 * 通过文件流复制文件到指定路径
	 * 
	 * @param is 指定的输入文件流
	 * @param target 指定的目标文件对象
	 * @param override 如果目标文件已存在，是否允许覆盖
	 * @return
	 */
	public static final void copyFile(InputStream is, File target, boolean override) {
		if (target.exists()) {
			// 如果目标文件是一个目录
			if (target.isDirectory()) {
				throw new IllegalStateException("目标文件是一个目录：" + target);
			}
			// 如果目标文件不可写入数据
			if (!target.canWrite()) {
				throw new IllegalStateException("目标文件不可写：" + target);
			}
			// 如果目标文件不允许被覆盖
			if (!override) {
				throw new IllegalStateException("目标文件已存在：" + target);
			}
		} else {
			// 如果目标文件所在的目录不存在，则创建
			File parent = target.getParentFile();
			if (!parent.exists() && !parent.mkdirs()) {
				throw new IllegalStateException("无法创建目标文件的所在目录：" + parent);
			}
		}
		copy(is, target);
	}

	/**
	 * 将指定的文件复制到指定文件对象所表示的位置
	 * 
	 * @param src 源文件对象
	 * @param target 目标文件对象
	 * @param override 如果目标文件已存在，是否允许覆盖
	 * @return
	 */
	public final static void copyFile(File src, File target, boolean override) {
		if (!src.exists()) {
			throw new IllegalStateException("指定的文件不存在：" + src);
		}
		if (!src.canRead()) {
			throw new IllegalStateException("无法读取指定的文件：" + src);
		}
		try {
			copyFile(new FileInputStream(src), target, override);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 将指定的文件复制到指定文件对象所表示的位置<br>
	 * 如果目标文件已存在，将引发异常
	 * 
	 * @param src 源文件对象
	 * @param target 目标文件对象
	 * @return
	 */
	public final static void copyFile(File src, File target) {
		copyFile(src, target, false);
	}

	/**
	 * 将指定的文件复制到指定的目标路径
	 * 
	 * @param src 源文件对象
	 * @param target 目标文件对象
	 * @return
	 */
	public final static void copyFile(String src, String target, boolean override) {
		try {
			copyFile(new FileInputStream(src), new File(target), override);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 将指定的文件复制到指定的目标路径
	 * 
	 * @param src 源文件路径
	 * @param target 目标文件路径
	 * @return
	 */
	public final static void copyFile(String src, String target) {
		copyFile(src, target, false);
	}

	/**
	 * 将指定的文件复制到指定的目录，保持其原文件名
	 * 
	 * @param file 指定的文件
	 * @param directory 指定的目录
	 * @param override 如果已存在同名的文件，是否允许覆盖
	 */
	public final static void copyFileToDirectory(File file, File directory, boolean override) {
		File target = null;
		if (directory.exists()) {
			// 如果目标文件是一个目录
			if (!directory.isDirectory()) {
				throw new IllegalStateException("目标文件夹不是一个目录：" + directory);
			}
			// 如果目标文件不可写入数据
			if (!directory.canWrite()) {
				throw new IllegalStateException("目标文件夹不可写入：" + directory);
			}
			// 如果目标文件不允许被覆盖
			target = new File(directory, file.getName());
			if (target.exists() && !override) {
				throw new IllegalStateException("目标文件夹已存在同名的文件：" + target);
			}
		} else {
			// 如果目标文件所在的目录不存在，则创建之
			// if (!diretory.mkdirs()) {
			// throw new LogicException("");
			// }
			target = new File(directory, file.getName());
		}
		copyFile(file, target, true);
	}

	/**
	 * 将指定的文件复制到指定的目录，保持其原文件名<br>
	 * 如果目标文件夹已存在同名的文件，则引发异常
	 * 
	 * @param file 指定的文件
	 * @param diretory 指定的目录
	 */
	public final static void copyFileToDirectory(File file, File diretory) {
		copyFileToDirectory(file, diretory, false);
	}

	/**
	 * 将指定的文件复制到指定的目录，保持其原文件名
	 * 
	 * @param file 指定的文件
	 * @param diretory 指定的目录
	 * @param override 如果已存在同名的文件，是否允许覆盖
	 */
	public final static void copyFileToDirectory(String file, String diretory, boolean override) {
		copyFileToDirectory(new File(file), new File(diretory), override);
	}

	/**
	 * 将指定的文件复制到指定的目录，保持其原文件名<br>
	 * 如果目标文件夹已存在同名的文件，则引发异常
	 * 
	 * @param file 指定的文件
	 * @param diretory 指定的目录
	 */
	public final static void copyFileToDirectory(String file, String diretory) {
		copyFileToDirectory(file, diretory, false);
	}

	/**
	 * 移动指定的文件到目标文件路径
	 * 
	 * @param file 指定的文件
	 * @param target 目标文件
	 * @param override 如果已存在同名的文件，是否允许覆盖
	 */
	public static final void moveFile(File file, File target, boolean override) {
		if (!file.canWrite()) {
			throw new IllegalStateException("指定的文件不存在或无法删除：" + file);
		}
		copyFile(file, target, override);
		file.delete();
	}

	/**
	 * 移动指定的文件到目标文件路径<br>
	 * 如果目标文件夹已存在同名的文件，则引发异常
	 * 
	 * @param file 指定的文件
	 * @param target 目标文件
	 */
	public static final void moveFile(File file, File target) {
		moveFile(file, target, false);
	}

	/**
	 * 移动指定的文件到目标文件路径
	 * 
	 * @param path 指定的文件
	 * @param target 目标文件
	 * @param override 如果已存在同名的文件，是否允许覆盖
	 */
	public static final void moveFile(String path, String target, boolean override) {
		moveFile(new File(path), new File(target), override);
	}

	/**
	 * 移动指定的文件到目标文件路径<br>
	 * 如果目标文件夹已存在同名的文件，则引发异常
	 * 
	 * @param path 指定的文件
	 * @param target 目标文件
	 */
	public static final void moveFile(String path, String target) {
		moveFile(new File(path), new File(target), false);
	}

	/**
	 * 移动指定的文件到目标文件夹
	 * 
	 * @param file 指定的文件
	 * @param directory 目标文件夹
	 * @param override 如果已存在同名的文件，是否允许覆盖
	 */
	public static final void moveFileToDirectory(File file, File directory, boolean override) {
		if (!file.canWrite()) {
			throw new IllegalStateException("无法删除指定的文件：" + file);
		}
		copyFileToDirectory(file, directory, override);
		file.delete();
	}

	/**
	 * 移动指定的文件到目标文件夹<br>
	 * 如果目标文件夹已存在同名的文件，则引发异常
	 * 
	 * @param file 指定的文件
	 * @param directory 目标文件夹
	 */
	public static final void moveFileToDirectory(File file, File directory) {
		moveFileToDirectory(file, directory, false);
	}

	/**
	 * 移动指定的文件到目标文件夹
	 * 
	 * @param file 指定的文件
	 * @param directory 目标文件夹
	 * @param override 如果已存在同名的文件，是否允许覆盖
	 */
	public static final void moveFileToDirectory(String path, String directory, boolean override) {
		moveFileToDirectory(new File(path), new File(directory), override);
	}

	/**
	 * 移动指定的文件到目标文件夹<br>
	 * 如果目标文件夹已存在同名的文件，则引发异常
	 * 
	 * @param file 指定的文件
	 * @param directory 目标文件夹
	 */
	public static final void moveFileToDirectory(String path, String directory) {
		moveFileToDirectory(path, directory, false);
	}

	/**
	 * 将指定的输入流写入到指定的输出流中<br>
	 * 注意该方法内部只负责写入，不负责关闭相关流资源
	 * 
	 * @param in 指定的输入流
	 * @param out 指定的输出流
	 * @throws IOException
	 */
	public static final void writeStream(InputStream in, OutputStream out) throws IOException {
		int length = 0;
		byte[] buffer = new byte[4096];
		while ((length = in.read(buffer)) != -1) {
			out.write(buffer, 0, length);
		}
		out.flush();
	}

	/**
	 * 关闭一组指定的文件流资源<br>
	 * 内部会先关闭输出流，再关闭输入流
	 * 
	 * @param in 输入流
	 * @param out 输出流
	 */
	public static final void closeResources(InputStream in, OutputStream out) {
		try {
			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
			throw new LogicException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new LogicException(e);
				}
			}
		}
	}

	/**
	 * 将指定文件复制到指定的目录，并且采用随机的文件名，方法内部会尽可能地确保文件名称不会重复
	 * 
	 * @param file 指定的文件对象
	 * @param targetDir 目标目录
	 * @return 返回复制后的目标文件对象
	 */
	public static final File copyFileToDirectoryWithRandomFileName(File file, String targetDir) {
		File target = getRandomFile(targetDir, getExtension(file.getName()));
		copyFile(file, target);
		return target;
	}

	/**
	 * 将指定文件移动到指定的目录，并且采用随机的文件名，方法内部会尽可能地确保文件名称不会重复
	 * 
	 * @param file 指定的文件对象
	 * @param targetDir 目标目录
	 * @return 返回移动后的目标文件对象
	 */
	public static final File moveFileToDirectoryWithRandomFileName(File file, String targetDir) {
		File target = getRandomFile(targetDir, getExtension(file.getName()));
		moveFile(file, target);
		return target;
	}

	/**
	 * 将指定文件复制到指定的目录，并且采用随机的文件名、指定的文件后缀，方法内部会尽可能地确保文件名称不会重复
	 * 
	 * @param file 指定的文件对象
	 * @param targetDir 目标目录
	 * @param prefix 目标文件的文件名前缀(可以为null)
	 * @param suffix 目标文件的文件后缀。null、""、"gif"、".gif"等形式均可，前两者表示没有后缀，后两者表示指定的后缀。
	 * @return 返回复制后的目标文件对象
	 */
	public static final File copyFileToDirectoryWithRandomFileName(File file, String targetDir, String prefix, String suffix) {
		File target = getRandomFile(targetDir, prefix, suffix);
		copyFile(file, target);
		return target;
	}

	/**
	 * 将指定文件复制到指定的目录，并且采用随机的文件名、指定的文件后缀，方法内部会尽可能地确保文件名称不会重复
	 * 
	 * @param file 指定的文件对象
	 * @param targetDir 目标目录
	 * @param suffix 目标文件的文件后缀。null、""、"gif"、".gif"等形式均可，前两者表示没有后缀，后两者表示指定的后缀。
	 * @return 返回复制后的目标文件对象
	 */
	public static final File copyFileToDirectoryWithRandomFileName(File file, String targetDir, String suffix) {
		return copyFileToDirectoryWithRandomFileName(file, targetDir, null, suffix);
	}

	/**
	 * 将指定文件移动到指定的目录，并且采用随机的文件名、指定的文件后缀，方法内部会尽可能地确保文件名称不会重复
	 * 
	 * @param file 指定的文件对象
	 * @param targetDir 目标目录
	 * @param prefix 目标文件的文件名前缀(可以为null)
	 * @param suffix 目标文件的文件后缀。null、""、"gif"、".gif"等形式均可，前两者表示没有后缀，后两者表示指定的后缀。
	 * @return 返回移动后的目标文件对象
	 */
	public static final File moveFileToDirectoryWithRandomFileName(File file, String targetDir, String prefix, String suffix) {
		File target = getRandomFile(targetDir, prefix, suffix);
		moveFile(file, target);
		return target;
	}

	/**
	 * 将指定文件移动到指定的目录，并且采用随机的文件名、指定的文件后缀，方法内部会尽可能地确保文件名称不会重复
	 * 
	 * @param file 指定的文件对象
	 * @param targetDir 目标目录
	 * @param suffix 目标文件的文件后缀。null、""、"gif"、".gif"等形式均可，前两者表示没有后缀，后两者表示指定的后缀。
	 * @return 返回移动后的目标文件对象
	 */
	public static final File moveFileToDirectoryWithRandomFileName(File file, String targetDir, String suffix) {
		return moveFileToDirectoryWithRandomFileName(file, targetDir, null, suffix);
	}
}
