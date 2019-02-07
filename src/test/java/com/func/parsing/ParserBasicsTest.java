package com.func.parsing;

import static com.func.list.Catamorphism.length;
import static com.func.list.Catamorphism.trace;
import static com.func.parsing.Parser.fail;
import static com.func.parsing.Parser.guard;
import static com.func.parsing.Parser.ofVoid;
import static com.func.parsing.ParserBasics.chr;
import static com.func.parsing.ParserBasics.digit;
import static com.func.parsing.ParserBasics.digits;
import static com.func.parsing.ParserBasics.getc;
import static com.func.parsing.ParserBasics.sat;
import static com.func.parsing.ParserBasics.str;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.Test;

import com.func.Prod;
import com.func.list.List;
import com.func.vacuum.None;

public class ParserBasicsTest {
	@Test
	public void testGetc() {
		// given
		Parser<Character> getc = getc();
		// when
		List<Prod<Character, String>> actuals = getc.apply("abc");
		Character actual = getc.parse("abc").get();
		// then
		assertThat(actual).isEqualTo('a');
		assertThat(actuals.head().get().left()).isEqualTo('a');
		assertThat(actuals.head().get().right()).isEqualTo("b.c");
	}

	@Test
	public void testFail() {
		assertThat(fail().parse("").isPresent()).isFalse();
	}

	@Test
	public void testOf() {
		// given
		Parser<Integer> of = Parser.of(3);
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
		Parser<Character> sat = sat(x -> x == 'b');
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
		Parser<None> chr = chr('b');
		// when
		Optional<None> actual0 = chr.parse("a");
		Optional<None> actual1 = chr.parse("b");
		// then
		assertThat(actual0.isPresent()).isFalse();
		assertThat(actual1.isPresent()).isTrue();
	}

	@Test
	public void testStr() {
		// given
		Parser<None> str = str("abc");
		// when
		Optional<None> act = str("aa").parse("aac");
		Optional<None> actual = str.parse("abcd");
		Optional<None> actual0 = str.parse("abc");
		Optional<None> actual1 = str.parse("bc");
		Optional<None> actual2 = str.parse("ab");
		// then
		assertThat(act.isPresent()).isTrue();
		assertThat(actual.isPresent()).isTrue();
		assertThat(actual0.isPresent()).isTrue();
		assertThat(actual1.isPresent()).isFalse();
		assertThat(actual2.isPresent()).isFalse();
	}

	@Test
	public void testStrMass() {
		// given
		int n = 9000 ;
		String s = IntStream.range(0, n).mapToObj(i -> "a").collect(joining(""));
		Parser<None> str = str(s);
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

    public void testDigit() {
          // arrange
          Parser<Integer> digit = digit();
          // act
          Optional<Integer> actual = digit.parse("x");
          Optional<Integer> actual0 = digit.parse("");
          Optional<Integer> actual1 = digit.parse("1");
          Optional<Integer> actual5 = digit.parse("5");
          // assert
          assertThat(actual.isPresent()).isFalse();
          assertThat(actual0.isPresent()).isFalse();
          assertThat(actual1.get()).isEqualTo(1);
          assertThat(actual5.get()).isEqualTo(5);
    }



    @Test
    public void testSomeAndMany() {
          // arrange
          Parser<Integer> digit = digit();
          Parser<List<Integer>> digits = digit.many();
          // act
          Optional<List<Integer>> actual2 = digits.parse("2");
          Optional<List<Integer>> actual1 = digits.parse("23");
          Optional<List<Integer>> actual5 = digits.parse("054321");
          // assert
          assertThat(trace(actual2.get())).isEqualTo("2");
          assertThat(trace(actual1.get())).isEqualTo("2.3");
          assertThat(trace(actual5.get())).isEqualTo("0.5.4.3.2.1");
    }

    @Test
    public void testSomeAndManyMass() {
          // arrange
          Parser<None> chr = chr('a');
          Parser<List<None>> digits = chr.many();
          int n = 300;
          String s = IntStream.range(0, n).mapToObj(i -> "a").reduce((a, b) -> a + b).get();
          // act
          Optional<List<None>> actuals = digits.parse(s);
          Optional<List<None>> actual0 = digits.parse(s+"a");
          // assert
          assertThat(length(actuals.get())).isEqualTo(n);
          assertThat(length(actual0.get())).isEqualTo(n+1);
    }


    @Test
    public void testDigits() {
          // arrange
          Parser<Integer> digits = digits();
          // act
          Optional<Integer> actual2 = digits.parse("2");
          Optional<Integer> actual1 = digits.parse("23");
          Optional<Integer> actual5 = digits.parse("054321");
          Optional<Integer> actual = digits.parse("2x1");
          // assert
          assertThat(actual2.get()).isEqualTo(2);
          assertThat(actual1.get()).isEqualTo(23);
          assertThat(actual5.get()).isEqualTo(54321);
          assertThat(actual.get()).isEqualTo(2);
    }
}
