package com.gparser.actions;

import com.gparser.files.ChannelFileData;
import com.gparser.files.FileChannel;
import com.gparser.tests.mock.ChannelFileDataFactory;
import com.gparser.validation.CartesianProductValidator;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.gparser.tests.ResourceUtils.getResourceLocation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for CompleteAction class.
 * Created by Gilad Ber on 5/14/14.
 */
public class CompleteActionTest
{
	private static ChannelFileData largeFileData;

	@BeforeClass
	public static void setup() throws IOException
	{
		ChannelFileDataFactory factory = new ChannelFileDataFactory();
		largeFileData = factory.build(getResourceLocation("result_large.txt"), "%#", "%%");
	}

	@Test
	public void testResultCartesian()
	{
		ChannelFileData result = new CompleteAction(1, -100.0, 3).apply(largeFileData);
		ChannelFileData result2 = new CompleteAction(2, -180, 3).apply(largeFileData);
		ChannelFileData result3 = new CompleteAction(3, 0, 3).apply(largeFileData);
		CartesianProductValidator validator = new CartesianProductValidator(3);
		assertTrue(validator.validate(Optional.of(result)).isSucceeded());
		assertTrue(validator.validate(Optional.of(result2)).isSucceeded());
		assertTrue(validator.validate(Optional.of(result3)).isSucceeded());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmpty()
	{
		new CompleteAction(1, 2, 1).apply(ChannelFileData.empty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testZeroChannelNum()
	{
		new CompleteAction(0, 100, 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeChannelNum()
	{
		new CompleteAction(-1, 100, 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeIndependentChannels()
	{
		new CompleteAction(1, 500, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testZeroIndependentChannels()
	{
		new CompleteAction(0, 50.0, 0);
	}

	@Test
	public void testIllegalIndependentChannelsNumEq()
	{
		//make sure this does not throw
		new CompleteAction(2, 1.0, 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalIndependentChannelsNumLess()
	{
		new CompleteAction(2, 1.0, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalIndependentChannelsNumNeg()
	{
		new CompleteAction(2, 1.0, -1);
	}

	@Test
	public void testAddExistingValue()
	{
		CompleteAction a1 = new CompleteAction(1, 1.0, 3);
		CompleteAction a2 = new CompleteAction(1, 20.0, 3);
		CompleteAction a3 = new CompleteAction(2, -90.0, 3);
		CompleteAction a4 = new CompleteAction(2, 0.0, 3);
		CompleteAction a5 = new CompleteAction(2, 90.0, 3);
		CompleteAction a6 = new CompleteAction(3, 1.0, 3);
		CompleteAction a7 = new CompleteAction(3, 3.0, 3);
		Stream.of(a1, a2, a3, a4, a5, a6, a7).
			map(a -> a.apply(largeFileData)).
			forEach(res -> assertEquals(res, largeFileData));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddToNonExistingChannel()
	{
		CompleteAction action = new CompleteAction(5, 1.0, 3);
		action.apply(largeFileData);
	}

	@Test
	public void testAddNewValue()
	{
		CompleteAction action = new CompleteAction(2, -180.0, 3);
		ChannelFileData result1 = action.apply(largeFileData);
		CompleteAction action2 = new CompleteAction(3, 7, 3);
		ChannelFileData result2 = action2.apply(largeFileData);

		FileChannel ch1 = result1.getChannels().get(1);
		long num180 = countValuesInChannel(ch1, -180.0);
		long num90 = countValuesInChannel(ch1, 90.0);
		assertEquals(num180, num90);

		FileChannel ch2 = result2.getChannels().get(2);
		long num7 = countValuesInChannel(ch2, 7);
		long num3 = countValuesInChannel(ch2, 3);
		assertEquals(num3, num7);

		assertFollowing(ch2, 5, 7);
	}

	private void assertFollowing(FileChannel channel, double firstValue, double secondValue)
	{
		AtomicInteger prevValue = new AtomicInteger(Integer.MIN_VALUE);
		channel.getData().stream().
			map(Double::parseDouble).
			filter(x -> x == firstValue || x == secondValue).
			forEachOrdered(x -> {
				int prev = prevValue.get();
				if (prev != firstValue && prev != secondValue)
				{
					prevValue.set(x.intValue());
				}
				else
				{
					boolean a = x == firstValue;
					boolean b = x == secondValue && prev == firstValue;
					prevValue.set(x.intValue());
					assertTrue(a || b);
				}
			});
	}

	private long countValuesInChannel(FileChannel channel, double val)
	{
		return channel.getData().stream().
			map(Double::parseDouble).
			filter(x -> x == val).
			count();
	}


}
