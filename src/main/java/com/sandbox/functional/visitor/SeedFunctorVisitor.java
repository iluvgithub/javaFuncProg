package com.sandbox.functional.visitor;

import com.sandbox.functional.list.ListlFunctor;
import com.sandbox.functional.list.ListrFunctor;

public interface SeedFunctorVisitor<T, X> {

	default X visit(ListrFunctor<T, X> target) {
		throw new Error("not implemented");
	}

	default X visit(ListlFunctor<T, X> target) {
		throw new Error("not implemented");
	}

}
