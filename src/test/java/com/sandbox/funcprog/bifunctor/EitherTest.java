package com.sandbox.funcprog.bifunctor;

import static com.sandbox.funcprog.bifunctor.Either.asSwitch;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;

public class EitherTest {

	@Test
	public void testIfThenElse() throws Exception {
		// arrange
		Predicate<Integer> p = n -> n > 0;
		Function<Integer, Either<Integer, Integer>> f = asSwitch(p);
		// act&assert
		Function<Integer, Integer> timesTwo = x -> x * 2;
		assertThat(f.apply(1).apply(timesTwo, null)).isEqualTo(2);
		assertThat(f.apply(-1).apply(null, timesTwo)).isEqualTo(-2);
	}

}
