package com.sandbox.functional.visitor;

import com.sandbox.functional.list.ListrFunctor;

public interface SeedFunctorVisitor<T, X> {

	default X visit(ListrFunctor<T, X> target) {
		throw new Error("not implemented");
	}

}
