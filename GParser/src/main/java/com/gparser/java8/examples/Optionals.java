package com.gparser.java8.examples;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

/**
 * Example usages of java.util.Optional class in Java 8.
 * Created by Gilad Ber on 5/12/14.
 */
public class Optionals
{
	public static void main(String... args)
	{
		example(500);
		example2();
	}


	public static void example(int max)
	{
		Optional<Integer> res = IntStream.range(0, max).
			boxed().
			map(x -> x * x + 1).
			reduce((x, y) -> x ^ y);

		Optional<Double> opt = res.filter(x -> x != 0).map(Math::sqrt);
		System.out.println(opt.orElse(ThreadLocalRandom.current().nextDouble()));
	}

	public static void example2()
	{
		Optional<String> firstName = ofNullable(callToServiceWhichMayReturnNull());
		Optional<String> lastName = ofNullable(callToServiceWhichMayReturnNull());
		Optional<String> address = ofNullable(callToServiceWhichMayReturnNull());

		String name = firstName.
			flatMap(first -> of(first + " " + lastName.orElse("last"))).
			flatMap(full -> of(full + " @ " + address.orElse("Hagoshrim"))).
			orElse("No name :(");

		System.out.println(name);
	}

	private static String callToServiceWhichMayReturnNull()
	{
		return ThreadLocalRandom.current().nextBoolean() ? null : "bwah";
	}
}
