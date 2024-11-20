package me.codeplayer.util;

import static org.junit.Assert.*;

import java.io.*;
import java.math.*;

import org.junit.*;
import org.junit.rules.*;

public class FileXTest {

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
		FileX.writeContent(file, content, false);

		File target = tempDir.newFolder("target-copyFile")
				.toPath().resolve("copyFile.zip").toFile();

		// execute
		FileX.copyFile(file, target, true);

		// verify
		assertTrue(target.exists());
		String targetContent = FileX.readContent(target);
		assertEquals(content, targetContent);
	}

	@Test
	public void getExtension() {
		String path = "D:\\BaiduYunDownload\\API\\Apache2.2_zh_CN.chm";
		String ext = FileX.getExtension(path);
		assertEquals(".chm", ext);

		ext = FileX.getExtension(path, true);
		assertEquals("chm", ext);

		path = "HelloWorld";
		assertEquals("", FileX.getExtension(path));

		path = "hello.world/console";
		assertEquals("", FileX.getExtension(path));
	}

	@Test
	public void getFileName() {
		String path = "hhkjhjkhk/xxx\\yyy.name";
		assertEquals("yyy.name", FileX.getFileName(path));

		path = "hhkjhjkhk/xxx\\yyy";
		assertEquals("yyy", FileX.getFileName(path));

		path = "hhkjhjkhk/xx_x/abc";
		assertEquals("abc", FileX.getFileName(path));
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
		FileX.writeContent(file, content, false);
		File dir = tempDir.newFolder("target");
		File target = dir.toPath().resolve(filename).toFile();

		// execute
		FileX.moveFileToDirectory(file, dir, true);

		// verify
		assertTrue(target.exists());
		String targetContent = FileX.readContent(target);
		assertEquals(content, targetContent);
	}

	@Test
	public void calcFileSize() {
		String fileSize = FileX.calcFileSize(10154, FileX.UNIT_AUTO);
		assertEquals("9.92KB", fileSize);

		fileSize = FileX.calcFileSize(37546126, FileX.UNIT_AUTO);
		assertEquals("35.81MB", fileSize);

		fileSize = FileX.calcFileSize(37546126, FileX.UNIT_AUTO, 2, RoundingMode.FLOOR);
		assertEquals("35.80MB", fileSize);

		fileSize = FileX.calcFileSize(1245546512, FileX.UNIT_AUTO);
		assertEquals("1.16GB", fileSize);

		fileSize = FileX.calcFileSize(37546126, FileX.UNIT_KB);
		assertEquals("36666.14KB", fileSize);

		fileSize = FileX.calcFileSize(37546126, FileX.UNIT_GB);
		assertEquals("0.03GB", fileSize);
	}
}