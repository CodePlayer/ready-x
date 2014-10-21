package me.ready.util;

import org.junit.Test;

public class ArithTest {

	@Test
	public void testRound() {
		System.out.println(Arith.round(12.65656465, 3));
	}

	@Test
	public void testCeil() {
		System.out.println(Arith.ceil(12.65656465));
		System.out.println(Arith.ceil(154.125656465));
		System.out.println(Arith.ceil(1442.65656465));
		System.out.println(Arith.ceil(-1442.65656465));
		System.out.println(Arith.ceil(123));
	}

	@Test
	public void testArith() {
		System.out.println(new Arith(212.454).add(1245.23).multiply(12.45).doubleValue());
	}

}
