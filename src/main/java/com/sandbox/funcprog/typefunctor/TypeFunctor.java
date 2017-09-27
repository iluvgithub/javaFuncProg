package com.sandbox.funcprog.typefunctor;

import java.util.function.Function;

import com.sandbox.funcprog.functor.Functor;
import com.sandbox.funcprog.functor.visitor.CataSeed;

public abstract class TypeFunctor<A> implements Functor<A> {
	protected abstract CataSeed<A, TypeFunctor<A>> out();

	protected <Z> Z cata(Function<CataSeed<A, Z>, Z> fun) {
		return fun.apply(out().map(initial -> initial.cata(fun)));
	}

	public abstract String trace();
}
