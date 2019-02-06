package com.func.list;

import static com.func.Prod.prod;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.junit.Test;

import com.func.Prod;

public class AnamorphismTest {

	@Test
	public void testUnfold() {
		// arrange
		Predicate<Integer> p = n -> n == 0;
		Function<Integer, String> g = n -> "" + n;
		Function<Integer, Integer> s = n -> n - 1;
		Function<Integer, List<String>> f = Anamorphism.unfold(p, s, g);
		// act
		List<String> actual = f.apply(3);
		// assert
		assertThat(actual.head().get()).isEqualTo("3");
		assertThat(actual.tail().get().head().get()).isEqualTo("2");
		assertThat(actual.tail().get().tail().get().head().get()).isEqualTo("1");
		assertThat(actual.tail().get().tail().get().tail().get().head()).isEmpty();
	}

	@Test
	public void testIterate() {
		// arrange
		UnaryOperator<Integer> u = n -> 1 + n;
		Function<Integer, List<Integer>> infinite = Anamorphism.iterate(u);
		// act
		List<Integer> actual = infinite.apply(0);
		// assert
		assertThat(actual.head().get()).isEqualTo(0);
		assertThat(actual.tail().get().head().get()).isEqualTo(1);
		assertThat(actual.tail().get().tail().get().head().get()).isEqualTo(2);
	}

	@Test
	public void testMapper() {
		// arrange
		Predicate<Integer> p = n -> n == 0;
		Function<Integer, Prod<Integer, Integer>> g = n -> prod(n, n - 1);
		List<Integer> list = Anamorphism.unfold(p, g).apply(2);
		Function<Integer, String> map = i -> "" + i;
		// act
		List<String> actual = Anamorphism.mapper(map).apply(list);
		// assert
		assertThat(actual.head().get()).isEqualTo("2");
		assertThat(actual.tail().get().head().get()).isEqualTo("1");
		assertThat(actual.tail().get().tail().get().head()).isEmpty();
	}

	@Test

	public void testOptionalIterate() {
		// arrange
		Function<Integer, Optional<Integer>> u = n -> Optional.of(n - 1).filter(x -> x >= 0);
		Function<Integer, List<Integer>> infinite = Anamorphism.iterate(u);
		// act
		List<Integer> actual0 = infinite.apply(0);
		List<Integer> actual = infinite.apply(3);
		// assert
		assertThat(actual0.head().get()).isEqualTo(0);
		assertThat(actual0.tail().get().head()).isEmpty();
		assertThat(actual.head().get()).isEqualTo(3);
		assertThat(actual.tail().get().head().get()).isEqualTo(2);
		assertThat(actual.tail().get().tail().get().head().get()).isEqualTo(1);
		assertThat(actual.tail().get().tail().get().tail().get().head().get()).isEqualTo(0);
	}

	@Test

	public void testStringToList() {
		// arrange
		String s = "abc";
		// act
		List<Character> actuals = Anamorphism.stringToList().apply(s);
		// assert
		assertThat(actuals.head().get()).isEqualTo('a');
		assertThat(actuals.tail().get().head().get()).isEqualTo('b');
		assertThat(actuals.tail().get().tail().get().head().get()).isEqualTo('c');
		assertThat(actuals.tail().get().tail().get().tail().get().head()).isEmpty();
	}

	@Test
	public void testList() {
		// arrange
		Integer[] is = new Integer[] { 1, 2, 3 };
		// act
		List<Integer> actuals = Anamorphism.list(is);
		// assert
		assertThat(actuals.head().get()).isEqualTo(1);
		assertThat(actuals.tail().get().head().get()).isEqualTo(2);
		assertThat(actuals.tail().get().tail().get().head().get()).isEqualTo(3);
		assertThat(actuals.tail().get().tail().get().tail().get().head()).isEmpty();
	}

	@Test
	public void testListConcat() {
		// arrange
		List<Integer> i1 = Anamorphism.list(new Integer[] { 1, 2 });
		List<Integer> i2 = Anamorphism.list(new Integer[] { 3, 4, 5 });
		// act
		List<Integer> actuals = Anamorphism.concat(i1, i2);
		// assert
		assertThat(actuals.head().get()).isEqualTo(1);
		assertThat(actuals.tail().get().head().get()).isEqualTo(2);
		assertThat(actuals.tail().get().tail().get().head().get()).isEqualTo(3);
		assertThat(actuals.tail().get().tail().get().tail().get().head().get()).isEqualTo(4);
	}
}
