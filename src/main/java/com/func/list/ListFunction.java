package com.func.list;

import java.util.function.Function;

public class ListFunction<X> implements Function<X, List<X>> {
	private final List<Function<X, List<X>>> functions;

	private ListFunction(List<Function<X, List<X>>> functions) {
		this.functions = functions;
	}

	private static <A> ListFunction<A> build(Function<A, List<A>> f, List<Function<A, List<A>>> previous) {
		return new ListFunction<>(List.cons(f, previous));
	}

	public static <A> ListFunction<A> build(Function<A, List<A>> f) {
		return build(f, List.empty());
	}

	@Override
	public List<X> apply(X x) {
		return Catamorphism.<Function<X, List<X>>, List<X>> //
				foldl(List.of(x), (xs, u) -> Hylomorphism.flatMapper(u).apply(xs)).apply(functions);

	}

	public ListFunction<X> flatten(Function<X, List<X>> f) {
		return build(f, functions);
	}
}
