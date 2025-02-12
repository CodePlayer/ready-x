package me.codeplayer.util;

import java.io.*;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;

public class FileXTest {

	static final String fileInClasspath = "test-data.json";

	@Rule
	public TemporaryFolder tempDir = new TemporaryFolder();

	@Test
	public void close() throws IOException {
		FileX.close(null, null);
		FileX.close(null, new ByteArrayOutputStream(16));
	}

	@Test
	public void copyFile() throws IOException {
		// prepare
		final String filename = "hello.txt";
		File file = tempDir.newFolder("test-copyFile").toPath().resolve(filename).toFile();

		final String content = "Hello World";
		FileX.writeContent(file, content, false);

		File target = tempDir.newFolder("target-copyFile").toPath().resolve("copyFile.zip").toFile();

		// execute
		FileX.copyFile(file.getPath(), target.getPath(), true);

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
		File file = tempDir.newFolder("test-moveFile").toPath().resolve(filename).toFile();

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
		assertEquals("9.91KB", fileSize);

		fileSize = FileX.calcFileSize(37546126, FileX.UNIT_AUTO);
		assertEquals("35.80MB", fileSize);

		fileSize = FileX.calcFileSize(37546126, FileX.UNIT_AUTO, 2, RoundingMode.FLOOR);
		assertEquals("35.80MB", fileSize);

		fileSize = FileX.calcFileSize(1245546512, FileX.UNIT_AUTO);
		assertEquals("1.16GB", fileSize);

		fileSize = FileX.calcFileSize(37546126, FileX.UNIT_KB);
		assertEquals("36666.13KB", fileSize);

		fileSize = FileX.calcFileSize(37546126, FileX.UNIT_GB);
		assertEquals("0.03GB", fileSize);
	}

	@Test
	public void calcFileSize_FileSizeNegative_ThrowsException() {
		try {
			FileX.calcFileSize(-1, FileX.UNIT_AUTO);
			fail("Expected IllegalArgumentException for negative fileSize");
		} catch (IllegalArgumentException e) {
			assertEquals("fileSize can not less than 0: -1", e.getMessage());
		}
	}

	@Test
	public void calcFileSize_InvalidUnit_ThrowsException() {
		try {
			FileX.calcFileSize(100, 10);
			fail("Expected IllegalArgumentException for invalid unit");
		} catch (IllegalArgumentException e) {
			assertEquals("10", e.getMessage());
		}
	}

	@Test
	public void calcFileSize_AutoUnitSelection_CorrectUnit() {
		assertEquals("100.00Byte", FileX.calcFileSize(100, FileX.UNIT_AUTO));
		assertEquals("1.00KB", FileX.calcFileSize(1024, FileX.UNIT_AUTO));
		assertEquals("1.00MB", FileX.calcFileSize(1024 * 1024, FileX.UNIT_AUTO));
		assertEquals("1.00GB", FileX.calcFileSize(1024 * 1024 * 1024, FileX.UNIT_AUTO));
		assertEquals("1.00TB", FileX.calcFileSize(1024L * 1024 * 1024 * 1024, FileX.UNIT_AUTO));
		assertEquals("1.00PB", FileX.calcFileSize(1024L * 1024 * 1024 * 1024 * 1024, FileX.UNIT_AUTO));
	}

	@Test
	public void calcFileSize_SpecificUnit_CorrectConversion() {
		assertEquals("100.00Byte", FileX.calcFileSize(100, FileX.UNIT_BYTE));
		assertEquals("0.09KB", FileX.calcFileSize(100, FileX.UNIT_KB));
		assertEquals("0.00MB", FileX.calcFileSize(100, FileX.UNIT_MB));
		assertEquals("0.00GB", FileX.calcFileSize(100, FileX.UNIT_GB));
		assertEquals("0.00TB", FileX.calcFileSize(100, FileX.UNIT_TB));
		assertEquals("0.00PB", FileX.calcFileSize(100, FileX.UNIT_PB));
	}

