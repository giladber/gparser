package com.gparser.java8.examples;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

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
		example2WithoutOptional();
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

		optionalCall1().
			flatMap(Optionals::optionalCall2).
			flatMap(Optionals::optionalCall3).
			ifPresent(System.out::println);
	}

	public static void example2WithoutOptional()
	{
		String s1 = optionalCall1().get();
		if (s1 != null)
		{
			Integer x2 = optionalCall2(s1).get();
			if (x2 != null)
			{
				String s3 = optionalCall3(x2).get();
				if (s3 != null)
				{
					System.out.println(s3);
				}
			}
		}
	}


	private static Optional<String> optionalCall1()
	{
		return Optional.ofNullable(Futures.randomString(10));
	}

	private static Optional<Integer> optionalCall2(String s)
	{
		return Optional.ofNullable(s.length());
	}

	private static Optional<String> optionalCall3(int x)
	{
		return Optional.ofNullable(Futures.randomString(x));
	}
}
