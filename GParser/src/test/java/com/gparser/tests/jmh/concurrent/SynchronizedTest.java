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
public class SynchronizedTest
{
	int x = 0;

	synchronized int get()
	{
		return x;
	}

	synchronized void set(int x)
	{
		this.x = x;
	}

	@GenerateMicroBenchmark
	@Group(value = "sync")
	public int measureSyncRead()
	{
		return get();
	}

	@GenerateMicroBenchmark
	@Group(value = "sync")
	public void measureSyncWrite()
	{
		set(1);
	}
}
