package me.ready.util;

import org.junit.Test;
import static org.junit.Assert.*;
public class FileUtilTest {

	@Test
	public void testCopyFile() {
//		FileUtil.copyFile("D:\\BaiduYunDownload\\API\\Apache2.2_zh_CN.chm", "E:/notfound/xxxxx/apache-maven-3.2.3-bin-23.zip", true);
	}

	@Test
	public void testGetExtension() {
		String path = "D:\\BaiduYunDownload\\API\\Apache2.2_zh_CN.chm";
		String ext = FileUtil.getExtension(path);
		assertEquals(ext, ".chm");
		ext = FileUtil.getExtension(path, true);
		assertEquals(ext, "chm");
		
		path = "HelloWorld";
		assertEquals(FileUtil.getExtension(path), "");
	}
}
