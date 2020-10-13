package me.codeplayer.util;

import org.assertj.core.api.*;
import org.junit.*;

public class RandomUtilTest implements WithAssertions {

	@Test
	public void getString() {
		assertThat(RandomUtil.getIntString(5))
				.hasSize(5)
				.has(new Condition<>(NumberUtil::isNumeric, "must be numeric"));

		assertThat(RandomUtil.getString("abcdefghijklmnopqrstuvwxyz", 6))
				.hasSize(6);
	}
}
