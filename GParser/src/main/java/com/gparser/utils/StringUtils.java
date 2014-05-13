package com.gparser.utils;

import com.gparser.parsing.Line;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * General utility class for dealing with strings.
 * Created by Gilad Ber on 3/29/14.
 */
public final class StringUtils
{
	public static String join(List<String> strings)
	{
		return strings.stream().collect(Collectors.joining(" "));
	}

	public static Stream<String> spaceStream(String s)
	{
		return Arrays.stream(splitBySpaces(s));
	}

	public static String[] splitBySpaces(String str)
	{
		return splitBySpaces(str != null ? Optional.of(str) : Optional.<String>empty());
	}

	private static String[] splitBySpaces(Optional<String> opt)
	{
		return opt.map(s -> s.split("\\s+")).orElse(new String[0]);
	}

	public static String[] lineToArray(Line line)
	{
		return splitBySpaces(line.getData());
	}

	public static <T> String join(Stream<T> stream, String separator)
	{
		return stream.map(val -> val.toString()).collect(Collectors.joining(separator));
	}

	/**
	 * Returns a list of all possible ordered combinations of the elements of a and b, separated by spaces
	 *
	 * @param a collection of strings
	 * @param b collections of strings
	 * @return A list of strings each containing all possible concatenated elements from a and b, in order.
	 */
	public static List<String> product(Collection<String> a, Collection<String> b)
	{
		return a.stream().
			sequential().
			flatMap(elem -> b.stream().map(elem2 -> elem + " " + elem2)).
			collect(Collectors.toList());
	}

	/**
	 * Checks whether the input lines are equal, comparing the first numParams elements of each line.
	 *
	 * @param line1     first line to compare
	 * @param line2     second line to compare
	 * @param numParams number of elements to check in each line
	 * @return whether the two lines' first numParams elements are equal or not, given that they have the same number of
	 * elements and both at least have numParams elements.
	 * @throws java.lang.NumberFormatException if any of the compared elements can not be parsed as a double.
	 */
	public static boolean areLinesEqual(String line1, String line2, int numParams) throws NumberFormatException
	{
		String[] split1 = StringUtils.splitBySpaces(line1);
		String[] split2 = StringUtils.splitBySpaces(line2);

		if ((split1.length < numParams || split2.length < numParams) && split1.length != split2.length)
		{
			return false;
		}

		Iterator<Double> it = Arrays.stream(split2).map(Double::parseDouble).iterator();
		return Arrays.stream(split1).
			limit(numParams).
			mapToDouble(Double::parseDouble).
			allMatch(d -> d == it.next());
	}
}
