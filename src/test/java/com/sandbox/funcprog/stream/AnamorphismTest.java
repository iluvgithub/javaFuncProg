package com.sandbox.funcprog.stream;

import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static com.sandbox.funcprog.stream.Anamorphism.unfold;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;

import com.sandbox.funcprog.bifunctor.Prod;

public class AnamorphismTest {

	@Test
	public void testGenerate() {

		// given
		Predicate<Integer> p = n -> n == 0;
		Function<Integer, Prod<String, Integer>> g = n -> prod(n.toString(), n - 1);
		// when
		ConsList<String> actual = unfold(p, g).apply(3);
		// then
		assertThat(actual.trace()).isEqualTo("3.2.1");
	}

	@Test
	public void testHylomorphism() {
		// given
		int input = 30000;
		Predicate<Integer> p = n -> n == 0;
		Function<Integer, Prod<Integer, Integer>> g = n -> prod(n, n - 1);
		// when
		Integer actual = unfold(p, g).apply(input).foldLeft(0, (a, b) -> a + b);
		// then
		assertThat(actual).isEqualTo(input * (input + 1) / 2);
	}

}
