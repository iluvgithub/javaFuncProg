package com.sandbox.funcprog.recursion;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TailRecursionTest {

	@Test
	public void testSum0() throws Exception {
		testSum(0);
	}

	@Test
	public void testSum10000() throws Exception {
		testSum(100000);
	}

	public void testSum(int upTo) throws Exception {
		// arrange
		int n = 10000;
		// act
		TailRecursion<Integer> addAll = addAllFromZeroToN(n, 0);
		// assert
		assertEquals(n * (n + 1) / 2, addAll.call().intValue());
	}

	private static TailRecursion<Integer> addAllFromZeroToN(int n, int cumul) {
		if (n == 0) {
			return TailRecursion.finalCall(cumul);
		} else {
			return TailRecursion.suspendCall(() -> addAllFromZeroToN(n - 1, cumul + n));
		}
	}

}
