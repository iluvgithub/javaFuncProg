package com.func.parse;

import static com.func.parse.BasicStringParsers.addition;
import static com.func.parse.BasicStringParsers.chr;
import static com.func.parse.BasicStringParsers.digit;
import static com.func.parse.BasicStringParsers.digits;
import static com.func.parse.BasicStringParsers.fail;
import static com.func.parse.BasicStringParsers.getc;
import static com.func.parse.BasicStringParsers.guard;
import static com.func.parse.BasicStringParsers.lower;
import static com.func.parse.BasicStringParsers.lowers;
import static com.func.parse.BasicStringParsers.ofVoid;
import static com.func.parse.BasicStringParsers.sat;
import static com.func.parse.BasicStringParsers.space;
import static com.func.parse.BasicStringParsers.str;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.Test;

import com.func.Prod;
import com.func.sequence.Sequence;
import com.func.vacuum.None;

public class BasicStringParserTest {

	@Test
	public void testGetc() {
		// given
		StringParser<Character> getc = getc();
		// when
		Sequence<Prod<Character, String>> actuals = getc.apply("abc");
		Character actual = getc.parse("abc").get();
		// then
		assertThat(actual).isEqualTo('a');
		assertThat(actuals.head().get().left()).isEqualTo('a');
		assertThat(actuals.head().get().right()).isEqualTo("bc");
	}

	@Test
	public void testFail() {
		assertThat(fail().parse("").isPresent()).isFalse();
	}

	@Test
	public void testOf() {
		// given
		StringParser<Integer> of = StringParser.of(3);
		// when
		Optional<Integer> actual = of.parse("");
		// then
		assertThat(actual.isPresent()).isTrue();
		assertThat(actual.get()).isEqualTo(3);
	}

	@Test
	public void testOfVoid() {
		assertThat(ofVoid().parse("").isPresent()).isTrue();
		assertThat(ofVoid().parse("").get()).isNotNull();
	}

	@Test
	public void testGuard() {
		assertThat(guard(false).parse("").isPresent()).isFalse();
		assertThat(guard(true).parse("").isPresent()).isTrue();
	}

	@Test
	public void testSat() {
		// given
		StringParser<Character> sat = sat(x -> x == 'b');
		// when
		Optional<Character> actual0 = sat.parse("a");
		Optional<Character> actual1 = sat.parse("b");
		// then
		assertThat(actual0.isPresent()).isFalse();
		assertThat(actual1.isPresent()).isTrue();
		assertThat(actual1.get()).isEqualTo('b');
	}

	@Test
	public void testChr() {
		// given
		StringParser<None> chr = chr('b');
		// when
		Optional<None> actual0 = chr.parse("a");
		Optional<None> actual1 = chr.parse("b");
		// then
		assertThat(actual0.isPresent()).isFalse();
		assertThat(actual1.isPresent()).isTrue();
	}

	@Test
	public void testFlatMap() {
		// given
		StringParser<None> x = chr('a');
		StringParser<None> y = chr('b');
		// when
		StringParser<None> z = x.flatMap(c -> y);
		// then
		assertThat(z.parse("ab").isPresent()).isTrue();
	}

	@Test
	public void testFlatMapMass() {
		// given
		int n = 20;
		String s = "";
		StringParser<None> x = ofVoid();
		for (int i = 0; i < n; ++i) {
			s += "a";
			x = x.flatMap(v -> chr('a'));
		}
		// when
		Optional<None> actual = x.parse(s);
		// then
		assertThat(actual.isPresent()).isTrue();
	}

	@Test
	public void testOrMass() {
		// given
		int n = 2;
		StringParser<None> x = ofVoid();
		for (int i = 0; i < n; ++i) {
			x = x.or(x);
		}
		// when
		Optional<None> actual = x.parse("");
		// then
		assertThat(actual.isPresent()).isTrue();
	}

	@Test
	public void testStr() {
		// given
		StringParser<None> str = str("abc");
		// when
		Optional<None> actual = str.parse("abcd");
		Optional<None> actual0 = str.parse("abc");
		Optional<None> actual1 = str.parse("bc");
		Optional<None> actual2 = str.parse("ab");
		// then
		assertThat(actual.isPresent()).isTrue();
		assertThat(actual0.isPresent()).isTrue();
		assertThat(actual1.isPresent()).isFalse();
		assertThat(actual2.isPresent()).isFalse();
	}

