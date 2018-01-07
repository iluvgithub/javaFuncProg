package com.sandbox.funcprog.stream;

import static com.sandbox.funcprog.stream.LazyStream.cons;
import static com.sandbox.funcprog.stream.LazyStream.nil;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;

import com.sandbox.funcprog.bifunctor.Prod;

public class LazyStreamTest {

	@Test
	public void testHeadTailOnEmpty() {
		// given
		LazyStream<Object> stream = nil();
		// when
		Optional<Prod<Object, LazyStream<Object>>> actual = stream.headTail();
		// then
		assertThat(actual).isEmpty();
	}

	@Test
	public void testHeadTailOnCons() {
		// given
		LazyStream<String> stream = cons(() -> "a", () -> nil());
		// when
		Optional<Prod<String, LazyStream<String>>> actual = stream.headTail();
		// then
		assertThat(actual).isNotEmpty();
		assertThat(actual.get().left()).isEqualTo("a");
	}

}
