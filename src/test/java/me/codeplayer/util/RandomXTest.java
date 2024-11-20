package me.codeplayer.util;

import org.assertj.core.api.Condition;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

public class RandomXTest implements WithAssertions {

	@Test
	public void getString() {
		assertThat(RandomX.getIntString(0)).isEqualTo("");
		for (int i = 1; i <= 30; i++) {
			assertThat(RandomX.getIntString(i))
					.hasSize(i)
					.has(new Condition<>(NumberX::isNumeric, "must be numeric"));
		}

		assertThat(RandomX.getString("abcdefghijklmnopqrstuvwxyz", 6))
				.hasSize(6);
	}

}