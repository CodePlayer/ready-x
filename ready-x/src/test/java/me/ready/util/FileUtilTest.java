package me.ready.util;

import java.io.File;

import org.junit.Test;
import static org.junit.Assert.*;

public class FileUtilTest {

	//	@Test
	public void testCopyFile() {
		FileUtil.copyFile("D:\\BaiduYunDownload\\API\\Apache2.2_zh_CN.chm", "E:/notfound/xxxxx/apache-maven-3.2.3-bin-23.zip", true);
	}

	//	@Test
	public void testGetExtension() {
		String path = "D:\\BaiduYunDownload\\API\\Apache2.2_zh_CN.chm";
		String ext = FileUtil.getExtension(path);
		assertEquals(ext, ".chm");
		ext = FileUtil.getExtension(path, true);
		assertEquals(ext, "chm");

		path = "HelloWorld";
		assertEquals(FileUtil.getExtension(path), "");
	}

	//	@Test
	public void testFile() {
		String path = "D:\\BaiduYunDownload\\API\\xxxx";
		System.out.println(new File(path).isDirectory());
	}

	@Test
	public void testGetFileName() {
		String path = "hhkjhjkhk/xxx\\yyy.name";
		System.out.println(FileUtil.getFileName(path));
	}

	//	@Test
	public void testMoveFile() {
		String path = "D:\\BaiduYunDownload\\Books\\hello.zip";
		FileUtil.moveFileToDirectory(path, "D:/BaiduYunDownload", true);
	}
}
