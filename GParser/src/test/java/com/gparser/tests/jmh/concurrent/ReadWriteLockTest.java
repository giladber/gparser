package com.gparser.tests.jmh.concurrent;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * a
 * Created by Gilad Ber on 5/10/14.
 */
@State(Scope.Group)
@SuppressWarnings("unused")
public class ReadWriteLockTest
{
	Lock fairReadLock;
	Lock fairWriteLock;
	Lock unfairReadLock;
	Lock unfairWriteLock;
	int x;

	@Setup
	public void setup()
	{
		ReadWriteLock fairLock = new ReentrantReadWriteLock(true);
		fairReadLock = fairLock.readLock();
		fairWriteLock = fairLock.writeLock();

		ReadWriteLock unfairLock = new ReentrantReadWriteLock(false);
		unfairReadLock = unfairLock.readLock();
		unfairWriteLock = unfairLock.writeLock();
	}

	void fairSet(int x)
	{
		fairWriteLock.lock();
		this.x = x;
		fairWriteLock.unlock();
	}

	int fairGet()
	{
		fairReadLock.lock();
		int result = this.x;
		fairReadLock.unlock();

		return result;
	}

	void unfairSet(int x)
	{
		unfairWriteLock.lock();
		this.x = x;
		unfairWriteLock.unlock();
	}

	int unfairGet()
	{
		unfairReadLock.lock();
		int result = this.x;
		unfairReadLock.unlock();

		return result;
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
