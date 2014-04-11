package com.gparser.utils;

import com.gparser.raw.Line;

import java.util.Arrays;
import java.util.List;
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
		return str.split("\\s+");
	}

	public static String[] lineToArray(Line line)
	{
		return splitBySpaces(line.getData());
	}

	public static <T> String join(Stream<T> stream, String separator)
	{
		StringBuilder builder = new StringBuilder();
		stream.forEachOrdered(val -> builder.append(val.toString()).append(separator));

		return builder.toString();
	}
}