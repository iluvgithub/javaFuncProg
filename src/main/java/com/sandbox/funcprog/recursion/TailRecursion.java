package com.sandbox.funcprog.recursion;

import java.util.function.Supplier;

public abstract class TailRecursion<T> {

	public T call() {
		TailRecursion<T> tail = this;
		while (tail.isSuspended()) {
			tail = tail.resume();
		}

		return tail.compute();
	}

	public static <T> TailRecursion<T> suspendCall(Supplier<TailRecursion<T>> resume) {
		return new Suspended<>(resume);
	}

	public static <T> TailRecursion<T> finalCall(T t) {
		return new Final<>(t);
	}

	protected abstract boolean isSuspended();

	protected abstract TailRecursion<T> resume();

	protected abstract T compute();

	private static class Final<T> extends TailRecursion<T> {

		private final T t;

		private Final(T t) {
			this.t = t;
		}

		@Override
		protected TailRecursion<T> resume() {
			return null;
		}

		@Override
		protected T compute() {
			return t;
		}

		@Override
		protected boolean isSuspended() {
			return false;
		}
	}

	private static class Suspended<T> extends TailRecursion<T> {

		private final Supplier<TailRecursion<T>> resume;

		private Suspended(Supplier<TailRecursion<T>> resume) {
			this.resume = resume;
		}

		@Override
		protected TailRecursion<T> resume() {
			return resume.get();
		}

		@Override
		protected T compute() {
			return null;
		}

		@Override
		protected boolean isSuspended() {
			return true;
		}
	}

}
