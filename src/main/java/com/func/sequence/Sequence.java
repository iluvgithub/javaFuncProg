package com.func.sequence;

import static com.func.Prod.prod;
import static com.func.tailrec.Bouncer.done;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.function.Function.identity;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.func.Prod;
import com.func.tailrec.Bouncer;

@FunctionalInterface
public interface Sequence<A> extends Supplier<Optional<Prod<A, Sequence<A>>>> {

	public static <X> Sequence<X> nil() {
		return () -> empty();
	}

	public static <X> Sequence<X> cons(X x, Sequence<X> xs) {
		return () -> of(prod(x, xs));
	}

	public static <X> Sequence<X> tau(X x) {
		return cons(x, nil());
	}

	public static <X> Sequence<X> cons(Supplier<Prod<X, Sequence<X>>> supplier) {
		return () -> of(supplier.get());
	}

	public default Optional<Prod<A, Sequence<A>>> headTail() {
		return get();
	}

	public default Boolean isEmpty() {
		return headTail().map(x -> false).orElse(true);
	}

	public default Boolean isNotEmpty() {
		return !isEmpty();
	}

	public default Optional<A> head() {
		return headTail().map(Prod::left);
	}

	public default Optional<Sequence<A>> tail() {
		return headTail().map(Prod::right);
	}

	public default <Z> Z fold(Z z, BiFunction<Z, A, Z> bif) {
		return fold0(this, z, bif).eval();
	}

	public static <X, Z> Bouncer<Z> fold0(Sequence<X> list, Z z, BiFunction<Z, X, Z> bif) {
		return list.headTail().//
				map(Prod.<X, Sequence<X>, Bouncer<Z>>//
						folder((x, xs) -> () -> fold0(xs, bif.apply(z, x), bif)))
				.//
				orElse(done(z));
	}

	public default String trace() {
		return fold("", (acc, s) -> acc + of(".").filter(x -> acc.length() > 0).orElse("") + s);
	}

	public default Integer size() {
		return fold(0, (i, a) -> i + 1);
	}

	public static <S, X> Function<S, Sequence<X>> unfold(Predicate<S> p, Function<S, Prod<X, S>> g) {
		return unfold(s -> of(s).//
				filter(p.negate()).//
				map(x -> of(g.apply(x))).//
				orElse(empty()));
	}

	public static <S, X> Function<S, Sequence<X>> unfold(Function<S, Optional<Prod<X, S>>> g) {
		return g.andThen(opt -> opt.//
				map(//
						Prod.<X, S, Sequence<X>>folder((x, s) -> () -> of(prod(x, unfold(g).apply(s))))
				//
				).//
				orElse(nil()));
	}

	public default <B> Sequence<B> map(Function<A, B> f) {
		return Sequence.<Sequence<A>, B>unfold(//
				as -> as.headTail().map(Prod.cross(f, identity()))//
		).apply(this);
	}

	public static <X, Y> Function<Sequence<X>, Sequence<Y>> mapper(Function<X, Y> f) {
		return xs -> xs.map(f);
	}

	public default <B> Sequence<B> flatMap(Function<A, Sequence<B>> f) {
		return fold(nil(), (bs, a) -> bs.cat(f.apply(a)));
	}

	public default Sequence<A> cat(Sequence<A> two) {
		return cat(this, two);
	}

	public static <X> Sequence<X> cat(Sequence<X> l, Sequence<X> r) {
		return r.fold(l.reverse(), (xs, x) -> cons(x, xs)).reverse();
	}

	public static <X> Sequence<X> concat(Sequence<Sequence<X>> xss) {
		return xss.fold(nil(), (l, r) -> l.cat(r));
	}

	public default Sequence<A> filter(Predicate<A> p) {
		return fold(nil(), (as, a) -> //
		Optional.of(a).filter(p).//
				map(x -> as.cat(tau(x))).orElse(as)
		//
		);
	}

	public default Sequence<A> reverse() {
		return fold(nil(), (as, a) -> cons(a, as));
	}

}
