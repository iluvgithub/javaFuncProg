package com.sandbox.funcprog.tailrecursion;

import static com.sandbox.funcprog.tailrecursion.Bouncer.resume;
import static com.sandbox.funcprog.tailrecursion.Bouncer.suspend;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class BouncerTest {

	@Test
	public void testSum() {
		// given
		Integer input = 30000;
		// when
		Integer actual = sum(0, input).call();
		// then
		assertThat(actual).isEqualTo(input * (input + 1) / 2);
	}

	public static Bouncer<Integer> sum(Integer acc, Integer n) {
		if (n <= 0) {
			return resume(acc);
		} else {
			return suspend(() -> sum(acc + n, n - 1));
		}
	}

}
