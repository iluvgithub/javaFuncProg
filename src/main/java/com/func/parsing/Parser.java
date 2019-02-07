package com.func.parsing;

import static com.func.Prod.prod;
import static com.func.list.Anamorphism.stringToList;
import static com.func.list.List.empty;

import java.util.Optional;
import java.util.function.Function;

import com.func.Prod;
import com.func.list.Anamorphism;
import com.func.list.Catamorphism;
import com.func.list.Hylomorphism;
import com.func.list.List;
import com.func.list.ListFunction;
import com.func.vacuum.None;

@FunctionalInterface
public interface Parser<X> extends Function<List<Character>, List<Prod<X, List<Character>>>> {

	public static <T> Parser<T> of(T t) {
		return l -> List.of(prod(t, l));
	}

	default <Y> Parser<Y> flatMap(Function<X, Parser<Y>> h) {
		return cs -> this.//
				andThen(uncurryAndMap(h)).//
				andThen(Hylomorphism.<Prod<Y, List<Character>>>concat()).//
				apply(cs);
	}

	public static <A, B> Function<List<Prod<A, List<Character>>>, List<List<Prod<B, List<Character>>>>> uncurryAndMap(
			Function<A, Parser<B>> h) {
		return Anamorphism.mapper(uncurry(h));
	}

	public static <T> ListFunction<Prod<T, List<Character>>> toListFunction(Parser<T> p) {
		return ListFunction.build(Prod.folder((v, cs) -> p.apply(cs)));
	}

	public static <A, B> Function<Prod<A, List<Character>>, List<Prod<B, List<Character>>>> uncurry(
			Function<A, Parser<B>> h) {
		return Prod.folder((a, cs) -> h.apply(a).apply(cs));
	}

	public static Parser<None> ofVoid() {
		return of(None.NONE);
	}

	public static <T> Parser<T> fail() {
		return l -> empty();
	}

	public static Parser<None> guard(Boolean b) {
		return Optional.of(ofVoid()).filter(v -> b).orElse(fail());
	}

	default List<Prod<X, String>> apply(String s) {
		return Anamorphism.mapper(//
				Prod.<X, List<Character>, String>rightMapper(Catamorphism::trace)//
		).compose(stringToList().andThen(this)).//
				apply(s);
	}

	default public Optional<X> parse(String s) {
		return parse(stringToList(s));
	}

	default public Optional<X> parse(List<Character> cs) {
		return apply(cs).head().map(Prod::left);
	}

	public default Parser<X> or(Parser<X> right) {
		return s -> Optional.of(apply(s)).//
				filter(List::isNotEmpty). //
				orElse(right.apply(s));//
	}

}
