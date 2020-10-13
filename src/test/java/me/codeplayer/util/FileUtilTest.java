package me.codeplayer.util;

import static org.junit.Assert.*;

import java.io.*;
import java.math.*;

import org.junit.*;
import org.junit.rules.*;

public class FileUtilTest {

	@Rule
	public TemporaryFolder tempDir = new TemporaryFolder();

	@Test
	public void copyFile() throws IOException {
		// prepare
		final String filename = "hello.txt";
		File file = tempDir.newFolder("test-copyFile")
				.toPath()
				.resolve(filename)
				.toFile();
		final String content = "Hello World";
		FileUtil.writeContent(file, content, false);

		File target = tempDir.newFolder("target-copyFile")
				.toPath().resolve("copyFile.zip").toFile();

		// execute
		FileUtil.copyFile(file, target, true);

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
		File file = tempDir.newFolder("test-moveFile")
				.toPath()
				.resolve(filename)
				.toFile();

		final String content = "Hello World";
		FileUtil.writeContent(file, content, false);
		File dir = tempDir.newFolder("target");
		File target = dir.toPath().resolve(filename).toFile();

		// execute
		FileUtil.moveFileToDirectory(file, dir, true);

		// verify
		assertTrue(target.exists());
		String targetContent = FileUtil.readContent(target);
		assertEquals(content, targetContent);
	}

	@Test
	public void calcFileSize() {
		String fileSize = FileUtil.calcFileSize(10154, FileUtil.UNIT_AUTO);
		assertEquals("9.92KB", fileSize);

		fileSize = FileUtil.calcFileSize(37546126, FileUtil.UNIT_AUTO);
		assertEquals("35.81MB", fileSize);

		fileSize = FileUtil.calcFileSize(37546126, FileUtil.UNIT_AUTO, 2, RoundingMode.FLOOR);
		assertEquals("35.80MB", fileSize);

		fileSize = FileUtil.calcFileSize(1245546512, FileUtil.UNIT_AUTO);
		assertEquals("1.16GB", fileSize);

		fileSize = FileUtil.calcFileSize(37546126, FileUtil.UNIT_KB);
		assertEquals("36666.14KB", fileSize);

		fileSize = FileUtil.calcFileSize(37546126, FileUtil.UNIT_GB);
		assertEquals("0.03GB", fileSize);
	}
}