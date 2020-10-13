package me.codeplayer.util;

import org.assertj.core.api.*;
import org.junit.*;

import me.codeplayer.util.ChineseNumber.*;

public class ChineseNumberTest implements WithAssertions {

	@Test
	public void formatNumber() {
		assertThat(ChineseNumber.formatNumber(1234567890, FormatStyle.UPPER_CASE))
				.isEqualTo("壹拾贰亿叁仟肆佰伍拾陆万柒仟捌佰玖拾");

		assertThat(ChineseNumber.formatNumber(1231200, FormatStyle.UPPER_CASE))
				.isEqualTo("壹佰贰拾叁万壹仟贰佰");

		assertThat(ChineseNumber.formatNumber(1231200, FormatStyle.LOWER_CASE))
				.isEqualTo("一百二十三万一千二百");

		assertThat(ChineseNumber.formatNumber(1231200, FormatStyle.MONEY))
				.isEqualTo("壹佰贰拾叁万壹仟贰佰元整");

		assertThat(ChineseNumber.formatNumber(100200.23, false, FormatStyle.MONEY))
				.isEqualTo("壹拾万零贰佰元贰角叁分");

		assertThat(ChineseNumber.formatNumber(100200.4567, false, FormatStyle.MONEY))
				.isEqualTo("壹拾万零贰佰元肆角伍分");

		assertThat(ChineseNumber.formatNumber(100200.4567, true, FormatStyle.MONEY))
				.isEqualTo("壹拾万零贰佰元整");

		assertThat(ChineseNumber.formatNumber(100200.01, false, FormatStyle.MONEY))
				.isEqualTo("壹拾万零贰佰元零壹分");
	}
}