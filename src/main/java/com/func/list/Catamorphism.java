package com.func.list;

import static com.func.Curry.eval;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.func.Curry;

public class Catamorphism {

	public static <A, Z> Function<List<A>, Z> foldl(Z out, Function<Z, Function<A, Z>> folder) {
		return foldl(out, Curry.uncurry(folder));
	}

	public static <A, Z> Function<List<A>, Z> foldl(Z out, BiFunction<Z, A, Z> foldSeed) {
		return l -> Hylomorphism.<List<A>, A, Z>hylo(List::out, out, foldSeed).apply(l);
	}

	public static <X> String trace(List<X> xs) {
		return Catamorphism.<X>tracer().apply(xs);
	}

	private static <X> Function<List<X>, String> tracer() {
		return foldl("", (s, x) -> s + (s.length() == 0 ? "" : ".") + x);
	}

	public static <X> Integer length(List<X> xs) {
		return eval(length(), xs);
	}

	private static <X> Function<List<X>, Integer> length() {
		return foldl(0, (n, unused) -> n + 1);
	}
}
