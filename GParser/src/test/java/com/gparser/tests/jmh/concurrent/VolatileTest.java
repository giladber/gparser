package com.gparser.tests.jmh.concurrent;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

/**
 * a
 * Created by Gilad Ber on 5/9/14.
 */
@State(Scope.Group)
@SuppressWarnings("unused")
public class VolatileTest
{
	volatile int x = 0;

	@GenerateMicroBenchmark
	@Group(value = "sync")
	public int measureVolatileRead()
	{
		return x;
	}

	@GenerateMicroBenchmark
	@Group(value = "sync")
	public void measureVolatileWrite()
	{
		x = 1;
	}
}
