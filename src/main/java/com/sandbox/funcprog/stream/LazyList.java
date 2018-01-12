package com.sandbox.funcprog.stream;

import static com.sandbox.funcprog.bifunctor.Prod.prod;
import static com.sandbox.funcprog.tailrecursion.Bouncer.resume;
import static com.sandbox.funcprog.tailrecursion.Bouncer.suspend;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.sandbox.funcprog.bifunctor.Prod;
import com.sandbox.funcprog.tailrecursion.Bouncer;

public class LazyList<A> implements ConsList<A> {

	private final Optional<Supplier<Prod<A, ConsList<A>>>> values;

	private LazyList(Optional<Supplier<Prod<A, ConsList<A>>>> values) {
		this.values = values;
	}

	public static <X> ConsList<X> nil() {
		return new LazyList<>(empty());
	}

	public static <X> ConsList<X> cons(Supplier<X> x, Supplier<ConsList<X>> xs) {
		return new LazyList<X>(of(() -> prod(x.get(), xs.get())));
	}

	public static ConsList<Integer> makeIntList(Integer fromIncluded, Integer toExcluded) {
		return Anamorphism.<Integer, Integer>unfold(n -> n.equals(toExcluded), n -> prod(n, n + 1)).apply(fromIncluded);
	}

	@Override
	public Optional<Prod<A, ConsList<A>>> out() {
		return values.map(Supplier::get);
	}

	@Override
	public <B> B foldLeft(B id, BiFunction<B, A, B> biFunction) {
		return bounceFoldLeft(this, id, biFunction).call();
	}

	private static <U, V> Bouncer<V> bounceFoldLeft(ConsList<U> list, V out, BiFunction<V, U, V> bi) {
		return apply(list, resume(out), (hd, tl) -> suspend(() -> bounceFoldLeft(tl, bi.apply(out, hd), bi)));
	}

	private static <U, V> V apply(ConsList<U> list, V cumulated, BiFunction<U, ConsList<U>, V> biFunction) {
		return list.out().map(headTail -> headTail.apply(biFunction)).orElse(cumulated);
	}

	@Override
	public ConsList<A> reverse() {
		return foldLeft(nil(), (list, a) -> cons(() -> a, () -> list));
	}

	@Override
	public <B> ConsList<B> map(Function<A, B> f) {
		return Anamorphism.<ConsList<A>, B>unfold(xs -> xs.out().map(prod -> prod.mapLeft(f))).apply(this);
	}

	@Override
	public ConsList<A> takeWhile(Predicate<A> p) {
		return apply((a, as) -> of(a).filter(p).map(x -> cons(() -> x, () -> as.takeWhile(p))).orElse(nil()));
	}

	private <B> ConsList<B> apply(BiFunction<A, ConsList<A>, ConsList<B>> biFunction) {
		return apply(this, nil(), biFunction);
	}

	@Override
	public ConsList<A> dropWhile(Predicate<A> predicate) {
		return dropWhile(this, predicate).call();
	}

	private static <X> Bouncer<ConsList<X>> dropWhile(ConsList<X> acc, Predicate<X> p) {
		return apply(acc, resume(acc), (hd, tl) -> p.test(hd) ? suspend(() -> dropWhile(tl, p)) : resume(acc));
	}

}
