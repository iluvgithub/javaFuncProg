package com.func.list;

import static com.func.Curry.optionalize;
import static com.func.Prod.prod;
import static java.util.function.Function.identity;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import com.func.Curry;
import com.func.Prod;
import com.func.monad.MonadicSupplier;

public class Anamorphism {

	public static <S, X> Function<S, List<X>> unfold(Predicate<S> isOver, //
			Function<S, S> nextState, //
			Function<S, X> newElement) {
		return unfold(isOver, Prod.bracket(newElement, nextState));
	}

	public static <S, X> Function<S, List<X>> unfold(Predicate<S> isOver, Function<S, Prod<X, S>> g) {
		return unfold(optionalize(isOver, g));
	}

	public static <S, X> Function<S, List<X>> unfold(Function<S, Optional<Prod<X, S>>> g) {
		return g.andThen(MonadicSupplier::unit).//
				andThen(mapWithListFunctor(() -> unfold(g))).//
				andThen(Anamorphism::in);
	}

	private static <A, U, V> Function<//
			MonadicSupplier<Optional<Prod<A, U>>>, //
			MonadicSupplier<Optional<Prod<A, V>>>//
	> mapWithListFunctor(MonadicSupplier<Function<U, V>> f) {
		return supplier -> supplier.flatMap(//
				opt -> MonadicSupplier.unit(opt.map(Prod.cross(identity(), f.get())))
		//
		);
	}

	public static <X> List<X> list(X[] xs) {
		return Anamorphism.<Prod<X[], Integer>, X>unfold(//
				Prod -> Prod.left().length == Prod.right(), //
				Prod.rightMapper(n -> n + 1), //
				Prod.folder((array, index) -> array[index])).//
				apply(prod(xs, 0));

	}

	private static <A> List<A> in(MonadicSupplier<Optional<Prod<A, List<A>>>> supplier) {
		return () -> supplier.get();
	}

	public static <A> List<A> cat(List<A> l0, List<A> r0) {
		return unfold(Anamorphism.<A>concatSeed()).apply(prod(l0, r0));
	}

	private static <A> Function<Prod<List<A>, List<A>>, Optional<Prod<A, Prod<List<A>, List<A>>>>> concatSeed() {
		return lr -> {
			if (Prod.<List<A>, List<A>, Boolean>folder((l, r) -> l.isEmpty() && r.isEmpty()).apply(lr)) {
				return Optional.empty();
			} else {
				return Prod.<List<A>, List<A>, Optional<Prod<A, Prod<List<A>, List<A>>>>>folder((l, r) -> {
					if (l.isEmpty()) {
						return r.out().map(Prod.folder((hd, tl) -> prod(hd, prod(l, tl))));
					} else {
						return l.out().map(Prod.folder((hd, tl) -> prod(hd, prod(tl, r))));
					}
				}).apply(lr);
			}
		};
	}

	public static <U, V> Function<List<U>, List<V>> mapper(Function<U, V> f) {
		return unfold(mapperGenerator(f));
	}

	private static <U, V> Function<List<U>, Optional<Prod<V, List<U>>>> mapperGenerator(Function<U, V> f) {
		return l -> l.out().map(Prod.cross(f, identity()));
	}

	public static <S> Function<S, List<S>> iterate(UnaryOperator<S> u) {
		return unfold(unused -> false, Prod.bracket(identity(), u));
	}

	public static <S> Function<S, List<S>> iterate(Function<S, Optional<S>> f) {
		return partialIterate(f).andThen(mapper(opt -> opt.orElse(null)));
	}

	private static <S> Function<S, List<Optional<S>>> partialIterate(Function<S, Optional<S>> partialFunction) {
		return iterate(Curry.optionalFlatMapperUnitary(partialFunction)).compose(Optional::of);
	}

	public static Function<String, List<Character>> stringToList() {
		return unfold(String::isEmpty, Prod.bracket(s -> s.charAt(0), s -> s.substring(1)));
	}

	public static List<Character> stringToList(String s) {
		return stringToList().apply(s);
	}
}
