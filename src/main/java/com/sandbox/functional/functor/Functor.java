package com.sandbox.functional.functor;

import java.util.function.Function;

public interface Functor<X> {
	<Y> Functor<Y> map(Function<X, Y> f);
}
