package com.func.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Test;

import com.func.Prod;

public class StreamUtilTest {

	@Test
	public void testFoldl() {
		// given
		Stream<Integer> str = Stream.of(1, 2, 3, 4);
		// when
		Double actual = StreamUtil.<Integer, Double>foldl((a, b) -> a + b, 0.0).apply(str);
		// then
		assertThat(actual).isEqualTo(10.0);
	}

	@Test
	public void testUnfold() {
		// given
		String s0 = "";
		String s1 = "abc";
		Function<String, Optional<Prod<Character, String>>> g = x -> Optional.of(x).//
				filter(s -> s.length() > 0).//
				map(s -> Prod.prod(s.charAt(0), s.substring(1)));
		Function<String, Stream<Character>> h = StreamUtil.unfold(g);
		// when
		String actual0 = h.apply(s0).map(Object::toString).reduce("", (s, c) -> s + c);
		String actual1 = h.apply(s1).map(Object::toString).reduce("", (s, c) -> s + c);
		// then
		assertThat(actual0).isEqualTo(s0);
		assertThat(actual1).isEqualTo(s1);
	}

}
