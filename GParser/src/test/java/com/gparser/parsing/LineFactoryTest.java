package com.gparser.parsing;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Test for LineFactory class.
 * Created by Gilad Ber on 5/12/14.
 */
@SuppressWarnings("unused")
public class LineFactoryTest
{
	private LineFactory factory;

	@Before
	public void createFactory()
	{
		this.factory = new LineFactory();
	}

	@Test
	public void testIndexInitialValue()
	{
		Line first = factory.next("");
		assertEquals(first.getIndex(), 1);
	}

	@Test
	public void testStringUnchanged()
	{
		String str = "alsdnalsjdaskdsd";
		Line line = factory.next(str);
		assertSame(line.getData(), str);
	}

	@Test
	public void testIndexIncreasing()
	{
		String s = "hellostring";
		AtomicInteger index = new AtomicInteger(1);

		boolean result = IntStream.range(1, 5000).
			boxed().
			map(x -> factory.next(s)).
			allMatch(line -> line.getIndex() == index.getAndIncrement());

		assertTrue(result);
	}
}
