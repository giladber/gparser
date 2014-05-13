package com.gparser.utils;

import com.gparser.parsing.Line;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
		StringBuilder builder = new StringBuilder();
		stream.forEachOrdered(val -> builder.append(val.toString()).append(separator));

		return builder.toString();
	}

	public static List<String> product(Collection<String> a, Collection<String> b)
	{
		return a.stream().
			sequential().
			flatMap(elem -> b.stream().map(elem2 -> elem + " " + elem2)).
			collect(Collectors.toList());
	}

	public static boolean compareRowData(String line1, String line2, int numParams)
	{
		String[] split1 = StringUtils.splitBySpaces(line1);
		String[] split2 = StringUtils.splitBySpaces(line2);

		if ((split1.length < numParams || split2.length < numParams) && split1.length != split2.length)
		{
			return false;
		}

		for (int i = 0; i < numParams; i++)
		{
			if (Double.parseDouble(split1[i]) != Double.parseDouble(split2[i]))
			{
				return false;
			}
		}

		return true;
	}
}
