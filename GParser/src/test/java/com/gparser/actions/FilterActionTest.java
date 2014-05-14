package com.gparser.actions;

import com.gparser.files.ChannelFileData;
import com.gparser.files.FileChannel;
import com.gparser.tests.mock.ChannelFileDataFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.function.Predicate;

import static com.gparser.tests.ResourceUtils.getResourceLocation;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for FilterAction class.
 * Created by Gilad Ber on 5/14/14.
 */
public class FilterActionTest
{

	private static ChannelFileData largeFileData;

	@BeforeClass
	public static void setup() throws IOException
	{
		ChannelFileDataFactory factory = new ChannelFileDataFactory();
		largeFileData = factory.build(getResourceLocation("result_large.txt"), "%#", "%%");
	}

	@Test
	public void testFilterNegatives()
	{
		assertByPredicate(d -> d >= 0, 1);
	}

	@Test
	public void testFilterPositives()
	{
		assertByPredicate(d -> d < 0, 1);
	}

	@Test
	public void testFilterNonInteger()
	{
		assertByPredicate(d -> d == d.intValue(), 3);
	}

	@Test(expected = NullPointerException.class)
	public void testNPE()
	{
		assertByPredicate(d -> {
			throw new NullPointerException();
		}, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNonExistentChannel()
	{
		assertByPredicate(d -> false, 4);
	}

	@Test
	public void testFilterAll()
	{
		assertByPredicate(d -> false, 3);
	}

	private void assertByPredicate(Predicate<Double> filter, int channelNum)
	{
		ChannelFileData result = new FilterAction(channelNum + 1, d -> filter.test(Double.parseDouble(d))).apply(largeFileData);
		long countInfringing = countByPredicate(result.getChannels().get(channelNum), d -> !filter.test(d));
		long countOldInfringing = countByPredicate(largeFileData.getChannels().get(channelNum), d -> !filter.test(d));

		assertEquals(countInfringing, 0);
		assertEquals(result.getChannels().get(channelNum).getData().size(), largeFileData.getChannels().get(channelNum).getData().size() - countOldInfringing);
	}

	private long countByPredicate(FileChannel channel, Predicate<Double> p)
	{
		return channel.getData().stream().
			map(Double::parseDouble).
			filter(p).
			count();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilterEmpty()
	{
		new FilterAction(1, d -> d != null).apply(ChannelFileData.empty());
	}

	@Test(expected = NullPointerException.class)
	public void testNullFilter()
	{
		new FilterAction(1, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testZeroChannel()
	{
		new FilterAction(0, s -> true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegChannel()
	{
		new FilterAction(-1, s -> true);
	}
}
