package com.gparser.actions;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for KHCompleteActionInput class.
 * Created by Gilad Ber on 5/14/2014.
 */
public class KHCompleteActionInputTest
{
	private static double[][] empty;
	private static double[][] goodBounds;

	@BeforeClass
	public static void setup()
	{
		empty = new double[1][];
		empty[0] = new double[0];

		goodBounds = new double[2][];
		goodBounds[0] = new double[]{1, 2};
		goodBounds[1] = new double[]{4, 5};
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNotLength2()
	{
		double[][] violating = new double[2][3];
		new KHCompleteActionInput(1, violating);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOneNotLength2()
	{
		double[][] violating = new double[3][];
		violating[0] = new double[2];
		violating[1] = new double[1];
		violating[2] = new double[2];
		new KHCompleteActionInput(3, violating);
	}

	@Test(expected = NullPointerException.class)
	public void testOneNull()
	{
		double[][] violating = new double[2][];
		violating[0] = new double[2];
		violating[1] = null;
		new KHCompleteActionInput(2, violating);
	}

	@Test(expected = NullPointerException.class)
	public void testNullArray()
	{
		new KHCompleteActionInput(1, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testZeroChannels()
	{
		new KHCompleteActionInput(0, empty);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeChannels()
	{
		new KHCompleteActionInput(-1, empty);
	}

	@Test
	public void testRegular()
	{
		new KHCompleteActionInput(2, goodBounds);
	}

	@Test(expected = IllegalArgumentException.class)
	public void mismatchingChannelNum()
	{
		new KHCompleteActionInput(3, goodBounds);
	}


}
