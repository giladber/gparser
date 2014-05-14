package com.gparser.actions;

import com.gparser.files.ChannelFileData;
import com.gparser.files.FileChannel;
import com.gparser.parsing.Line;
import com.gparser.tests.mock.ChannelFileDataFactory;
import com.gparser.utils.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.gparser.tests.ResourceUtils.getResourceLocation;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for AddChannelsAction class.
 * Created by Gilad Ber on 5/14/14.
 */
public class AddChannelsActionTest
{
	private static ChannelFileData largeFileData;

	@BeforeClass
	public static void setup() throws IOException
	{
		ChannelFileDataFactory factory = new ChannelFileDataFactory();
		largeFileData = factory.build(getResourceLocation("result_large.txt"), "%#", "%%");
	}

	@Test
	public void testAddNoChannels()
	{
		AddChannelsAction action = new AddChannelsAction(emptyList());
		ChannelFileData result = action.apply(ChannelFileData.empty());
		assertEquals(result.getRowData().size(), 0);
		assertEquals(result.getChannels().size(), 0);

		ChannelFileData otherResult = action.apply(largeFileData);
		assertEquals(otherResult, largeFileData);
	}

	@Test(expected = NullPointerException.class)
	public void testAddNullChannels()
	{
		new AddChannelsAction(null);
	}

	@Test
	public void testAddSomeChannels()
	{
		List<String> d1 = Stream.of("1", "2", "3", "4", "5").collect(toList());
		List<String> d2 = Stream.of("x1", "x2", "x3", "x4", "x5").collect(toList());
		List<String> d3 = Stream.of("a1", "a2", "a3", "a4", "a5").collect(toList());
		FileChannel c1 = new FileChannel("c1", d1);
		FileChannel c2 = new FileChannel("c2", d2);
		FileChannel c3 = new FileChannel("c3", d3);
		List<FileChannel> channels = Stream.of(c1, c2, c3).collect(toList());
		List<Line> lines = IntStream.rangeClosed(1, 5).boxed().map(x -> new Line(x, Double.toString(x))).collect(toList());
		List<String> titles = singletonList("t1");

		ChannelFileData base = ChannelFileData.create(lines, titles, emptyList());
		AddChannelsAction action = new AddChannelsAction(channels);
		ChannelFileData result = action.apply(base);
		assertEquals(result.getChannels().size(), 4);
		assertEquals(result.getChannels().get(1), c1);
		assertEquals(result.getChannels().get(2), c2);
		assertEquals(result.getChannels().get(3), c3);
		List<FileChannel> allChannels = new ArrayList<>(4);
		allChannels.add(base.getChannels().get(0));
		allChannels.addAll(channels);

		List<Iterator<String>> iteratorList = allChannels.stream().
			map(FileChannel::getData).
			map(list -> list.iterator()).
			collect(toList());

		result.getRowData().stream().
			map(Line::getData).
			map(StringUtils::splitBySpaces).
			forEach(arr -> {
				Iterator<Iterator<String>> iteratorIterator = iteratorList.iterator();
				stream(arr).
					forEach(s -> assertEquals(iteratorIterator.next().next(), s));
			});
	}


}
