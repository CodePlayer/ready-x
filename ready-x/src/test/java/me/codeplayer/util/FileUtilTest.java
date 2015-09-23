package me.codeplayer.util;

import static org.junit.Assert.assertEquals;

import java.io.File;

import me.codeplayer.util.FileUtil;

import org.junit.Test;

public class FileUtilTest {

	// @Test
	public void copyFile() {
		FileUtil.copyFile("D:\\BaiduYunDownload\\API\\Apache2.2_zh_CN.chm", "E:/notfound/xxxxx/apache-maven-3.2.3-bin-23.zip", true);
	}

	// @Test
	public void getExtension() {
		String path = "D:\\BaiduYunDownload\\API\\Apache2.2_zh_CN.chm";
		String ext = FileUtil.getExtension(path);
		assertEquals(ext, ".chm");
		ext = FileUtil.getExtension(path, true);
		assertEquals(ext, "chm");
		path = "HelloWorld";
		assertEquals(FileUtil.getExtension(path), "");
	}

	// @Test
	public void file() {
		String path = "D:\\BaiduYunDownload\\API\\xxxx";
		System.out.println(new File(path).isDirectory());
	}

	@Test
	public void getFileName() {
		String path = "hhkjhjkhk/xxx\\yyy.name";
		System.out.println(FileUtil.getFileName(path));
	}

	// @Test
	public void moveFile() {
		String path = "D:\\BaiduYunDownload\\Books\\hello.zip";
		FileUtil.moveFileToDirectory(path, "D:/BaiduYunDownload", true);
	}

	@Test
	public void calcFileSize() {
		System.out.println(FileUtil.calcFileSize(10154L, FileUtil.UNIT_AUTO));
	}
}
