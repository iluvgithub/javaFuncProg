package com.sandbox.funcprog.functor;

import java.util.function.Function;

public interface Functor<X> {

	<Y> Functor<Y> map(Function<X, Y> f);

}
