package com.func.monad;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.Test;

import com.func.vacuum.None;

public class StateMonadTest {

	private StateMonad<Integer, Integer> get = StateMonad.<Integer>get();
	private Function<Integer, StateMonad<Integer, None>> put = StateMonad.<Integer>put();

	@Test
	public void testFlatMap() {
		// given
		StateMonad<Integer, None> monad = get.flatMap(put);
		Integer n = 3;
		// when
		Integer actual = monad.apply(n).left();
		// then
		assertThat(actual).isEqualTo(n);
	}

	@Test
	public void testFlatMapPlus() {
		// given
		Function<Integer, StateMonad<Integer, None>> h = put.compose(x -> x + 1);
		StateMonad<Integer, None> monad = get.flatMap(h);
		Integer n = 3;
		// when
		Integer actual = monad.apply(n).left();
		// then
		assertThat(actual).isEqualTo(n + 1);
	}

}
