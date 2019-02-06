package com.func.list;

import static com.func.Prod.prod;

import java.util.Optional;

import com.func.Prod;

@FunctionalInterface
public interface List<X> {

	Optional<Prod<X, List<X>>> out();

	default Boolean isEmpty() {
		return !out().isPresent();
	}

	default Boolean isNotEmpty() {
		return !isEmpty();
	}

	default Optional<X> head() {
		return out().map(Prod::left);
	}

	default Optional<List<X>> tail() {
		return out().map(Prod::right);
	}

	/**
	 *
	 * @return an empty List
	 */
	public static <A> List<A> empty() {
		return () -> Optional.empty();
	}

	/**
	 *
	 * @param a
	 * @return a List containing a
	 * 
	 */
	public static <A> List<A> of(A a) {
		return cons(a, empty());
	}

	/**
	 * 
	 * @param a
	 * @param as
	 * @return a list prepended with a and followed with as
	 * 
	 */
	public static <A> List<A> cons(A a, List<A> as) {
		return cons(prod(a, as));

	}

	public static <A> List<A> cons(Prod<A, List<A>> in) {
		return () -> Optional.of(in);
	}

}
