package me.ready.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.ready.e.LogicException;

/**
 * 用于文件操作的公共工具类
 * @author Ready
 * @date 2013-4-9
 */
public class FileUtil {

	private static final SimpleDateFormat FILENAME_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");

	// 禁止实例构建
	private FileUtil() {
	}

	/**
	 * 获取随机文件名，根据当前时间采用随机算法自动生成，并且内部保证本地没有重复文件名的文件
	 * @param path
	 * @param suffix
	 * @return
	 */
	public static String getRandomFileName(String path, String suffix) {
		String fileName = FILENAME_DATE_FORMAT.format(new Date());
		if (suffix == null) {
			suffix = "";
		} else if (suffix.length() > 0) { // 如果有后缀就+"."
			suffix = '.' + suffix;
		}
		String destFileName = fileName + suffix;
		File file = new File(path, destFileName);
		if (file.exists()) {
			do {
				destFileName = fileName + '-' + getRandomString() + suffix;
				file = new File(path, destFileName);
			} while (file.exists());
		}
		return destFileName;
	}

	/**
	 * 获取随机4位字符串
	 * @return
	 */
	public static final String getRandomString() {
		String str = new Double(Math.random()).toString();
		int index = str.indexOf('.');// 0.123456
		index++;
		int lastIndex = index + 4;
		int length = str.length();
		if (length >= lastIndex) { // 如果小数部分有4位
			str = str.substring(index, lastIndex);
		} else {
			str = str.substring(index);
			str = X.zeroFill(str, 4);
		}
		return str;
	}

	/**
	 * 根据指定的带后缀的文件名，返回文件后缀(不含"."号)<br>
	 * 有些文件可能没有后缀，则返回""
	 * @param fileName
	 * @return
	 */
	public static String getFileSuffix(String fileName) {
		int index = fileName.lastIndexOf('.');
		int length = fileName.length();
		index++;
		if (length > index) {
			return fileName.substring(index);
		}
		return "";
	}

	/**
	 * 根据文件的字节数量计算出改文件为多少"Byte"/"KB"/"MB"/"GB"<br>
	 * 如果unit为null，则根据文件大小自动选择单位，如果存在小数均保留两位小数<br>
	 * 返回的单位均为注释中所备注的大小写格式
	 * @param fileSize
	 * @return
	 */
	public static String calcFileSize(String unit, int fileSize) {
		if (fileSize < 0) {
			throw new LogicException("需要计算的文件大小不能为负数！");
		}
		if (!X.isEmpty(unit)) {
			if ("Byte".equalsIgnoreCase(unit)) {
				return fileSize + "Byte";
			} else if ("KB".equalsIgnoreCase(unit)) {
				return divide(fileSize, 1 << 10) + "KB";
			} else if ("MB".equalsIgnoreCase(unit)) {
				return divide(fileSize, 1 << 20) + "MB";
			} else if ("GB".equalsIgnoreCase(unit)) {
				return divide(fileSize, 1 << 30) + "GB";
			}
		}
		// 如果没有传入单位或无法识别单位，自动选择
		long bytes = 1 << 10;
		if (fileSize < bytes) {
			return fileSize + "Byte";
		}
		bytes <<= 10;
		if (fileSize < bytes) { // 如果是KB
			return divide(fileSize, 1 << 10) + "KB";
		}
		bytes <<= 10;
		if (fileSize < bytes) {
			return divide(fileSize, 1 << 20) + "MB";
		}
		bytes <<= 10;
		if (fileSize < bytes) {
			return divide(fileSize, 1 << 30) + "GB";
		}
		return null;
	}

	/**
	 * 文件大小除以指定度量，返回保留2位小数的值
	 * @param fileSize
	 * @param divisor
	 * @return
	 */
	public static String divide(int fileSize, long divisor) {
		return new BigDecimal(fileSize).divide(new BigDecimal(divisor), 2, RoundingMode.HALF_UP).toString();
	}

	/**
	 * 删除指定路径的文件。<br>
	 * 如果该文件不存在则返回0<br>
	 * 如果该文件存在并成功删除则返回1<br>
	 * 其他情况返回-1
	 * @param filePath 文件路径(包含文件名)
	 * @return
	 */
	public static int delete(String filePath) {
		return delete(new File(filePath));
	}

	/**
	 * 删除指定路径的文件。<br>
	 * 如果该文件不存在则返回0<br>
	 * 如果该文件存在并成功删除则返回1<br>
	 * 其他情况返回-1
	 * @param directoryPath 文件所在的文件夹路径
	 * @param fileName 文件名
	 * @return
	 */
	public static int delete(String directoryPath, String fileName) {
		return delete(new File(directoryPath, fileName));
	}

	/**
	 * 删除指定的文件。<br>
	 * 如果该文件不存在则返回0<br>
	 * 如果该文件存在并成功删除则返回1<br>
	 * 其他情况返回-1
	 * @param file
	 * @return
	 */
	private static int delete(File file) {
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
	 * 通过文件流复制文件到指定路径
	 * @param is
	 * @param target
	 * @return
	 */
	public final static int copy(InputStream is, File target) {
		int result = -1;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = new FileOutputStream(target);
			bos = new BufferedOutputStream(fos);
			int length = 0;
			byte[] buffer = new byte[4096];
			while ((length = is.read(buffer)) != -1) {
				bos.write(buffer, 0, length);
			}
			bos.flush();
		} catch (Exception e) {
			throw new LogicException(e);
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				throw new LogicException(e);
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						throw new LogicException(e);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 通过文件流复制文件到指定路径
	 * @param is
	 * @param target
	 * @return
	 */
	public final static int copy(InputStream is, String filePath) {
		return copy(is, new File(filePath));
	}

}
