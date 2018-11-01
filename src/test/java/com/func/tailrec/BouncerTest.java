package com.func.tailrec;

import static com.func.tailrec.Bouncer.done;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class BouncerTest {

	@Test
	public void testSum() {
		// given
		int n = 30000;
		// when
		Integer actual = sum(n);
		// then
		assertThat(actual).isEqualTo(n * (n + 1) / 2);
	}

	private Integer sum(Integer n) {
		return sum(n, 0).eval();
	}

	private Bouncer<Integer> sum(Integer n, Integer out) {
		if (n.equals(0)) {
			return done(out);
		} else {
			return () -> sum(n - 1, out + n);
		}
	}

}