	@Test
	public void calcFileSize_SpecificUnitWithScale_CorrectConversion() {
		assertEquals("0.03MB", FileX.calcFileSize(Short.MAX_VALUE, FileX.UNIT_MB, 2));
		assertEquals("2047.99MB", FileX.calcFileSize(Integer.MAX_VALUE, FileX.UNIT_MB, 2));
		assertEquals("1.99GB", FileX.calcFileSize(Integer.MAX_VALUE, FileX.UNIT_GB, 2));
		assertEquals("1.95TB", FileX.calcFileSize(Integer.MAX_VALUE * 1000L, FileX.UNIT_TB, 2));
		assertEquals("8191.99PB", FileX.calcFileSize(Long.MAX_VALUE, FileX.UNIT_PB, 2));
		assertEquals("7.99EB", FileX.calcFileSize(Long.MAX_VALUE, FileX.UNIT_EB, 2));
		FileX.setFileSizeRoundingMode(RoundingMode.HALF_UP);
		assertEquals("8.00EB", FileX.calcFileSize(Long.MAX_VALUE, FileX.UNIT_EB, 2));
		assertEquals("8.00", FileX.divide(Long.MAX_VALUE, 1024L * 1024 * 1024 * 1024 * 1024 * 1024, 2));
		FileX.setFileSizeRoundingMode(RoundingMode.FLOOR);
	}

	static void runAfterBackup(String fileInClasspath, Consumer<File> task) throws IOException {
		File file = FileX.parseClassPathFile(fileInClasspath);
		File copy = new File(file.getParent(), file.getName() + ".bak");
		FileX.copyFile(file, copy);
		task.accept(file);
		FileX.moveFile(copy, file, true);
	}

	@Test
	public void deleteFile() throws IOException {
		assertEquals(0, FileX.deleteFile("/not/found"));

		runAfterBackup(fileInClasspath, file -> {
			final String data = "Hello World 中文";
			try {
				FileX.writeContent(file, data, false);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
			try {
				assertEquals(data, FileX.readContent(file));
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
			assertEquals(1, FileX.deleteFile(file.getPath()));
		});
		// C:\Users\$USER\AppData\Local\Temp\FileX-test8988523009901350283.tmp
		Path path1 = Files.createTempFile("FileX-", null);

		File f1 = path1.toFile();
		FileX.writeContent(f1, "Hello World".getBytes(StandardCharsets.UTF_8), false);
		assertEquals(11, f1.length());
		FileX.writeContent(f1, "+123".getBytes(StandardCharsets.UTF_8), true);
		assertEquals(15, f1.length());

		Path path2 = Files.createTempFile("FileX-", ".txt");
		File f2 = path2.toFile();
		FileX.copyFile(f1.getPath(), f2.getPath(), true);
		assertEquals("Hello World+123", FileX.readContent(f2.getAbsolutePath(), false));

		assertEquals(1, FileX.deleteFile(f1.getParent(), f1.getName()));
	}

	@Test
	public void readProperties() throws IOException {
		final String data = "# 自动选择\n"
				+ "test.name=Ready-X\n"
				+ "test.chinese=中文\n"
				+ "test.number=123\n"
				+ "test.empty=\n"
				+ "test.blank=abc\n"
				+ "\n"
				+ "test.code=1024\n"
				+ "test.version=4.0.0";
		final String classpath = FileX.getFile(fileInClasspath, true).getParent();
		String name = "test-data.properties";
		File target = new File(classpath, name);
		FileX.writeContent(target, new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)), false);
		Map<String, String> map = FileX.readProperties(name, true);
		assertEquals(7, map.size());
		assertEquals("Ready-X", map.get("test.name"));
		assertEquals("中文", map.get("test.chinese"));
		assertEquals("4.0.0", map.get("test.version"));
	}

	@Test
	public void copyFile_InputStreamToFile_Success() throws IOException {
		// 准备
		final String filename = "hello.txt";
		File file = tempDir.newFolder("test-copyFile").toPath().resolve(filename).toFile();

		final String content = "Hello World";
		FileX.writeContent(file, content, false);

		File target = tempDir.newFolder("target-copyFile").toPath().resolve("copyFile.txt").toFile();

		try (InputStream is = Files.newInputStream(file.toPath())) {
			// 执行
			FileX.copyFile(is, target, true);

			// 验证
			assertTrue(target.exists());
			String targetContent = FileX.readContent(target);
			assertEquals(content, targetContent);
		}
	}

	@Test
	public void copyFile_TargetFileExistsAndWritable_OverrideTrue_Success() throws IOException {
		// 准备
		final String filename = "hello.txt";
		File file = tempDir.newFolder("test-copyFile").toPath().resolve(filename).toFile();

		final String content = "Hello World";
		FileX.writeContent(file, content, false);

		File target = tempDir.newFolder("target-copyFile").toPath().resolve("copyFile.txt").toFile();
		FileX.writeContent(target, "Old Content", false);

		try (InputStream is = Files.newInputStream(file.toPath())) {
			// 执行
			FileX.copyFile(is, target, true);

			// 验证
			assertTrue(target.exists());
			String targetContent = FileX.readContent(target);
			assertEquals(content, targetContent);
		}
	}

