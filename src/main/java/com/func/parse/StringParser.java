package com.func.parse;

import static com.func.Prod.prod;
import static com.func.sequence.Sequence.cons;
import static com.func.sequence.Sequence.nil;

import java.util.Optional;
import java.util.function.Function;

import com.func.Prod;
import com.func.sequence.Sequence;

@FunctionalInterface
public interface StringParser<A> extends Function<String, Sequence<Prod<A, String>>> {

	public static <X> StringParser<X> of(X x) {
		return s -> cons(prod(x, s), nil());
	}

	public default Optional<A> parse(String s) {
		return apply(s).head().map(Prod::left);
	}

	public default <B> StringParser<B> map(Function<A, B> f) {
		return s -> apply(s).map(Prod.cross(f, x -> x));
	}

	public default <B> StringParser<B> flatMap(Function<A, Function<String, Sequence<Prod<B, String>>>> q) {
		return // s -> Sequence.concat(apply(s).map(Prod.folder(q)));
		s -> apply(s).flatMap(Prod.folder(q));
	}

	public default StringParser<A> or(StringParser<A> right) {
		return s -> Optional.of(apply(s)).//
				filter(Sequence::isNotEmpty). //
				orElse(right.apply(s));//
	}

	public default StringParser<Sequence<A>> many() {
		return optional(some());
	}

	public default StringParser<Sequence<A>> some() {
		return flatMap(x -> many().//
				flatMap(xs -> of(cons(x, xs))));
	}

	public static <X> StringParser<Sequence<X>> none() {
		return of(nil());
	}

	public static <X> StringParser<Sequence<X>> optional(StringParser<Sequence<X>> p) {
		return p.or(none());
	}

}


