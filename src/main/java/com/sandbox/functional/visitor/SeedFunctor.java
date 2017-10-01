package com.sandbox.functional.visitor;

import java.util.function.Function;

import com.sandbox.functional.functor.Functor;

public interface SeedFunctor<T, X> extends Functor<X> {

    X accept(SeedFunctorVisitor<T, X> visitor);
    
    @Override
    <Y> SeedFunctor<T, Y> map(Function<X, Y> f);

}