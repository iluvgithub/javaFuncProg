package com.sandbox.funcprog.bifunctor;

import static com.sandbox.funcprog.bifunctor.Either.asSwitch;
import static org.junit.Assert.assertEquals;

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
		assertEquals(2, f.apply(1).apply(x -> x * 2, null));
		assertEquals(-2, f.apply(-1).apply(null, x -> x * 2));
	}

}
