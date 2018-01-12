package com.sandbox.funcprog.stream;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import com.sandbox.funcprog.bifunctor.Prod;

public interface ConsList<A> {

	<B> B foldLeft(B identity, BiFunction<B, A, B> biFunction);

	Optional<Prod<A, ConsList<A>>> out();

	default Optional<A> head() {
		return out().map(Prod::left);
	}

	default Optional<ConsList<A>> tail() {
		return out().map(Prod::right);
	}

	default String trace() {
		return foldLeft("", (s, i) -> s + (s.length() == 0 ? "" : ".") + i);
	}

	ConsList<A> reverse();

	<B> ConsList<B> map(Function<A, B> f);

	default <B> B foldRight(B id, BiFunction<A, B, B> biFunction) {
		return reverse().foldLeft(id, (b, a) -> biFunction.apply(a, b));
	}

	ConsList<A> takeWhile(Predicate<A> predicate);

	ConsList<A> dropWhile(Predicate<A> predicate);

}
