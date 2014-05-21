package com.gparser.tests.jmh.concurrent;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.locks.StampedLock;

/**
 * a
 * Created by Gilad Ber on 5/10/14.
 */
@State(Scope.Group)
@SuppressWarnings("unused")
public class StampedLockTest
{
	StampedLock lock;
	int x;

	@Setup
	public void setup()
	{
		lock = new StampedLock();
	}

	int optimisticRead()
	{
		long stamp = lock.tryOptimisticRead();
		int result = x;
		if (!lock.validate(stamp))
		{
			stamp = lock.readLock();
			result = x;
			lock.unlockRead(stamp);
		}
		return result;
	}

	int read()
	{
		long stamp = lock.readLock();
		int result = x;
		lock.unlockRead(stamp);
		return result;
	}

	void write(int x)
	{
		long stamp = lock.writeLock();
		this.x = x;
		lock.unlockWrite(stamp);
	}

	@GenerateMicroBenchmark
	@Group(value = "optimistic")
	public int measureOptimisticRead()
	{
		return optimisticRead();
	}

	@GenerateMicroBenchmark
	@Group(value = "optimistic")
	public void measureWriteWithOptimisticRead()
	{
		write(1);
	}

	@GenerateMicroBenchmark
	@Group(value = "stamped")
	public int measureRead()
	{
		return read();
	}

	@GenerateMicroBenchmark
	@Group(value = "stamped")
	public void measureWrite()
	{
		write(1);
	}
}
