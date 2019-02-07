package com.func.parsing;

import static com.func.list.List.empty;
import static com.func.parsing.Parser.guard;
import static com.func.parsing.Parser.ofVoid;

import java.util.function.Predicate;

import com.func.list.List;
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
						flatMap(v ->  Parser.of(c))
		//
		);
	}

	public static Parser<None> chr(Character c) {
		return sat(x -> x == c).flatMap(y -> ofVoid());
	}
}
