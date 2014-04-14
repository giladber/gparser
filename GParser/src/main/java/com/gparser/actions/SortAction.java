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
			limit(numIndependentChannels).
			map(val -> Double.parseDouble(val) - Double.parseDouble(split2Iterator.next()));

		int result = 0;
		int sortTypeFactor = ascending ? 1 : -1;
		Optional<Double> intermediateResult = comparisons.
			filter(val -> val != 0).
			map(val -> sortTypeFactor * val).
			findFirst();

		result = intermediateResult.map(res -> (int) res.doubleValue()).orElse(result);

		return result;
	}

}
