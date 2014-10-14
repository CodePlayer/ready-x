package me.ready;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.lang3.StringUtils;

/**
 * Unit test for simple App.
 */
public class AppTest
		extends TestCase {

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
		User user = new User(1, "张三", "Helo*47878576568%*……%&*", false);
		StringUtils.trimToEmpty("");
//		System.out.println(JSONUtil.encode(user));
	}
}
