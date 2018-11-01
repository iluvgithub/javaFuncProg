package com.func.stream;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import com.func.Prod;

public class StreamUtil {

	public static <X, Z> Function<Stream<X>, Z> foldl(BiFunction<Z, X, Z> bi, Z z) {
		return str -> str.//
				map(x -> Prod.<X, Z>prod(x, null)).//
				reduce(Prod.<X, Z>prod(null, z), reducer(bi)).//
				right();
	}

	private static <X, Z> BinaryOperator<Prod<X, Z>> reducer(BiFunction<Z, X, Z> bi) {
		return (xz1, xz2) -> Prod.<X, Z>prod(xz2.left(), bi.apply(xz1.right(), xz2.left()));
	}

}
