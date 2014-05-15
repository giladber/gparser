package com.gparser.actions;

import com.gparser.files.ChannelFileData;
import com.gparser.parsing.Line;
import com.gparser.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A channel action for completing data in the channel file.
 * The data returned by a complete action is sorted.
 * Created by Gilad Ber on 4/5/14.
 */
public class CompleteAction implements ChannelAction
{
	private final int channelNumToComplete;
	private final int numIndependentChannels;
	private final double completeValue;
	private static final Logger logger = LoggerFactory.getLogger(CompleteAction.class);

	public CompleteAction(int channelNumToComplete, double completeValue, int numIndependentChannels)
	{
		if (channelNumToComplete < 1)
		{
			throw new IllegalArgumentException("Channel # to complete must be > 0, is: " + channelNumToComplete);
		}
		if (numIndependentChannels < channelNumToComplete)
		{
			throw new IllegalArgumentException("Number of independent channels must be >= " + (channelNumToComplete + 1) + ", is: " + numIndependentChannels);
		}

		this.channelNumToComplete = channelNumToComplete - 1;
		this.completeValue = completeValue;
		this.numIndependentChannels = numIndependentChannels;
	}

	@Override
	public ChannelFileData apply(ChannelFileData data)
	{
		validateChannelNum(data);
		List<Line> rows = data.getRowData();
		Optional<Double> closestValue = findClosestValue(rows);
		if (completionNotNeeded(closestValue))
		{
			return data;
		}

		List<Line> completedRows = completeRows(rows, closestValue);

		completedRows.addAll(rows);
		ChannelFileData result = ChannelFileData.create(completedRows, data.getTitles(), data.getComments());
		logger.info("Applied complete action for channel {} with completion value {}", channelNumToComplete + 1, completeValue);
		return new SortAction(numIndependentChannels, true).apply(result);
	}

	private void validateChannelNum(ChannelFileData data)
	{
		if (data.getChannels().size() <= channelNumToComplete)
		{
			throw new IllegalArgumentException("Attempted to complete channel " + channelNumToComplete + " but input has " +
				data.getChannels().size() + " channels");
		}
	}

	private boolean completionNotNeeded(Optional<Double> closestValue)
	{
		return closestValue.get() == completeValue;
	}

	private List<Line> completeRows(List<Line> rows, Optional<Double> closestValue)
	{
		return rows.stream().
			filter(row -> Double.parseDouble(StringUtils.splitBySpaces(row.getData())[channelNumToComplete]) == closestValue.get()).
			map(row -> changeLineParameter(row, channelNumToComplete, Double.toString(completeValue))).
			collect(Collectors.toList());
	}

	private Optional<Double> findClosestValue(List<Line> rows)
	{
		return rows.stream().
			map(Line::getData).
			filter(row -> !row.isEmpty()).
			map(s -> Double.parseDouble(StringUtils.splitBySpaces(s)[channelNumToComplete])).
			max((d1, d2) -> (int) (Math.abs(completeValue - d2) - Math.abs(completeValue - d1)));
	}

	private Line changeLineParameter(Line line, int numParameter, String newValue)
	{
		String[] split = StringUtils.splitBySpaces(line.getData());
		if (split.length >= numParameter)
		{
			split[numParameter] = newValue;
		}

		return new Line(line.getIndex(), StringUtils.join(Arrays.asList(split)));
	}
}
