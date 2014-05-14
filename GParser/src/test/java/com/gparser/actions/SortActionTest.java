package com.gparser.actions;

import com.gparser.files.ChannelFileData;
import com.gparser.files.FileChannel;
import com.gparser.parsing.Line;
import com.gparser.tests.mock.ChannelFileDataFactory;
import com.gparser.utils.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.gparser.tests.ResourceUtils.getResourceLocation;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

/**
 * Unit tests for SortAction class.
 * Created by Gilad Ber on 5/14/2014.
 */
public class SortActionTest
{

	private static ChannelFileData mediumFileData;
	private static ChannelFileData largeFileData;
	private static ChannelFileData duplicateLinesData;
	private static ChannelFileData nonCartesianData;

	@BeforeClass
	public static void setup() throws IOException
	{
		ChannelFileDataFactory factory = new ChannelFileDataFactory();
		mediumFileData = factory.build(getResourceLocation("result_medium.txt"), "%#", "%%");
		largeFileData = factory.build(getResourceLocation("result_large.txt"), "%#", "%%");
		duplicateLinesData = factory.build(getResourceLocation("result_duplicate_lines.txt"), "%#", "%%");
		nonCartesianData = factory.build(getResourceLocation("result_large_non_cartesian.txt"), "%#", "%%");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testZeroChannels()
	{
		new SortAction(0, true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeChannels()
	{
		new SortAction(-1, true);
	}

	@Test
	public void testDistinctSorted()
	{
		ChannelFileData sorted = new SortAction(3, true).apply(mediumFileData);
		List<List<Double>> distinctChannelData = sorted.getChannels().stream().
			map(FileChannel::getData).
			limit(3).
			map(list -> list.stream().map(Double::parseDouble).distinct().collect(toList())).
			collect(toList());

		List<List<Double>> other = distinctChannelData.stream().
			map(list -> copy(list)).
			map(list -> list.stream().sorted().collect(toList())).
			collect(toList());

		Iterator<List<Double>> iter = distinctChannelData.iterator();
		other.stream().forEach(list -> assertEquals(list, iter.next()));
	}

	/*
	 * The purpose of this test is to make sure that when rows are reordered according to their independent channel data,
	 * the data from the dependent channels is also copied along with the row.
	 */
	@Test
	public void testRowsCopiedFully()
	{
		ChannelFileData sorted = new SortAction(3, true).apply(mediumFileData);
		List<List<String>> sortedRowsData = getRawLineData(sorted);
		List<List<String>> prevRowsData = getRawLineData(mediumFileData);
		assertTrue(listsHaveSameElements(sortedRowsData, prevRowsData));
	}

	private List<List<String>> getRawLineData(ChannelFileData fileData)
	{
		return fileData.getRowData().stream().
			map(Line::getData).
			map(StringUtils::splitBySpaces).
			map(arr -> Arrays.asList(arr)).
			collect(toList());
	}

	private List<List<Double>> getDoubleLineData(ChannelFileData fileData)
	{
		return getRawLineData(fileData).stream().
			map(list -> list.stream().
				limit(list.size() - 1).
				map(Double::parseDouble).
				collect(Collectors.<Double>toList())).
			collect(toList());
	}

	/*
	 * Checking that both lists have the same elements in this way is fine, for lines of a ChannelFileData object,
	 * since they can not possibly have any duplicate elements (part of the ChannelFileData contract).
	 */
	private <T> boolean listsHaveSameElements(List<T> list1, List<T> list2)
	{
		return list1.containsAll(list2) && list2.containsAll(list1);
	}

	private <T> List<T> copy(List<T> from)
	{
		List<T> list = new ArrayList<>();
		list.addAll(from);
		return list;
	}

	@Test
	public void testSortedMedium()
	{
		ChannelFileData sorted = new SortAction(3, true).apply(mediumFileData);
		assertTrue(isSorted(sorted, true));

		ChannelFileData sortedDesc = new SortAction(3, false).apply(mediumFileData);
		assertTrue(isSorted(sortedDesc, false));
	}

	@Test
	public void testDescVsAscFailure()
	{
		ChannelFileData sortedAsc = new SortAction(3, true).apply(mediumFileData);
		assertFalse(isSorted(sortedAsc, false));
	}

	@Test
	public void testSortedNonCartesian()
	{
		ChannelFileData sorted = new SortAction(3, true).apply(nonCartesianData);
		assertTrue(isSorted(sorted, true));

		ChannelFileData sortedDesc = new SortAction(3, false).apply(nonCartesianData);
		assertTrue(isSorted(sortedDesc, false));
	}

	@Test
	public void testSortedLarge()
	{
		ChannelFileData sorted = new SortAction(3, true).apply(largeFileData);
		assertTrue(isSorted(sorted, true));

		ChannelFileData sortedDesc = new SortAction(3, false).apply(largeFileData);
		assertTrue(isSorted(sortedDesc, false));
	}

	@Test
	public void testSortedDuplicateLines()
	{
		ChannelFileData unsorted = new SortAction(3, true).apply(duplicateLinesData);
		List<List<Double>> doublesLineData = getDoubleLineData(unsorted);
		Iterator<List<Double>> it = doublesLineData.iterator();
		it.next();

		int result = doublesLineData.stream().
			limit(doublesLineData.size() - 1).
			map(list -> compareDoubleLists(list, it.next())).
			filter(x -> x == 0).
			findAny().orElse(-1);

		assertEquals(result, 0);
	}

	private boolean isSorted(ChannelFileData data, boolean ascending)
	{
		List<List<Double>> doublesLineData = getDoubleLineData(data);
		Iterator<List<Double>> it = doublesLineData.iterator();
		it.next();

		return doublesLineData.stream().
			limit(doublesLineData.size() - 1).
			map(list -> compareDoubleLists(list, it.next())).
			allMatch(x -> ascending ? x < 0 : x > 0);
	}

	private int compareDoubleLists(List<Double> list1, List<Double> list2)
	{
		Iterator<Double> it = list2.iterator();
		return list1.stream().
			map(x -> x - it.next()).
			filter(x -> x != 0).
			map(x -> x < 0 ? -1 : 1). //to prevent int rounding to 0 when was != 0
			findFirst().
			orElse(0);
	}
}
