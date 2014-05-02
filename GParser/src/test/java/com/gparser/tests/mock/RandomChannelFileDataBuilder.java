package com.gparser.tests.mock;

import com.gparser.actions.AddChannelsAction;
import com.gparser.actions.ChannelAction;
import com.gparser.actions.SortAction;
import com.gparser.files.ChannelFileData;
import com.gparser.files.FileChannel;
import com.gparser.parsing.Line;
import com.gparser.parsing.LineFactory;
import com.gparser.utils.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Builder class for supplying randomly generated channel file data objects.
 * Created by Gilad Ber on 5/1/14.
 */
public class RandomChannelFileDataBuilder
{
	public ChannelFileData randomize(final long maxLength, String[] titles, int numDependentChannels)
	{
		List<Line> lines = createIndependentLines(maxLength, titles);
		ChannelAction compositeAction = createCompositeAction(titles, numDependentChannels, lines);

		ChannelFileData unsorted = ChannelFileData.create(lines, Arrays.asList(titles), Collections.emptyList());
		return compositeAction.apply(unsorted);
	}

	private ChannelAction createCompositeAction(String[] titles, int numDependentChannels, List<Line> lines)
	{
		List<FileChannel> dependentChannels = createDependentChannelsData(numDependentChannels, lines);
		return createSortAndAddAction(titles, dependentChannels);
	}

	private List<Line> createIndependentLines(long maxLength, String[] titles)
	{
		List<List<String>> channels = createBaseChannels(maxLength, titles);
		List<String> cartesianProduct = cartesify(channels);
		return toLines(cartesianProduct);
	}

	private ChannelAction createSortAndAddAction(String[] titles, List<FileChannel> dependentChannels)
	{
		AddChannelsAction addAction = new AddChannelsAction(dependentChannels);
		SortAction sortAction = new SortAction(titles.length, true);
		return sortAction.compose(addAction);
	}

	private List<FileChannel> createDependentChannelsData(int numDependentChannels, List<Line> lines)
	{
		return IntStream.range(0, numDependentChannels).
			boxed().
			map(i -> new FileChannel("random" + i, createRandomData(lines.size()))).
			collect(Collectors.toList());
	}

	private List<Line> toLines(List<String> cartesianProduct)
	{
		LineFactory lineFactory = new LineFactory();
		return cartesianProduct.stream().sequential().
			map(lineFactory::next).
			collect(Collectors.toList());
	}

	private List<String> cartesify(List<List<String>> channels)
	{
		return channels.stream().sequential().
			reduce(StringUtils::product).
			get();
	}

	private List<List<String>> createBaseChannels(long maxLength, String[] titles)
	{
		return IntStream.range(0, titles.length).
			boxed().
			map(i -> createRandomData(nextLong(maxLength))).
			collect(Collectors.toList());
	}

	private List<String> createRandomData(long length)
	{
		return LongStream.rangeClosed(1, length).
			boxed().
			parallel().
			map(num -> ThreadLocalRandom.current().nextDouble()).
			map(d -> Double.toString(d)).
			collect(Collectors.toList());
	}

	private long nextLong(long maxLength)
	{
		return ThreadLocalRandom.current().nextLong(maxLength);
	}
}
