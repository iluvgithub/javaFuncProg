package com.sandbox.funcprog.list;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class List<A> {

	public static <T> List<T> cons(T head, List<T> tail) {
		return new Cons<T>(head, tail);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> nil() {
		return (Nil<T>) Nil.instance;
	}

	public abstract <B> B foldRight(B seed, BiFunction<A, B, B> biFunc);

	public abstract <B> B foldLeft(B seed, BiFunction<A, B, B> biFunc);

	public abstract Boolean isEqualTo(List<A> list);

	public <B> List<B> map(Function<A, B> func) {
		return foldRight(nil(), (a, list) -> cons(func.apply(a), list));
	}
	 

	public List<A> cat(List<A> list) {
		return foldRight(list, (a, as) -> cons(a, as));
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
		public <B> B foldRight(B seed, BiFunction<A, B, B> biFunc) {
			return biFunc.apply(head, tail.foldRight(seed, biFunc));
		}

		@Override
		public <B> B foldLeft(B seed, BiFunction<A, B, B> biFunc) {
			return tail.foldLeft(biFunc.apply(head, seed), biFunc);
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
		public <B> B foldLeft(B seed, BiFunction<A, B, B> biFunc) {
			return seed;
		}

		@Override
		public <B> B foldRight(B seed, BiFunction<A, B, B> biFunc) {
			return seed;
		}

		@Override
		public Boolean isEqualTo(List<A> list) {
			return this == list;
		}

	}

}
