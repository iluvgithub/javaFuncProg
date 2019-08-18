package com.func;

import static com.func.Prod.bracket;
import static com.func.Prod.cross;
import static com.func.Prod.prod;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.Test;

public class ProdTest {

	@Test
	public void testBracket() {
		// given
		Function<String, Integer> f = Integer::parseInt;
		Function<String, Double> g = Double::parseDouble;
		// when
		Prod<Integer, Double> actual = bracket(f, g).apply("1");
		// then
		assertThat(actual.left()).isEqualTo(1);
		assertThat(actual.right()).isEqualTo(1.0);
	}

	@Test
	public void testCross() {
		// given
		Function<Integer, Integer> f = i -> i + 1;
		Function<Double, Double> g = x -> x + 0.5;
		// when
		Prod<Integer, Double> actual = cross(f, g).apply(prod(1, 1.0));
		// then
		assertThat(actual.left()).isEqualTo(2);
		assertThat(actual.right()).isEqualTo(1.5);
	}

	@Test
	public void testLeftMapperAndRightMapper() {
		// given
		Function<Integer, Long> f = i -> i + 1L;
		Prod<Integer, Double> input0 = prod(1, 3.14);
		Prod<Double, Integer> input1 = prod(3.14, 1);
		// when
		Prod<Long, Double> actual0 = Prod.<Integer, Long, Double>leftMapper(f).apply(input0);
		Prod<Double, Long> actual1 = Prod.<Double, Integer, Long>rightMapper(f).apply(input1);
		// then
		assertThat(actual0.left()).isEqualTo(2L);
		assertThat(actual0.right()).isEqualTo(3.14);

		assertThat(actual1.left()).isEqualTo(3.14);
		assertThat(actual1.right()).isEqualTo(2L);
	}
}
