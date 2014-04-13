package com.gparser.actions;

import com.gparser.files.ChannelFileData;
import com.gparser.parsing.Line;
import com.gparser.utils.StringUtils;

import java.util.*;
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

	public SortAction(int numIndependentChannels, boolean ascending)
	{
		this.numIndependentChannels = numIndependentChannels;
		this.ascending = ascending;
	}

	@Override
	public ChannelFileData apply(ChannelFileData data)
	{
		final Comparator<Line> rowComparator = this::compareLines;

		List<Line> sortedRowData = data.getRowData().stream().
			sorted(rowComparator).
			collect(Collectors.toList());
		return ChannelFileData.create(sortedRowData, data.getTitles(), data.getComments());
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
		Optional<Double> intermediateResult = comparisons.filter(val -> val != 0).map(val -> sortTypeFactor * val).findFirst();
		result = intermediateResult.isPresent() ? (int) ((double) intermediateResult.get()) : result;

		return result;
	}

}
