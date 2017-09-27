package com.sandbox.funcprog.functor.visitor;

import com.sandbox.funcprog.typefunctor.list.ListNilFunctor;

public interface CataSeedVisitor<A, Z> {

	Z visit(ListNilFunctor<A, Z> cataSeed);
}
