package com.gparser.actions;

import com.gparser.files.ChannelFileData;
import com.gparser.parsing.Line;
import com.gparser.utils.StringUtils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * An action to filter elements of a channeled file's data based on an input predicate.
 * The filtering will be done only on the input channel (identified by index).
 * Created by Gilad Ber on 4/5/14.
 */
public class FilterAction implements ChannelAction
{
	private final int numChannelToFilter;
	private final Predicate<? super String> filter;

	public FilterAction(int numChannelToFilter, Predicate<? super String> filter)
	{
		this.numChannelToFilter = numChannelToFilter;
		this.filter = filter;
	}

	@Override
	public ChannelFileData apply(ChannelFileData data)
	{
		System.out.println("raw data: " + data.getRowData());
		List<Line> filtered = data.getRowData().
			stream().
			filter(row -> filter.test(StringUtils.splitBySpaces(row.getData())[numChannelToFilter - 1])).
			collect(Collectors.toList());

		System.out.println(filtered);

		return ChannelFileData.create(filtered, data.getTitles(), data.getComments());
	}
}
