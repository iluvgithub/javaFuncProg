package com.func.stream;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import com.func.Prod;
import com.func.tailrec.Hylomorphism;

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

	public static <S, A> Function<S, Stream<A>> unfold(Function<S, Optional<Prod<A, S>>> g) {
		return s -> Hylomorphism.hylo(g, Stream.<A>builder(), //
				(bld, a) -> bld.add(a)).//
				apply(s).build();
	}

}
