package com.func.tailrec;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class HylomorphismTest {
	@Test
	public void testSum() {
		// given
		int n = 8000;
		// when
		Integer actual = sum(n);
		// then
		assertThat(actual).isEqualTo(n * (n + 1) / 2);
	}

	private Integer sum(Integer n) {
		return sum(n, 0).eval();
	}

	private Bouncer<Integer> sum(Integer n, Integer out) {
		return Hylomorphism.hylo(//
				x -> x == 0, //
				x -> x - 1, //
				x -> x, //
				(z, y) -> z + y, //
				out, //
				n //
		);
	}
}
