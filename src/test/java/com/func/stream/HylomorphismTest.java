package com.func.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;

import com.func.Prod;

public class HylomorphismTest {

	@Test
	public void testFact() {
		// given
		Predicate<Integer> p = i -> 0 == i;
		Long out = 1L;
		Function<Integer, Prod<Long, Integer>> g = i -> Prod.prod(0L + i, i - 1);
		Function<Integer, Long> f = Hylomorphism.hylo(p, //
				g, //
				out, //
				 (u, v) -> u * v //
		);
		// when
		Long actual = f.apply(5);
		// then
		assertThat(actual).isEqualTo(120L);
	}

}