	@Test(expected = FileAlreadyExistsException.class)
	public void copyFile_TargetFileExistsAndWritable_OverrideFalse_ThrowsException() throws IOException {
		// 准备
		final String filename = "hello.txt";
		File file = tempDir.newFolder("test-copyFile").toPath().resolve(filename).toFile();

		final String content = "Hello World";
		FileX.writeContent(file, content, false);

		File target = tempDir.newFolder("target-copyFile").toPath().resolve("copyFile.txt").toFile();
		FileX.writeContent(target, "Old Content", false);

		try (InputStream is = Files.newInputStream(file.toPath())) {
			// 执行
			FileX.copyFile(is, target, false);
		}
	}

	@Test(expected = AccessDeniedException.class)
	public void copyFile_TargetIsDirectory_ThrowsException() throws IOException {
		// 准备
		final String filename = "hello.txt";
		File file = tempDir.newFolder("test-copyFile").toPath().resolve(filename).toFile();

		final String content = "Hello World";
		FileX.writeContent(file, content, false);

		File target = tempDir.newFolder("target-copyFile");

		try (InputStream is = Files.newInputStream(file.toPath())) {
			// 执行
			FileX.copyFile(is, target, true);
		}

		// 准备
		File source = tempDir.newFile("source.txt");
		target = tempDir.newFolder("targetDir");

		// 执行并验证
		try {
			FileX.copyFile(source.getPath(), target.getPath());
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("File is a directory:" + target, e.getMessage());
		}
	}

	@Test(expected = UncheckedIOException.class)
	public void copyFile_IOExceptionDuringWrite_ThrowsUncheckedIOException() throws IOException {
		// 准备
		final String filename = "hello.txt";
		File file = tempDir.newFolder("test-copyFile").toPath().resolve(filename).toFile();

		final String content = "Hello World";
		FileX.writeContent(file, content, false);

		File target = tempDir.newFolder("target-copyFile").toPath().resolve("copyFile.txt").toFile();

		// 模拟 IOException
		try (InputStream is = new InputStream() {
			@Override
			public int read() throws IOException {
				throw new IOException("Simulated IOException");
			}
		}) {
			// 执行
			FileX.copyFile(is, target, true);
		}
	}

	@Test
	public void copyFile_FileExistsAndWritable_FileCopied() throws IOException {
		// 准备
		File folder = tempDir.newFolder("file-exists");
		File source = tempDir.newFile("file-exists/source.txt");
		Files.write(source.toPath(), "Hello World".getBytes());

		File target = new File(folder, "target.txt");
		// 执行
		FileX.copyFile(source.getPath(), target.getPath());

		// 验证
		assertTrue(target.exists());
		assertEquals("Hello World", new String(Files.readAllBytes(target.toPath())));
	}

	@Test
	public void copyFile_FileExistsAndNotWritable_ThrowsException() throws IOException {
		// 准备
		File source = tempDir.newFile("source.txt");
		File target = tempDir.newFile("target.txt");
		target.setWritable(false);
		Files.write(source.toPath(), "Hello World".getBytes());

		// 执行并验证
		assertThrows(AccessDeniedException.class, () -> FileX.copyFile(source.getPath(), target.getPath()));
	}

	@Test
	public void copyFile_FileDoesNotExist_ThrowsException() throws IOException {
		// 准备
		File source = new File(tempDir.getRoot(), "nonexistent.txt");
		File target = tempDir.newFile("target.txt");

		// 执行并验证
		assertThrows(NoSuchFileException.class, () -> FileX.copyFile(source.getPath(), target.getPath()));
	}

	@Test(expected = AccessDeniedException.class)
	public void copyFile_SourceIsDirectory_ThrowsException() throws IOException {
		// 准备
		File source = tempDir.newFolder("sourceDir");
		File target = tempDir.newFile("target.txt");

		// 执行并验证
		FileX.copyFile(source.getPath(), target.getPath());
	}

