package com.func.list;

import static com.func.Prod.prod;
import static com.func.list.List.cons;
import static com.func.list.List.empty;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;

import com.func.Prod;

public class HylomorphismTest {

	@Test
	public void testBigSum() {
		// arrange
		int n = 8000;
		// act
		Long actual = Hylomorphism.<Integer, Long, Long>hylo(//
				x -> x == 0, //
				x -> prod(0L + x, x - 1), //
				0L, //
				(l, x) -> l + x).apply(n);
		// assert
		assertThat(actual).isEqualTo(n * (n + 1) / 2);
	}

	@Test
	public void testHylomorphismLeftToRightAndRighToLeft() {
		// arrange
		Predicate<Integer> isOver = i -> i == 0;
		BiFunction<String, Long, String> Catamorphism = (s, l) -> "(" + s + "." + l + ")";
		Function<Integer, Prod<Long, Integer>> unfoldSeed = i -> prod(i + 0L, i - 1);
		Function<Integer, String> left = Hylomorphism.hylo(isOver, unfoldSeed, "e", Catamorphism);
		BiFunction<Long, String, String> CatamorphismR = (l, s) -> "(" + l + "." + s + ")";
		Function<Integer, String> right = Hylomorphism.hylo(isOver, unfoldSeed, CatamorphismR, "e");
		// act
		String actualL = left.apply(3);
		String actualR = right.apply(3);
		// assert
		assertThat(actualL).isEqualTo("(((e.3).2).1)");
		assertThat(actualR).isEqualTo("(3.(2.(1.e)))");
	}

	@Test
	public void testFilter() {
		// arrange
		Predicate<Integer> p = x -> x % 2 == 0;
		List<Integer> empty = empty();
		List<Integer> one = cons(0, empty());
		List<Integer> list = cons(0, cons(1, cons(2, empty())));
		// act
		List<Integer> actuals0 = Hylomorphism.filter(p).apply(empty);
		List<Integer> actuals1 = Hylomorphism.filter(p).apply(one);
		List<Integer> actuals = Hylomorphism.filter(p).apply(list);
		// assert
		assertThat(Catamorphism.trace(actuals0)).isEqualTo("");
		assertThat(Catamorphism.trace(actuals1)).isEqualTo("0");
		assertThat(Catamorphism.trace(actuals)).isEqualTo("0.2");
	}

	@Test
	public void testFlatMap() {
		// arrange
		List<Integer> i1 = Anamorphism.list(new Integer[] { 1, 2, 3 });
		Predicate<Integer> p = n -> n == 0;
		Function<Integer, Prod<String, Integer>> g = n -> prod("" + n, n - 1);
		Function<Integer, List<String>> h = Anamorphism.unfold(p, g);
		// act
		List<String> actuals = Hylomorphism.flatMapper(h).apply(i1);
		String actual = Catamorphism.trace(actuals);
		// assert
		assertThat(actual).isEqualTo("1.2.1.3.2.1");
	}
}
