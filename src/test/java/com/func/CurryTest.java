package com.func;

import static com.func.Curry.curry;
import static com.func.Curry.uncurry;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

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

	@Test

	public void testOptionalMapperUnitary() {
		// arrange
		Function<Integer, Integer> inc = x -> x + 1;
		Function<Integer, String> mapper = inc.andThen(Object::toString);
		Function<Optional<Integer>, Optional<String>> f = Curry.optionalMapper(mapper);
		// act
		Optional<String> actual = f.apply(Optional.of(3));
		Optional<String> actual0 = f.apply(Optional.empty());
		// assert
		assertThat(actual.get()).isEqualTo("4");
		assertThat(actual0.isPresent()).isFalse();
	}

	@Test
	public void testOptionalFlatMapperUnitary() {
		// arrange
		Function<Integer, Integer> inc = x -> x + 1;
		Function<Integer, Optional<Integer>> mapper = x -> Optional.of(x).map(inc);
		UnaryOperator<Optional<Integer>> unary = Curry.optionalFlatMapperUnitary(mapper);
		// act
		Optional<Integer> actual = unary.apply(Optional.of(3));
		Optional<Integer> actual0 = unary.apply(Optional.empty());
		// assert
		assertThat(actual.get()).isEqualTo(3 + 1);
		assertThat(actual0.isPresent()).isFalse();
	}
}