	@Test
	public void parseClassPathFile() {
		assertNotNull(FileX.parseFile("classpath:" + fileInClasspath, true));
		assertNotNull(FileX.parseFile("/usr/local", false));

		assertThrows(NullPointerException.class, () -> FileX.parseFile(null, false));
		assertThrows(IllegalArgumentException.class, () -> FileX.parseFile("/$notFound/usr/local", true));

		assertNull(FileX.parseClassPathFile("/$notFound/usr/local"));
	}

	@Test(expected = NoSuchFileException.class)
	public void moveFileToDirectory_FileDoesNotExist_ThrowsException() throws IOException {
		File source = new File("nonexistentfile.txt");
		File destDir = tempDir.newFolder("dest");
		FileX.moveFileToDirectory(source, destDir);
	}

	@Test(expected = IOException.class)
	public void moveFileToDirectory_SourceIsDirectory_ThrowsException() throws IOException {
		File source = tempDir.newFolder("source");
		File destDir = tempDir.newFolder("dest");
		FileX.moveFileToDirectory(source.getPath(), destDir.getPath());
	}

	@Test
	public void moveFileToDirectory_FileNotReadable_ThrowsException() throws IOException {
		File source = tempDir.newFile("source.txt");
		File destDir = tempDir.newFolder("dest");
		FileX.moveFileToDirectory(source, destDir);
	}

	@Test(expected = FileAlreadyExistsException.class)
	public void moveFileToDirectory_TargetFileExistsAndOverrideFalse_ThrowsException() throws IOException {
		File source = tempDir.newFile("source.txt");
		File destDir = tempDir.newFolder("dest");
		File destFile = new File(destDir, source.getName());
		destFile.createNewFile();
		FileX.moveFileToDirectory(source, destDir);
	}

	@Test(expected = AccessDeniedException.class)
	public void moveFileToDirectory_TargetIsDirectory_ThrowsException() throws IOException {
		File source = tempDir.newFile("source.txt");
		File destDir = tempDir.newFolder("dest");
		File destFile = new File(destDir, source.getName());
		destFile.mkdir();
		FileX.moveFileToDirectory(source, destDir, true);
	}

	@Test(expected = AccessDeniedException.class)
	public void moveFileToDirectory_TargetFileNotWritable_ThrowsException() throws IOException {
		File source = tempDir.newFile("source.txt");
		File destDir = tempDir.newFolder("dest");
		File destFile = new File(destDir, source.getName());
		destFile.createNewFile();
		destFile.setWritable(false);
		FileX.moveFileToDirectory(source, destDir, true);
	}

	@Test
	public void moveFileToDirectory_CannotCreateDestDirectory_ThrowsException() throws IOException {
		File source = tempDir.newFile("source.txt");
		File destDir = new File(tempDir.getRoot(), "nonexistent/dest");
		destDir.getParentFile().setWritable(false);
		FileX.moveFileToDirectory(source, destDir, true);
	}

	@Test
	public void moveFileToDirectory_FileMovedSuccessfully() throws IOException {
		File source = tempDir.newFile("source.txt");
		File destDir = tempDir.newFolder("dest");
		File destFile = new File(destDir, source.getName());
		FileX.moveFileToDirectory(source.getPath(), destDir.getPath(), false);
		assertTrue(destFile.exists());
		assertFalse(source.exists());
	}

	@Test(expected = AccessDeniedException.class)
	public void moveFileToDirectory_FileRenamedFailedAndOverrideTrue_FileCopiedAndDeleted() throws IOException {
		File source = tempDir.newFile("source.txt");
		File destDir = tempDir.newFolder("dest");
		File destFile = new File(destDir, source.getName());
		destFile.createNewFile();
		destFile.setWritable(false);
		FileX.moveFileToDirectory(source, destDir, true);
		assertTrue(destFile.exists());
		assertFalse(source.exists());
	}

	@Test
	public void copyFileToDirectory_DirectoryDoesNotExist_ShouldCreateAndCopy() throws IOException {
		File sourceFile = tempDir.newFile("source.txt");
		File destDir = new File(tempDir.getRoot(), "newDir");
		File destFile = new File(destDir, sourceFile.getName());

		FileX.copyFileToDirectory(sourceFile, destDir);

		assertTrue(destFile.exists());

		FileX.moveFileToDirectoryWithRandomFileName(sourceFile, destDir.getPath(), ".txt");
	}

