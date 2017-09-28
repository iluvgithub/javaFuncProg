package com.sandbox.functional.functor;

import java.util.function.Function;

import com.sandbox.functional.visitor.SeedFunctor;

public interface Initial<T> extends Functor<T> {

	SeedFunctor<T, Initial<T>> out();

	default <Z> Z cata(Function<SeedFunctor<T, Z>, Z> f) {
		return f.apply(out().map(initial -> initial.cata(f)));
	}

}