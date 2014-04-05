package com.gparser.raw.actions;

import com.gparser.raw.ChannelFileData;
import com.gparser.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Gilad Ber on 4/5/14.
 */
public class CompleteAction implements ChannelAction
{
	private final int channelNumToComplete;
	private final long completeValue;

	public CompleteAction(int channelNumToComplete, long completeValue)
	{
		this.channelNumToComplete = channelNumToComplete - 1;
		this.completeValue = completeValue;
	}

	@Override
	public ChannelFileData perform(ChannelFileData data)
	{
		List<String> rows = data.getRowData();

		Optional<Double> closestValue = rows.stream().
			filter(row -> !row.isEmpty()).
			map(s -> Double.parseDouble(StringUtils.splitBySpaces(s)[channelNumToComplete])).
			max((d1, d2) -> (int) (Math.abs(completeValue - d2) - Math.abs(completeValue - d1)));

		if (closestValue.get() == (double) completeValue)
		{
			return data;
		}

		List<String> completedRows = rows.stream().
			filter(row -> Double.parseDouble(StringUtils.splitBySpaces(row)[channelNumToComplete]) == closestValue.get()).
			map(row -> changeLineParameter(row, channelNumToComplete, Long.toString(completeValue))).
			collect(Collectors.toList());

		completedRows.addAll(rows);
		return ChannelFileData.create(completedRows, data.getTitles(), data.getComments());
	}

	private String changeLineParameter(String oldValue, int numParameter, String newValue)
	{
		String[] split = StringUtils.splitBySpaces(oldValue);
		if (split.length >= numParameter)
		{
			split[numParameter] = newValue;
		}

		return Arrays.stream(split).collect(Collectors.joining(" "));
	}
}
