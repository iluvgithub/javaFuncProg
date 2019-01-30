package com.func;

import static com.func.Curry.curry;
import static com.func.Curry.uncurry;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Test;

public class CurryTest {

	@Test
	public void testCurry() {
		// given
		BiFunction<Integer, Long, String> bif = (i, l) -> i + "." + l;
		// when

		String act = curry(bif).apply(1).apply(2L);
		// then
		assertThat(act).isEqualTo("1.2");
	}

	@Test
	public void testUncurry() {
		// given
		Function<Integer, Function<Long, String>> f = i -> l -> i + "." + l;
		// when

		String act = uncurry(f).apply(1, 2L);
		// then
		assertThat(act).isEqualTo("1.2");
	}

}
