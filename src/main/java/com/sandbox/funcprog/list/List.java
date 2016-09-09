package com.sandbox.funcprog.list;

import static com.sandbox.funcprog.pair.Pair.pair;
import static com.sandbox.funcprog.recursion.TailRecursion.finalCall;
import static com.sandbox.funcprog.recursion.TailRecursion.suspendCall;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import com.sandbox.funcprog.pair.Pair;
import com.sandbox.funcprog.recursion.TailRecursion;

public abstract class List<A> {
	public static <T> List<T> cons(T head, List<T> tail) {
		return new Cons<T>(head, tail);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> nil() {
		return (Nil<T>) Nil.instance;
	}

	public <B> B foldLeft(B seed, BiFunction<A, B, B> biFunc) {
		return foldl(seed, biFunc).call();
	}

	protected abstract <B> TailRecursion<B> foldl(B seed, BiFunction<A, B, B> biFunc);

	public static <A, B> List<A> unfold(Predicate<B> keepGoing, Function<B, A> head, Function<B, B> tail, B input) {
		return unfoldAcc(keepGoing, head, tail, input, nil()).call().reverse();
	}

	private static <A, B> TailRecursion<List<A>> unfoldAcc(Predicate<B> keepGoing, Function<B, A> hd, Function<B, B> tl,
			B in, List<A> acc) {
		if (keepGoing.test(in)) {
			return suspendCall(() -> unfoldAcc(keepGoing, hd, tl, tl.apply(in), cons(hd.apply(in), acc)));
		} else {
			return finalCall(acc);
		}

	}

	protected abstract Cons<A> toCons();

	public <X, Y> List<Pair<X, Y>> zip(Pair<List<X>, List<Y>> input) {
		Predicate<Pair<List<X>, List<Y>>> keepGoing = pair -> pair.left() != nil() && pair.right() != nil();
		Function<Pair<List<X>, List<Y>>, Pair<X, Y>> hd = pair -> pair(pair.left().toCons().head,
				pair.right().toCons().head);
		Function<Pair<List<X>, List<Y>>, Pair<List<X>, List<Y>>> tl =

		pair -> pair(pair.left().toCons().tail, pair.right().toCons().tail);
		return unfold(keepGoing, hd, tl, input);
	}

	public Boolean isEqualTo(List<A> list) {
		if (!this.size().equals(list.size())) {
			return false;
		}

		return zip(pair(this, list)).foldLeft(true, (pair, bool) -> bool && pair.left().equals(pair.right()));

	}

	public Integer size() {
		return foldLeft(0, (a, i) -> i + 1);
	}

	public List<A> reverse() {
		return foldLeft(nil(), (hd, tl) -> cons(hd, tl));
	}

	public <B> List<B> map(Function<A, B> func) {
		return reverse().foldLeft(nil(), (a, list) -> cons(func.apply(a), list));
	}

	public List<A> cat(List<A> list) {
		return reverse().foldLeft(list, (a, as) -> cons(a, as));
	}

	private final static class Cons<A> extends List<A> {

		private final A head;

		private final List<A> tail;

		public Cons(A head, List<A> tail) {
			super();
			this.head = head;
			this.tail = tail;
		}

		@Override

		public <B> TailRecursion<B> foldl(B seed, BiFunction<A, B, B> biFunc) {
			return suspendCall(() -> tail.foldl(biFunc.apply(head, seed), biFunc));
		}

		@Override
		protected Cons<A> toCons() {
			return this;
		}

	}

	private final static class Nil<A> extends List<A> {

		private static final Nil<Object> instance = new Nil<>();

		@Override
		protected <B> TailRecursion<B> foldl(B seed, BiFunction<A, B, B> biFunc) {
			return finalCall(seed);
		}

		@Override

		protected Cons<A> toCons() {
			throw new RuntimeException("Should never call Nil::toCons");
		}

	}
}
