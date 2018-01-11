package com.sandbox.funcprog.stream;

import java.util.Optional;
import java.util.function.BiFunction;

import com.sandbox.funcprog.bifunctor.Prod;

public interface ConsList<A> {

	<B> B foldLeft(B identity, BiFunction<B, A, B> accumulator);
	
	Optional<Prod<A, ConsList<A>>> out();
	
	default String trace() {
		return foldLeft("", (s, i) -> s + (s.length() == 0 ? "" : ".") + i);
	}
	
	
}
