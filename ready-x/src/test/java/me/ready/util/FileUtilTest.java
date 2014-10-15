package me.ready.util;

import org.junit.Test;

public class FileUtilTest {

	@Test
	public void testCopyFile() {
		FileUtil.copyFile("D:\\BaiduYunDownload\\API\\Apache2.2_zh_CN.chm", "E:/notfound/xxxxx/apache-maven-3.2.3-bin-23.zip", true);
	}

}
