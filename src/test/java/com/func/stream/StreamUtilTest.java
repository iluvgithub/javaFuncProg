package com.func.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.Test;

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

}
