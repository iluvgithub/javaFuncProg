package com.sandbox.funcprog.functor.visitor;

import java.util.function.Function;

import com.sandbox.funcprog.functor.Functor;

public interface CataSeed <T, X> extends Functor<X> {

    @Override
    <Y> CataSeed<T, Y> map(Function<X, Y> f);

    X accept(CataSeedVisitor<T, X> visitor);

}