package com.sandbox.funcprog.recursion;

import static com.sandbox.funcprog.recursion.Bouncer.resume;
import static com.sandbox.funcprog.recursion.Bouncer.suspend;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BouncerTest {

	@Test
	public void testSum0() throws Exception {
		testSum(0);
	}

	@Test
	public void testSum3() throws Exception {
		testSum(3);
	}

	@Test
	public void testSum10000() throws Exception {
		testSum(100000);
	}

	public void testSum(int upTo) throws Exception {
		// arrange
		int n = upTo;
		// act
		Integer actual = sumFromZeroToN(n);
		// assert
		assertEquals(n * (n + 1) / 2, actual.intValue());
	}

	private static Integer sumFromZeroToN(Integer n) {
		return addAllFromZeroToN(n, 0).call();
	}

	private static Bouncer<Integer> addAllFromZeroToN(Integer n, Integer cumul) {
		if (n == 0) {
			return resume(cumul);
		} else {
			return suspend(() -> addAllFromZeroToN(n - 1, cumul + n));
		}
	}

}
