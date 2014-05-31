package com.gparser.java8.examples;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.out;
import static java.util.concurrent.CompletableFuture.*;
import static java.util.concurrent.ThreadLocalRandom.current;


/**
 * Examples of using CompletableFuture in Java 8.
 * Created by Gilad Ber on 5/9/14.
 */
public class Futures
{
	public static void main(String... args)
	{
		example1();
		example2();
		example3();
	}

	public static void example1()
	{
		CompletableFuture<Void> f1 = runAsync(() -> out.println("1"));
		CompletableFuture<Double> f2 = supplyAsync(() -> current().nextDouble());
		CompletableFuture<Double> f3 = supplyAsync(() -> Math.atan2(current().nextDouble(), current().nextDouble()));
		CompletableFuture<Void> f4 = allOf(f1, f2.exceptionally(t -> -1D), f3.thenAcceptAsync(d -> out.println("Result: " + d)));
		f4.thenRun(() -> out.println("Finished all tasks in example1!"));
	}

	public static void example2()
	{
		LongAdder adder = new LongAdder();
		CompletableFuture<Void> f1 = supplyAsync(() -> current().nextLong()).thenAccept(adder::add);
		CompletableFuture<Void> f2 = supplyAsync(() -> current().nextLong()).thenAccept(adder::add);
		CompletableFuture<Long> f3 = f1.thenCombineAsync(f2, (x, y) -> adder.sum());
		f3.thenAcceptAsync(x -> out.println("Result is " + x));
	}

	public static void example3()
	{
		CompletableFuture<String> firstName = supplyAsync(() -> randomString(7));
		CompletableFuture<String> middleName = supplyAsync(() -> randomString(4));
		CompletableFuture<String> lastName = supplyAsync(() -> randomString(8));
		CompletableFuture<String> fullName = firstName.thenCombineAsync(middleName, (a, b) -> a + "-" + b).
			thenCombineAsync(lastName, (a, b) -> "Full name: " + a + " " + b);
		fullName.thenAccept(out::println);
	}

	private static char randomChar()
	{
		return (char) ('A' + current().nextInt('z' - 'A'));
	}

	public static String randomString(int length)
	{
		randomWait();

		return IntStream.range(0, length).
			boxed().
			map(x -> String.valueOf(randomChar())).
			collect(Collectors.joining());
	}

	private static void randomWait()
	{
		try
		{
			Thread.sleep(current().nextInt(5) * 1000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
