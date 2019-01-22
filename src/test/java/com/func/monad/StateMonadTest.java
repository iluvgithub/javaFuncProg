package com.func.monad;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.Test;

import com.func.vacuum.None;

public class StateMonadTest {

	private StateMonad<Integer, Integer> get = StateMonad.<Integer>get();

	@Test
	public void testFlatMap() {
		// given
		Function<Integer, StateMonad<Integer, None>> put = s -> StateMonad.<Integer>put(s);
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
		Function<Integer, StateMonad<Integer, None>> h = x -> StateMonad.<Integer>put(x + 1);
		StateMonad<Integer, None> monad = get.flatMap(h);
		Integer n = 3;
		// when
		Integer actual = monad.apply(n).left();
		// then
		assertThat(actual).isEqualTo(n + 1);
	}

	@Test
	public void testFlatMapMass() {
		// given
		StateMonad<Integer, Integer> m = StateMonad.<Integer>get();
		int n = 30;// 000;
		for (int i = 0; i < n; ++i) {
			m = m.flatMap(x -> StateMonad.<Integer>put(x + 1).flatMap(none -> StateMonad.<Integer>get()));
		}
		// when
		Integer actual = m.apply(1).left();
		// then
		assertThat(actual).isEqualTo(n + 1);
	}
}
