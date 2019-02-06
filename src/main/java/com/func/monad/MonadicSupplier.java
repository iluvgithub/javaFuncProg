package com.func.monad;

import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface MonadicSupplier<X> extends Supplier<X> {

	public static <A> MonadicSupplier<A> unit(A a) {
		return () -> a;
	}

	public default <Y> MonadicSupplier<Y> flatMap(Function<X, MonadicSupplier<Y>> f) {
		return () -> f.apply(get()).get();
	}

}
