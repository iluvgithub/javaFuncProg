package com.sandbox.functional.bifunctor;

import java.util.function.Function;

public interface BiFunctor<X1, X2> {

	<Y1, Y2> BiFunctor<Y1, Y2> map(Function<X1, Y1> f1, Function<X2, Y2> f2);
}
