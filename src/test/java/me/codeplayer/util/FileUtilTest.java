package me.codeplayer.util;

import java.io.*;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

public class FileUtilTest {

	@TempDir
	Path tempDir;

	Path subdir(String dirname) throws IOException {
		Path subdir = tempDir.resolve(dirname);
		Files.createDirectory(subdir);
		return subdir;
	}

	static File createFile(Path dir, String filename) throws IOException {
		File file = dir.resolve(filename).toFile();
		file.createNewFile();
		return file;
	}

	@Test
	public void close() throws IOException {
		FileUtil.close(null, null);
		FileUtil.close(null, new ByteArrayOutputStream(16));
	}

	@Test
	public void copyFile() throws IOException {
		// prepare
		final String filename = "hello.txt";
		File file = subdir("test-copyFile").resolve(filename).toFile();

		final String content = "Hello World";
		FileUtil.writeContent(file, content, false);

		File target = subdir("target-copyFile").resolve("copyFile.zip").toFile();

		// execute
		FileUtil.copyFile(file.getPath(), target.getPath(), true);

		// verify
		assertTrue(target.exists());
		String targetContent = FileUtil.readContent(target);
		assertEquals(content, targetContent);
	}

	@Test
	public void getExtension() {
		String path = "D:\\BaiduYunDownload\\API\\Apache2.2_zh_CN.chm";
		String ext = FileUtil.getExtension(path);
		assertEquals(".chm", ext);

		ext = FileUtil.getExtension(path, true);
		assertEquals("chm", ext);

		path = "HelloWorld";
		assertEquals("", FileUtil.getExtension(path));

		path = "hello.world/console";
		assertEquals("", FileUtil.getExtension(path));
	}

	@Test
	public void getFileName() {
		String path = "hhkjhjkhk/xxx\\yyy.name";
		assertEquals("yyy.name", FileUtil.getFileName(path));

		path = "hhkjhjkhk/xxx\\yyy";
		assertEquals("yyy", FileUtil.getFileName(path));

		path = "hhkjhjkhk/xx_x/abc";
		assertEquals("abc", FileUtil.getFileName(path));
	}

	@Test
	public void moveFile() throws IOException {
		// prepare
		final String filename = "hello.txt";
		File file = subdir("test-moveFile").resolve(filename).toFile();

		final String content = "Hello World";
		FileUtil.writeContent(file, content, false);
		Path dir = subdir("target");
		File target = dir.resolve(filename).toFile();

		// execute
		FileUtil.moveFileToDirectory(file, dir.toFile(), true);

		// verify
		assertTrue(target.exists());
		String targetContent = FileUtil.readContent(target);
		assertEquals(content, targetContent);
	}

	@Test
	public void calcFileSize() {
		String fileSize = FileUtil.calcFileSize(10154, FileUtil.UNIT_AUTO);
		assertEquals("9.91KB", fileSize);

		fileSize = FileUtil.calcFileSize(37546126, FileUtil.UNIT_AUTO);
		assertEquals("35.80MB", fileSize);

		fileSize = FileUtil.calcFileSize(37546126, FileUtil.UNIT_AUTO, 2, RoundingMode.FLOOR);
		assertEquals("35.80MB", fileSize);

		fileSize = FileUtil.calcFileSize(1245546512, FileUtil.UNIT_AUTO);
		assertEquals("1.16GB", fileSize);

		fileSize = FileUtil.calcFileSize(37546126, FileUtil.UNIT_KB);
		assertEquals("36666.13KB", fileSize);

		fileSize = FileUtil.calcFileSize(37546126, FileUtil.UNIT_GB);
		assertEquals("0.03GB", fileSize);
	}

	@Test
	public void calcFileSize_FileSizeNegative_ThrowsException() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
				FileUtil.calcFileSize(-1, FileUtil.UNIT_AUTO));
		assertEquals("fileSize can not less than 0: -1", e.getMessage());
	}

	@Test
	public void calcFileSize_InvalidUnit_ThrowsException() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> FileUtil.calcFileSize(100, 10));
		assertEquals("10", e.getMessage());
	}

	@Test
	public void calcFileSize_AutoUnitSelection_CorrectUnit() {
		assertEquals("100.00Byte", FileUtil.calcFileSize(100, FileUtil.UNIT_AUTO));
		assertEquals("1.00KB", FileUtil.calcFileSize(1024, FileUtil.UNIT_AUTO));
		assertEquals("1.00MB", FileUtil.calcFileSize(1024 * 1024, FileUtil.UNIT_AUTO));
		assertEquals("1.00GB", FileUtil.calcFileSize(1024 * 1024 * 1024, FileUtil.UNIT_AUTO));
		assertEquals("1.00TB", FileUtil.calcFileSize(1024L * 1024 * 1024 * 1024, FileUtil.UNIT_AUTO));
		assertEquals("1.00PB", FileUtil.calcFileSize(1024L * 1024 * 1024 * 1024 * 1024, FileUtil.UNIT_AUTO));
	}

	@Test
	public void calcFileSize_SpecificUnit_CorrectConversion() {
		assertEquals("100.00Byte", FileUtil.calcFileSize(100, FileUtil.UNIT_BYTE));
		assertEquals("0.09KB", FileUtil.calcFileSize(100, FileUtil.UNIT_KB));
		assertEquals("0.00MB", FileUtil.calcFileSize(100, FileUtil.UNIT_MB));
		assertEquals("0.00GB", FileUtil.calcFileSize(100, FileUtil.UNIT_GB));
		assertEquals("0.00TB", FileUtil.calcFileSize(100, FileUtil.UNIT_TB));
		assertEquals("0.00PB", FileUtil.calcFileSize(100, FileUtil.UNIT_PB));
	}

	@Test
	public void calcFileSize_SpecificUnitWithScale_CorrectConversion() {
		assertEquals("0.03MB", FileUtil.calcFileSize(Short.MAX_VALUE, FileUtil.UNIT_MB, 2));
		assertEquals("2047.99MB", FileUtil.calcFileSize(Integer.MAX_VALUE, FileUtil.UNIT_MB, 2));
		assertEquals("1.99GB", FileUtil.calcFileSize(Integer.MAX_VALUE, FileUtil.UNIT_GB, 2));
		assertEquals("1.95TB", FileUtil.calcFileSize(Integer.MAX_VALUE * 1000L, FileUtil.UNIT_TB, 2));
		assertEquals("8191.99PB", FileUtil.calcFileSize(Long.MAX_VALUE, FileUtil.UNIT_PB, 2));
		assertEquals("7.99EB", FileUtil.calcFileSize(Long.MAX_VALUE, FileUtil.UNIT_EB, 2));
		FileUtil.setFileSizeRoundingMode(RoundingMode.HALF_UP);
		assertEquals("8.00EB", FileUtil.calcFileSize(Long.MAX_VALUE, FileUtil.UNIT_EB, 2));
		assertEquals("8.00", FileUtil.divide(Long.MAX_VALUE, 1024L * 1024 * 1024 * 1024 * 1024 * 1024, 2));
		FileUtil.setFileSizeRoundingMode(RoundingMode.FLOOR);
	}

	static void runAfterBackup(String fileInClasspath, Consumer<File> task) throws IOException {
		File file = FileUtil.parseClassPathFile(fileInClasspath);
		File copy = new File(file.getParent(), file.getName() + ".bak");
		FileUtil.copyFile(file, copy);
		task.accept(file);
		FileUtil.moveFile(copy, file, true);
	}

}