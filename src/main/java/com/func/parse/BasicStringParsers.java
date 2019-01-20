package com.func.parse;

import static com.func.Prod.prod;
import static com.func.sequence.Sequence.nil;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.func.Prod;
import com.func.sequence.Sequence;
import com.func.vacuum.None;

public class BasicStringParsers {
	private static final Predicate<String> NON_EMPTY = s -> s.length() > 0;

	public static StringParser<Character> getc() {
		return s0 -> outString(s0).//
				map(Sequence::tau).//
				orElse(nil());
	}

	private static Optional<Prod<Character, String>> outString(String s) {
		return Optional.of(s).filter(NON_EMPTY).//
				map(x -> prod(x.charAt(0), x.substring(1)));
	}

	public static StringParser<None> ofVoid() {
		return StringParser.of(None.INSTANCE);
	}

	public static <X> StringParser<X> fail() {
		return s -> nil();
	}

	public static StringParser<Character> sat(Predicate<Character> p) {
		return getc().flatMap(//
				c -> guard(p.test(c)).//
						flatMap(v -> StringParser.of(c))
		//
		);
	}

	public static StringParser<None> guard(Boolean b) {
		return Optional.of(ofVoid()).//
				filter(v -> b).//
				orElse(fail());
	}

	public static StringParser<None> chr(Character c) {
		return sat(x -> x == c).flatMap(y -> ofVoid());
	}

	public static StringParser<None> str(String s) {
		return stringToList().apply(s).fold(ofVoid(), //
				(sp, c) -> sp.flatMap(v -> chr(c))//
		);
	}

	protected static Function<String, Sequence<Character>> stringToList() {
		return Sequence.unfold(BasicStringParsers::outString);
	}

	public static StringParser<Character> lower() {
		return sat(between('a', 'z'));
	}

	private static Predicate<Character> between(Character from, Character to) {
		return x -> from <= x && x <= to;
	}

	public static StringParser<Integer> digit() {
		return sat(between('0', '9')).flatMap(d -> StringParser.of(d - '0'));
	}

	public static StringParser<Integer> digits() {
		return digitRemain(0);
	}

	private static StringParser<Integer> digitRemain(Integer m) {
		return digit().flatMap(d -> digitRemain(10 * m + d)).or(StringParser.of(m));
	}

	public static StringParser<String> lowers() {
		return lower().many().map(BasicStringParsers::asString);
	}

	private static String asString(Sequence<Character> cs) {
		return cs.fold("", (s, c) -> s + c);
	}

	public static StringParser<Integer> addition() {
		return digits().flatMap(m -> additionRest(m));
	}

	private static StringParser<Integer> additionRest(Integer m) {
		return chr('+').//
				flatMap(x -> digits().//
						flatMap(n -> additionRest(m + n)))
				.//
				or(StringParser.of(m));
	}

	public static StringParser<None> space() {
		return sat(isSpace()).many().flatMap(x -> ofVoid());
	}

	public static Predicate<Character> isSpace() {
		return c -> c == ' ';
	}

	public static <X> StringParser<X> token(StringParser<X> p) {
		return space().flatMap(x -> p);
	}

}
