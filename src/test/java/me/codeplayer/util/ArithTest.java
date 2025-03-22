package me.codeplayer.util;

import java.math.BigDecimal;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

public class ArithTest implements WithAssertions {

	@Test
	public void round() {
		assertThat(Arith.round(12.65656465, 3)).isEqualTo(12.657);

		assertThat(Arith.round(12.45123, 2)).isEqualTo(12.45);

		assertThat(Arith.round(-123.641, 0)).isEqualTo(-124);
	}

	@Test
	public void ceil() {
		assertThat(Arith.ceilToLong(12.65656465)).isEqualTo(13);
		assertThat(Arith.ceilToLong(154.125656465)).isEqualTo(155);
		assertThat(Arith.ceilToLong(1442.65656465)).isEqualTo(1443);
		assertThat(Arith.ceilToLong(-1442.65656465)).isEqualTo(-1442);
		assertThat(Arith.ceilToLong(123)).isEqualTo(123);
	}

	@Test
	public void floor() {
		assertThat(Arith.floorToLong(12.65656465)).isEqualTo(12);
		assertThat(Arith.floorToLong(154.125656465)).isEqualTo(154);
		assertThat(Arith.floorToLong(1442.65656465)).isEqualTo(1442);
		assertThat(Arith.floorToLong(-1442.65656465)).isEqualTo(-1443);
		assertThat(Arith.floorToLong(123)).isEqualTo(123);
	}

	@Test
	public void arith() {
		assertThat(new Arith(212.454).add(1245.23).multiply(12.45).value)
				.isEqualByComparingTo(BigDecimal.valueOf(18148.1658));
	}
}