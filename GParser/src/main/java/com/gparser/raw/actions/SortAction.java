package com.gparser.raw.actions;

import com.gparser.raw.ChannelFileData;
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

	public SortAction(int numUndependentChannels, boolean ascending)
	{
		this.numIndependentChannels = numUndependentChannels;
		this.ascending = ascending;
	}

	@Override
	public ChannelFileData perform(ChannelFileData data)
	{
		final Comparator<String> rowComparator = this::compareLines;

		List<String> sortedRowData = data.getRowData().stream().sorted(rowComparator).collect(Collectors.toList());
		return ChannelFileData.create(sortedRowData, data.getTitles(), data.getComments());
	}

	private int compareLines(String s1, String s2)
	{
		String[] split2 = StringUtils.splitBySpaces(s2);

		Iterator<String> split2Iterator = Arrays.stream(split2).iterator();
		Stream<Double> comparisons = StringUtils.spaceStream(s1).
			limit(numIndependentChannels).
			map(val -> Double.parseDouble(val) - Double.parseDouble(split2Iterator.next()));

		int result = 0;
		int sortTypeFactor = ascending ? 1 : -1;
		Optional<Double> intermediateResult = comparisons.filter(val -> val != 0).map(val -> sortTypeFactor * val).findFirst();
		result = intermediateResult.isPresent() ? (int) ((double) intermediateResult.get()) : result;

		return result;
	}

}
