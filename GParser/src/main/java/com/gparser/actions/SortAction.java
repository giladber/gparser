package com.gparser.actions;

import com.gparser.files.ChannelFileData;
import com.gparser.parsing.Line;
import com.gparser.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Action to sort a channel file data by its undependent types.
 * Created by Gilad Ber on 4/4/14.
 */
public class SortAction implements ChannelAction
{
	private final int numIndependentChannels;
	private final boolean ascending;

	private static final Logger logger = LoggerFactory.getLogger(SortAction.class);

	public SortAction(int numIndependentChannels, boolean ascending)
	{
		this.numIndependentChannels = numIndependentChannels;
		this.ascending = ascending;
	}

	@Override
	public ChannelFileData apply(ChannelFileData data)
	{
		List<Line> sortedRowData = data.getRowData().stream().
			sorted(this::compareLines).
			collect(Collectors.toList());

		ChannelFileData channelFileData = ChannelFileData.create(sortedRowData, data.getTitles(), data.getComments());
		logger.info("Finished applying sort action to input");
		return channelFileData;
	}

	private int compareLines(Line s1, Line s2)
	{
		String[] split2 = StringUtils.splitBySpaces(s2.getData());

		Iterator<String> split2Iterator = Arrays.stream(split2).iterator();
		Stream<Double> comparisons = StringUtils.spaceStream(s1.getData()).
			sequential().
			limit(numIndependentChannels).
			map(val -> Double.parseDouble(val) - Double.parseDouble(split2Iterator.next()));

		int result = 0;
		int sortTypeFactor = ascending ? 1 : -1;
		Optional<Integer> intermediateResult = comparisons.
			filter(val -> val != 0).
			map(val -> val > 0 ? 1 : -1).
			map(val -> sortTypeFactor * val).
			findFirst();

		result = intermediateResult.orElse(result);

		if (result == 0)
		{
			logger.warn("Result should not be 0! \nline1 = {} \nline2 = {}", s1, s2);
		}

		return result;
	}

}
