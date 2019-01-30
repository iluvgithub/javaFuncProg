package com.func.tailrec;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.junit.Test;

public class ComposedFunctionTest {
	@Test
	public void testCompose() {
		// given
		Function<Integer, Long> f = i -> 0L + i;
		Function<Long, String> g = l -> l.toString();
		Function<Integer, String> h = ComposedFunction.<String>identity().//
				compose(g).compose(f);
		// when
		String act = h.apply(0);
		// then
		assertThat(act).isEqualTo("0");
	}

	@Test
	public void testComposeLoop() {
		// given
		int n = 10000;
		UnaryOperator<Integer> inc = x -> x + 1;
		Function<Integer, Integer> f = ComposedFunction.identity();
		for (int i = 0; i < n; ++i) {
			f = f.compose(inc);
		}

		// when
		Integer act = f.apply(0);
		// then
		assertThat(act).isEqualTo(n);
	}
}
