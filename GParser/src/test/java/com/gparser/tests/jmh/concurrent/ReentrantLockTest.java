package com.gparser.tests.jmh.concurrent;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * a
 * Created by Gilad Ber on 5/10/14.
 */
@State(Scope.Group)
@SuppressWarnings("unused")
public class ReentrantLockTest
{
	Lock fairLock;
	Lock unfairLock;
	int x;

	void fairSet(int x)
	{
		fairLock.lock();
		this.x = x;
		fairLock.unlock();
	}

	int fairGet()
	{
		fairLock.lock();
		int result = this.x;
		fairLock.unlock();

		return result;
	}

	void unfairSet(int x)
	{
		unfairLock.lock();
		this.x = x;
		unfairLock.unlock();
	}

	int unfairGet()
	{
		unfairLock.lock();
		int result = this.x;
		unfairLock.unlock();

		return result;
	}

	@Setup
	public void setup()
	{
		fairLock = new ReentrantLock(true);
		unfairLock = new ReentrantLock(false);
	}

	@GenerateMicroBenchmark
	@Group(value = "fair")
	public int measureFairRead()
	{
		return fairGet();
	}

	@GenerateMicroBenchmark
	@Group(value = "fair")
	public void measureFairWrite()
	{
		fairSet(1);
	}

	@GenerateMicroBenchmark
	@Group(value = "unfair")
	public int measureUnfairRead()
	{
		return unfairGet();
	}

	@GenerateMicroBenchmark
	@Group(value = "unfair")
	public void measureUnfairWrite()
	{
		unfairSet(1);
	}


}
