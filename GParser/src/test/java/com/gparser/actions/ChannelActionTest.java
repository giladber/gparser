package com.gparser.actions;

import com.gparser.files.ChannelFileData;
import com.gparser.files.FileChannel;
import com.gparser.parsing.Line;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for ChannelAction class.
 * Created by Gilad Ber on 5/14/14.
 */
public class ChannelActionTest
{
	@Test
	public void testCompose()
	{
		ChannelAction a1 = c -> c;
		ChannelAction a2 = c -> null;
		ChannelAction a3 = a2.compose(a1);

		assertNull(a3.apply(ChannelFileData.empty()));
	}

	@Test
	public void testAddTwoChannelsComposed()
	{
		List<String> data1 = Stream.of("1", "2").collect(toList());
		List<String> data2 = Stream.of("a", "b").collect(toList());
		FileChannel channel1 = new FileChannel("1", data1);
		FileChannel channel2 = new FileChannel("2", data2);
		List<Line> lines = Stream.of(new Line(1, "12 34"), new Line(2, "13 24")).collect(toList());
		List<String> titles = Stream.of("a", "b").collect(toList());
		ChannelFileData base = ChannelFileData.create(lines, titles, Collections.emptyList());

		ChannelAction a1 = x -> x.addChannel(channel1);
		ChannelAction a2 = x -> x.addChannel(channel2);
		ChannelFileData twoChannels = a1.compose(a2).apply(base);
		assertEquals(twoChannels.getChannels().get(2), channel2);
		assertEquals(twoChannels.getChannels().get(3), channel1);
	}
}