	@Test
	public void testStrMass() {
		// given
		int n = 2000;// 4000 wrong
		String s = IntStream.range(0, n).mapToObj(i -> "a").collect(joining(""));
		StringParser<None> str = str(s);
		// when
		Optional<None> actual = str.parse(s + "b");
		Optional<None> actual0 = str.parse(s);
		Optional<None> actual1 = str.parse("b");
		Optional<None> actual2 = str.parse("b" + s);
		// then
		assertThat(actual.isPresent()).isTrue();
		assertThat(actual0.isPresent()).isTrue();
		assertThat(actual1.isPresent()).isFalse();
		assertThat(actual2.isPresent()).isFalse();
	}

	@Test
	public void testLower() {
		// given
		StringParser<Character> lower = lower();
		// when
		Optional<Character> actual0 = lower.parse("abc");
		Optional<Character> actual1 = lower.parse("bB");
		Optional<Character> actual2 = lower.parse("Ab");
		// then
		assertThat(actual0.isPresent()).isTrue();
		assertThat(actual0.get()).isEqualTo('a');
		assertThat(actual1.isPresent()).isTrue();
		assertThat(actual1.get()).isEqualTo('b');
		assertThat(actual2.isPresent()).isFalse();
	}

	@Test
	public void testDigit() {
		// given
		StringParser<Integer> digit = digit();
		// when
		Optional<Integer> actual0 = digit.parse("1ab");
		Optional<Integer> actual1 = digit.parse("23");
		Optional<Integer> actual2 = digit.parse("b1B");
		// then
		assertThat(actual0.isPresent()).isTrue();
		assertThat(actual0.get()).isEqualTo(1);
		assertThat(actual1.isPresent()).isTrue();
		assertThat(actual1.get()).isEqualTo(2);
		assertThat(actual2.isPresent()).isFalse();
	}

	@Test
	public void testDigits() {
		// given
		StringParser<Integer> digits = digits();
		// when
		Optional<Integer> actual0 = digits.parse("");
		Optional<Integer> actual1 = digits.parse("23");
		Optional<Integer> actual2 = digits.parse("1234a");
		// then
		assertThat(actual0.isPresent()).isTrue();
		assertThat(actual0.get()).isEqualTo(0);
		assertThat(actual1.get()).isEqualTo(23);
		assertThat(actual2.get()).isEqualTo(1234);
	}

	@Test
	public void testLowers() {
		// given
		StringParser<String> lowers = lowers();
		// when
		Optional<String> act = lowers.parse("a");
		Optional<String> actual0 = lowers.parse("abc");
		Optional<String> actual1 = lowers.parse("abCD");
		Optional<String> actual2 = lowers.parse("Abcd");
		// then
		assertThat(act.isPresent()).isTrue();
		assertThat(act.get()).isEqualTo("a");
		assertThat(actual0.isPresent()).isTrue();
		assertThat(actual0.get()).isEqualTo("abc");
		assertThat(actual1.isPresent()).isTrue();
		assertThat(actual1.get()).isEqualTo("ab");
		assertThat(actual2.isPresent()).isTrue();
		assertThat(actual2.get()).isEqualTo("");
	}

	@Test
	public void testAddition() {
		// given
		StringParser<Integer> add = addition();
		// when
		Optional<Integer> act0 = add.parse("123");
		Optional<Integer> act1 = add.parse("34+66");
		Optional<Integer> act2 = add.parse("34+66+100");
		Optional<Integer> act3 = add.parse("34+66+100+");
		Sequence<Prod<Integer, String>> actuals = add.apply("1+2+3");
		// then
		assertThat(act0.get()).isEqualTo(123);
		assertThat(act1.get()).isEqualTo(100);
		assertThat(act2.get()).isEqualTo(200);
		assertThat(act3.get()).isEqualTo(200);

		assertThat(actuals.head().get().left()).isEqualTo(6);
		assertThat(actuals.head().get().right()).isEqualTo("");
	}

	@Test
	public void testAdditionMass() {
		// given
		StringParser<Integer> add = addition();
		int n = 100;
		String s = "0";
		for (int i = 0; i < n; ++i) {
			s += "+1";
		}
		// when
		Optional<Integer> act0 = add.parse(s);
		// then
		assertThat(act0.get()).isEqualTo(n);
	}

	@Test
	public void testSpace() {
		// given
		StringParser<None> space = space();
		// when
		Optional<None> act0 = space.parse(" ");
		Optional<None> act1 = space.parse("  ");
		Optional<Integer> act2 = space.flatMap(x -> digits()).parse(" 123");
		Optional<Integer> act3 = digits().parse(" 123");

		// then
		assertThat(act0.isPresent()).isTrue();
		assertThat(act1.isPresent()).isTrue();
		assertThat(act2.isPresent()).isTrue();
		assertThat(act2.get()).isEqualTo(123);
		assertThat(act3.get()).isEqualTo(0);
	}

}