	@Test
	public void copyFileToDirectory_DirectoryExistsAndWritable_ShouldCopy() throws IOException {
		File sourceFile = tempDir.newFile("source.txt");
		File destDir = tempDir.newFolder("existingDir");
		File destFile = new File(destDir, sourceFile.getName());

		FileX.copyFileToDirectory(sourceFile.getPath(), destDir.getPath());

		assertTrue(destFile.exists());
		FileX.copyFileToDirectoryWithRandomFileName(sourceFile, destDir.getPath());
		FileX.moveFileToDirectoryWithRandomFileName(sourceFile, destDir.getPath());
		assertFalse(sourceFile.exists());
	}

	@Test(expected = NotDirectoryException.class)
	public void copyFileToDirectory_DestinationIsFile_ShouldThrowException() throws IOException {
		File sourceFile = tempDir.newFile("source.txt");
		File destFile = tempDir.newFile("existingFile.txt");

		FileX.copyFileToDirectory(sourceFile, destFile);
	}

	@Test(expected = FileAlreadyExistsException.class)
	public void copyFileToDirectory_FileExistsAndOverrideFalse_ShouldThrowException() throws IOException {
		File sourceFile = tempDir.newFile("source.txt");
		File destDir = tempDir.newFolder("existingDir");
		File destFile = new File(destDir, sourceFile.getName());
		destFile.createNewFile();

		FileX.copyFileToDirectory(sourceFile, destDir);
	}

	@Test
	public void copyFileToDirectory_FileExistsAndOverrideTrue_ShouldCopyAndOverride() throws IOException {
		File sourceFile = tempDir.newFile("source.txt");
		File destDir = tempDir.newFolder("existingDir");
		File destFile = new File(destDir, sourceFile.getName());
		destFile.createNewFile();

		FileX.copyFileToDirectory(sourceFile, destDir, true);

		assertTrue(destFile.exists());
		FileX.copyFileToDirectoryWithRandomFileName(sourceFile, destDir.getPath(), ".txt");
	}

	@Test(expected = NoSuchFileException.class)
	public void moveFile_SourceFileDoesNotExist_ThrowsException() throws IOException {
		File src = tempDir.newFile("source.txt");
		File dest = tempDir.newFile("destination.txt");
		src.delete(); // 删除源文件以模拟不存在的情况
		FileX.moveFile(src, dest, false);
	}

	@Test(expected = AccessDeniedException.class)
	public void moveFile_SourceFileIsDirectory_ThrowsException() throws IOException {
		File src = tempDir.newFolder("sourceDir");
		File dest = tempDir.newFile("destination.txt");
		FileX.moveFile(src, dest);
	}

	@Test
	public void moveFile_SourceFileNotReadable_ThrowsException() throws IOException {
		File src = tempDir.newFile("source.txt");
		File dest = new File(src.getParent(), "destination.txt");
		FileX.moveFile(src.getPath(), dest.getPath(), false);
	}

	@Test(expected = FileAlreadyExistsException.class)
	public void moveFile_DestinationExistsAndNotOverridable_ThrowsException() throws IOException {
		File src = tempDir.newFile("source.txt");
		File dest = tempDir.newFile("destination.txt");
		FileX.moveFile(src.getPath(), dest.getPath());
	}

	@Test
	public void moveFile_DestinationExistsAndOverridable_FileMoved() throws IOException {
		File src = tempDir.newFile("source.txt");
		File dest = tempDir.newFile("destination.txt");
		FileX.moveFile(src, dest, true);
		assertTrue(dest.exists());
		assertFalse(src.exists());
	}

	@Test
	public void moveFile_DestinationDoesNotExist_FileMoved() throws IOException {
		File src = tempDir.newFile("source.txt");
		File dest = new File(tempDir.getRoot(), "destination.txt");
		FileX.moveFile(src, dest);
		assertTrue(dest.exists());
		assertFalse(src.exists());
	}

	@Test(expected = AccessDeniedException.class)
	public void moveFile_DestIsDirectory_ThrowsException() throws IOException {
		File src = tempDir.newFile("source.txt");
		File dest = tempDir.newFolder("destinationDir");
		FileX.moveFile(src, dest, true);
		assertTrue(dest.exists());
		assertFalse(src.exists());
	}

	@Test(expected = AccessDeniedException.class)
	public void moveFile_MoveFailsAndNotOverridable_ThrowsException() throws IOException {
		File src = tempDir.newFile("source.txt");
		File dest = tempDir.newFile("destination.txt");
		dest.setWritable(false); // 设置目标文件不可写以模拟移动失败
		FileX.moveFile(src, dest, true);
	}

}