package com.func.list;

import static com.func.list.List.cons;
import static com.func.list.List.empty;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Test;

public class CatamorphismTest {
	private final List<Integer> EMPTY = empty();
	private final List<Integer> ONE = cons(0, empty());
	private final List<Integer> LIST = cons(0, cons(1, cons(2, empty())));

	@Test
	public void testFoldl() {
		// arrange
		String out = "e";
		BiFunction<String, Integer, String> seed = (s, i) -> "(" + s + "." + i + ")";
		Function<List<Integer>, String> fold = Catamorphism.foldl(out, seed);
		// act
		String actual = fold.apply(LIST);
		String actual0 = fold.apply(EMPTY);
		String actual1 = fold.apply(ONE);
		// assert
		assertThat(actual).isEqualTo("(((e.0).1).2)");
		assertThat(actual0).isEqualTo("e");
		assertThat(actual1).isEqualTo("(e.0)");
	}

	@Test
	public void testTrace() {
		// act
		String actual0 = Catamorphism.trace(EMPTY);
		String actual1 = Catamorphism.trace(ONE);
		String actual2 = Catamorphism.trace(LIST);
		// assert
		assertThat(actual0).isEqualTo("");
		assertThat(actual1).isEqualTo("0");
		assertThat(actual2).isEqualTo("0.1.2");
		assertThat(Catamorphism.trace(LIST)).isEqualTo("0.1.2");
	}

	@Test
	public void testLength() {
		// act
		Integer actual0 = Catamorphism.length(EMPTY);
		Integer actual1 = Catamorphism.length(ONE);
		Integer actual3 = Catamorphism.length(LIST);
		// assert
		assertThat(actual0).isEqualTo(0);
		assertThat(actual1).isEqualTo(1);
		assertThat(actual3).isEqualTo(3);
	}
}
