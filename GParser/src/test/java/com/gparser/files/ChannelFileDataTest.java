package com.gparser.files;

import com.gparser.parsing.Line;
import com.gparser.tests.mock.RandomChannelFileDataBuilder;
import com.gparser.utils.StringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for ChannelFileData class.
 * Created by Gilad Ber on 5/13/14.
 */
public class ChannelFileDataTest
{

	private static List<String> titles;
	private ChannelFileData fileData;

	@Before
	public void setupFileData()
	{
		fileData = new RandomChannelFileDataBuilder().randomize(25, new String[]{"a, b, c"}, 3);
	}

	@BeforeClass
	public static void setup()
	{
		titles = new ArrayList<>();
		titles.add("a");
		titles.add("b");
		titles.add("c");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testShortLine()
	{
		Line shortLine = new Line(1, "a b");
		ChannelFileData.create(singletonList(shortLine), titles, emptyList());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLongLine()
	{
		Line longLine = new Line(1, "a b 1 2");
		ChannelFileData.create(singletonList(longLine), titles, emptyList());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLongChannel()
	{
		fileData.addChannel(ofSize(fileData.getRowData().size() + 1, "a"));
	}

	@Test
	public void testAddChannel()
	{
		List<String> data = IntStream.range(0, fileData.getRowData().size()).
			boxed().
			map(x -> Integer.toString(x)).
			collect(toList());

		FileChannel channel = new FileChannel("channel", data);
		fileData.addChannel(channel);

		Iterator<String> iter = data.iterator();
		List<FileChannel> channels = fileData.getChannels();
		boolean result1 = listsEqual(channels.get(channels.size() - 1).getData(), data);
		boolean result2 = fileData.getRowData().stream().
			map(Line::getData).
			map(StringUtils::splitBySpaces).
			allMatch(arr -> arr[arr.length - 1].equals(iter.next()));

		assertTrue(result1 && result2);
	}

	private <T> boolean listsEqual(List<T> list1, List<T> list2)
	{
		return list1.containsAll(list2) && list2.containsAll(list1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testShortChannel()
	{
		fileData.addChannel(ofSize(fileData.getRowData().size() - 1, "a"));
	}

	private FileChannel ofSize(int size, String content)
	{
		List<String> data = new ArrayList<>();
		IntStream.range(0, size).forEach(x -> data.add(content));

		return new FileChannel("long", data);
	}

}
