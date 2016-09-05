package com.sandbox.funcprog.list;

import java.util.function.BiFunction;
import java.util.function.Function;

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

	public abstract Boolean isEqualTo(List<A> list);

	public List<A> reverse() {
		return reverseImpl(nil()).call();
	}

	protected abstract TailRecursion<List<A>> reverseImpl(List<A> accumulator);

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
			return TailRecursion.suspendCall(() -> tail.foldl(biFunc.apply(head, seed), biFunc));
		}

		@Override
		protected TailRecursion<List<A>> reverseImpl(List<A> accumulator) {
			return TailRecursion.suspendCall(() -> tail.reverseImpl(cons(head, accumulator)));
		}

		@Override
		public Boolean isEqualTo(List<A> list) {
			return list != Nil.instance && isEqualToCons((Cons<A>) list);
		}

		private Boolean isEqualToCons(Cons<A> list) {
			return head.equals(list.head) && tail.isEqualTo(list.tail);
		}

	}

	private final static class Nil<A> extends List<A> {

		private static final Nil<Object> instance = new Nil<>();

		@Override
		protected <B> TailRecursion<B> foldl(B seed, BiFunction<A, B, B> biFunc) {
			return TailRecursion.finalCall(seed);
		}

		@Override
		protected TailRecursion<List<A>> reverseImpl(List<A> accumulator) {
			return TailRecursion.finalCall(accumulator);
		}

		@Override
		public Boolean isEqualTo(List<A> list) {
			return this == list;
		}
	}

}
