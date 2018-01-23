package com.sandbox.funcprog.stream;

import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static com.sandbox.funcprog.stream.Anamorphism.from;
import static com.sandbox.funcprog.stream.Anamorphism.fromTo;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;

import com.sandbox.funcprog.bifunctor.Prod;

public class AnamorphismTest {

	@Test
	public void testUnfold() {
		// given
		Predicate<Integer> p = n -> n == 0;
		Function<Integer, Prod<String, Integer>> g = n -> prod(n.toString(), n - 1);
		// when
		ConsList<String> actual = new Anamorphism<>(p, g).unfold(3);
		// then
		assertThat(actual.trace()).isEqualTo("3.2.1");
	}

	@Test
	public void testUnfoldWithState() {
		// given
		Predicate<Integer> p = n -> n == 0;
		Function<Integer, Prod<String, Integer>> g = n -> prod("U" + n, n - 1);
		// when
		Prod<ConsList<String>, Integer> actual = new Anamorphism<>(p, g).unfoldWithState(3);
		Prod<ConsList<String>, Integer> actual0 = new Anamorphism<>(p, g).unfoldWithState(0);
		// then
		assertThat(actual.left().trace()).isEqualTo("U3.U2.U1");
		assertThat(actual.right()).isEqualTo(0);
		assertThat(actual0.left().trace()).isEqualTo("");
		assertThat(actual0.right()).isEqualTo(0);
	}

	@Test
	public void testIterate() {
		// given
		ConsList<Integer> infinite = from(0).takeWhile(n -> n < 4);
		// when
		String actual = infinite.trace();
		// then
		assertThat(actual).isEqualTo("0.1.2.3");
	}

	@Test
	public void testHylomorphism() {
		// given
		int input = 30000;
		Predicate<Integer> p = n -> n == 0;
		Function<Integer, Prod<Integer, Integer>> g = n -> prod(n, n - 1);
		// when
		Integer actual = new Anamorphism<>(p, g).unfold(input).foldLeft(0, (a, b) -> a + b);
		// then
		assertThat(actual).isEqualTo(input * (input + 1) / 2);
	}

	@Test
	public void testMakeIntList() {
		// given
		ConsList<Integer> list = fromTo(0, 3);
		// when
		String actual = list.trace();
		// then
		assertThat(actual).isEqualTo("0.1.2");
	}

}
