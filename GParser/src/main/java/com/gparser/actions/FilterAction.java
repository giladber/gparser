package com.gparser.actions;

import com.gparser.files.ChannelFileData;
import com.gparser.parsing.Line;
import com.gparser.parsing.LineFactory;
import com.gparser.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * An action to filter elements of a channeled file's data based on an input predicate.
 * This action will remove all lines which do not match the input filter on the input channel number.
 * Created by Gilad Ber on 4/5/14.
 */
public class FilterAction implements ChannelAction
{
	private final int numChannelToFilter;
	private final Predicate<? super String> filter;
	private static final Logger logger = LoggerFactory.getLogger(FilterAction.class);

	public FilterAction(int numChannelToFilter, Predicate<? super String> filter)
	{
		Objects.requireNonNull(filter);
		if (numChannelToFilter < 1)
		{
			throw new IllegalArgumentException("Channel number to filter must be > 0, is: " + numChannelToFilter);
		}

		this.numChannelToFilter = numChannelToFilter;
		this.filter = filter;
	}

	@Override
	public ChannelFileData apply(ChannelFileData data)
	{
		if (numChannelToFilter > data.getChannels().size())
		{
			throw new IllegalArgumentException("Input data has only " + data.getChannels().size() + " channels, action expected at least " + numChannelToFilter);
		}

		LineFactory factory = new LineFactory();
		List<Line> filtered = data.getRowData().
			stream().
			map(Line::getData).
			filter(s -> filter.test(StringUtils.splitBySpaces(s)[numChannelToFilter - 1])).
			map(factory::next).
			collect(Collectors.toList());

		ChannelFileData result = ChannelFileData.create(filtered, data.getTitles(), data.getComments());
		logger.info("Applied filter action on channel #{}", numChannelToFilter);
		return result;
	}
}
