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
 * Created by Gilad Ber on 4/5/14.
 */
public class CompleteAction implements ChannelAction
{
	private final int channelNumToComplete;
	private final double completeValue;
	private static final Logger logger = LoggerFactory.getLogger(CompleteAction.class);

	public CompleteAction(int channelNumToComplete, double completeValue)
	{
		this.channelNumToComplete = channelNumToComplete - 1;
		this.completeValue = completeValue;
	}

	@Override
	public ChannelFileData apply(ChannelFileData data)
	{
		List<Line> rows = data.getRowData();

		Optional<Double> closestValue = rows.stream().
			map(Line::getData).
			filter(row -> !row.isEmpty()).
			map(s -> Double.parseDouble(StringUtils.splitBySpaces(s)[channelNumToComplete])).
			max((d1, d2) -> (int) (Math.abs(completeValue - d2) - Math.abs(completeValue - d1)));

		if (closestValue.get() == completeValue)
		{
			return data;
		}

		List<Line> completedRows = rows.stream().
			filter(row -> Double.parseDouble(StringUtils.splitBySpaces(row.getData())[channelNumToComplete]) == closestValue.get()).
			map(row -> changeLineParameter(row, channelNumToComplete, Double.toString(completeValue))).
			collect(Collectors.toList());

		completedRows.addAll(rows);
		ChannelFileData result = ChannelFileData.create(completedRows, data.getTitles(), data.getComments());
		logger.info("Applied complete action for channel {} with completion value {}", channelNumToComplete + 1, completeValue);
		return result;
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
