package com.gparser.raw.actions;

import com.gparser.raw.ChannelFileData;
import com.gparser.raw.Line;
import com.gparser.utils.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Gilad Ber on 4/5/14.
 */
public class FilterAction implements ChannelAction
{
	private final int numChannelToFilter;
	private final Function<String, Boolean> filter;

	public FilterAction(int numChannelToFilter, Function<String, Boolean> filter)
	{
		this.numChannelToFilter = numChannelToFilter;
		this.filter = filter;
	}

	@Override
	public ChannelFileData perform(ChannelFileData data)
	{
		System.out.println("raw data: " + data.getRowData());
		List<Line> filtered = data.getRowData().
			stream().
			filter(row -> filter.apply(StringUtils.splitBySpaces(row.getData())[numChannelToFilter - 1])).
			collect(Collectors.toList());

		System.out.println(filtered);

		return ChannelFileData.create(filtered, data.getTitles(), data.getComments());
	}
}
