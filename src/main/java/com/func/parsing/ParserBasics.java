package com.func.parsing;

import static com.func.Prod.prod;
import static com.func.list.Anamorphism.reverseStringToList;
import static com.func.list.List.empty;
import static com.func.parsing.Parser.guard;
import static com.func.parsing.Parser.ofVoid;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import com.func.Prod;
import com.func.list.Catamorphism;
import com.func.list.List;
import com.func.list.ListFunction;
import com.func.vacuum.None;

public class ParserBasics {

	public static Parser<Character> getc() {
		return l -> l.out().map(prd -> List.of(prd)).orElse(empty());
	}

	public static <X> Parser<X> fail() {
		return l -> empty();
	}

	public static Parser<Character> sat(Predicate<Character> p) {
		return getc().flatMap(//
				c -> guard(p.test(c)).//
						flatMap(v -> Parser.of(c))
		//
		);
	}

	public static Parser<None> chr(Character c) {
		return sat(x -> x == c).flatMap(y -> ofVoid());
	}

	public static Parser<None> str(String s) {
		return str(reverseStringToList().apply(s));
	}

	public static Parser<None> str(List<Character> cs) {
		return xs -> Catamorphism.foldl(//
				Parser.toListFunction(ofVoid()), //
				strSeed()).//
				apply(cs).//
				apply(prod(None.NONE, xs));
	}

	private static BiFunction<ListFunction<Prod<None, List<Character>>>, Character, ListFunction<Prod<None, List<Character>>>> strSeed() {
		return (l, c) -> l.flatten(ncs -> chr(c).apply(ncs.right()));
	}

}
