package me.codeplayer.util;

import org.assertj.core.api.*;
import org.junit.*;

public class RandomXTest implements WithAssertions {

	@Test
	public void getString() {
		assertThat(RandomX.getIntString(5))
				.hasSize(5)
				.has(new Condition<>(NumberX::isNumeric, "must be numeric"));

		assertThat(RandomX.getString("abcdefghijklmnopqrstuvwxyz", 6))
				.hasSize(6);
	}
}