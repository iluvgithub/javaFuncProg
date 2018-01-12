package com.sandbox.funcprog.oldstream;

import static com.sandbox.funcprog.oldstream.LazyStream.cons;
import static com.sandbox.funcprog.oldstream.LazyStream.nil;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.sandbox.funcprog.bifunctor.Prod;

public class LazyStreamTest {

	public void testHeadTailOnEmpty() {
		// given
		LazyStream<Object> stream = nil();
		// when
		Optional<Prod<Object, LazyStream<Object>>> actual = stream.headTail();
		// then
		assertThat(actual).isEmpty();
	}

	public void testHeadTailOnCons() {
		// given
		LazyStream<String> stream = cons(() -> "a", () -> nil());
		// when
		Optional<Prod<String, LazyStream<String>>> actual = stream.headTail();
		// then
		assertThat(actual).isNotEmpty();
		assertThat(actual.get().left()).isEqualTo("a");
	}

	public void testApplyOnEmpty() {
		// given
		LazyStream<String> stream = nil();
		// when
		String actual = stream.apply("e", (x, y) -> x + "." + y);
		// then
		assertThat(actual).isEqualTo("e");
	}

	public void testApplyOnCons() {
		// given
		LazyStream<String> stream = cons(() -> "a", () -> nil());
		// when
		String actual = stream.apply("e", (x, y) -> x);
		// then
		assertThat(actual).isEqualTo("a");
	}

	public void testFoldLeft() {
		// given
		LazyStream<Integer> stream = fromList(asList(0, 1, 2));
		// when
		String actual0 = stream.foldLeft("e", (x, y) -> x + "." + y);
		String actual1 = stream.foldLeft("E", x -> y -> x + "_" + y);
		// then
		assertThat(stream.apply(-1, (hd, tl) -> hd)).isEqualTo(0);
		assertThat(actual0).isEqualTo("e.0.1.2");
		assertThat(actual1).isEqualTo("E_0_1_2");
	}

	private <X> LazyStream<X> fromList(List<X> list) {
		LazyStream<X> out = nil();
		for (int i = list.size() - 1; i >= 0; --i) {
			X x = list.get(i);
			LazyStream<X> xs = out;
			out = cons(() -> x, () -> xs);
		}
		return out;
	}

	public void testTrace() {
		// given
		LazyStream<Integer> stream = fromList(asList(0, 1, 2));
		// when
		String actual = stream.trace();
		// then
		assertThat(actual).isEqualTo("0.1.2");
	}

	public void testMap() {
		// given
		LazyStream<Integer> stream = fromList(asList(0, 1, 2));
		// when
		LazyStream<String> actual = stream.map(x -> x.toString());
		// then
		assertThat(actual).isEqualTo("0.1.2");
	}
}
