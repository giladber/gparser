package com.gparser.java8.examples;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.stream.Collectors.groupingBy;

/**
 * Examples for using the new atomic classes in java 8.
 * Created by Gilad Ber on 5/9/14.
 */
public class Concurrency
{
	public static void main(String... args)
	{
		example1();
		example2();
		collatz();
	}

	private static void collatz()
	{
		long a = System.nanoTime();
		boolean all = collatzUpTo(1_000_000);
		long b = System.nanoTime() - a;
		System.out.println("Collatz time: " + b / 1E6 + "ms");
		System.out.println("Did we disprove collatz?  " + (all ? "YEAH!!!!" : "nope :("));
	}

	private static void example1()
	{
		ConcurrentHashMap<Long, String> map = new ConcurrentHashMap<>();
		IntStream.rangeClosed(1, 10).forEach(x -> map.put((long) x, Long.toString(current().nextLong())));
		map.computeIfAbsent(15L, x -> x + " is fifteen");

		String result = map.reduce(10, (k, v) -> k.toString() + ": " + v, (s1, s2) -> s1 + "\n" + s2);
		System.out.println(result);

		int charNum = map.reduceValues(10, String::length, (v1, v2) -> v1 + v2);
		System.out.println("Total char num in all values is " + charNum);

		double[] arr = new double[500];
		Arrays.parallelSetAll(arr, i -> 2 * i * current().nextDouble());
		Arrays.parallelPrefix(arr, (x, y) -> x + y - 1);
		Arrays.parallelSort(arr);
		System.out.println(Arrays.toString(arr));
	}

	private static void example2()
	{
		LongAccumulator acc = new LongAccumulator((a, b) -> Math.abs(a - b), 1);
		LongStream randomStream = LongStream.generate(() -> current().nextLong());
		randomStream.parallel().limit(300).forEach(acc::accumulate);
		System.out.println("Result is: " + acc.get());
	}


	private static boolean testCollatz(long start)
	{
		LongStream seq = LongStream.iterate(start, x -> (x & 1) == 0 ? x >> 1 : 3 * x + 1);
		return seq.anyMatch(x -> x == 1);
	}

	private static boolean collatzUpTo(long max)
	{
		Map<Boolean, List<Long>> results = LongStream.rangeClosed(2, max).
			boxed().
			parallel().
			collect(groupingBy(Concurrency::testCollatz));

		return results.getOrDefault(false, Collections.emptyList()).size() > 0;
	}
}
