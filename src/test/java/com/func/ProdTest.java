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
}
