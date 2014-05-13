package com.gparser.utils;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.gparser.utils.StringUtils.*;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the StringUtils class.
 * Created by Gilad Ber on 5/13/14.
 */
public class StringUtilsTest
{

	private static String[] words;

	@BeforeClass
	public static void makeWords()
	{
		words = new String[]{"1", "2", "3", "4", "5"};
	}

	@Test
	public void testJoinStream()
	{
		Stream<String> stream = Stream.of(words);
		String separator = ",";
		String joined = join(stream, separator);

		String[] split = joined.split(separator);
		AtomicInteger counter = new AtomicInteger();
		assertEquals(split.length, words.length);
		stream(words).forEach(s -> assertEquals(s, split[counter.getAndIncrement()]));
	}

	@Test
	public void testJoinList()
	{
		List<String> list = asList(words);
		String joined = join(list);

		String[] split = joined.split(" ");
		AtomicInteger counter = new AtomicInteger();
		assertEquals(split.length, words.length);
		stream(words).forEach(s -> assertEquals(s, split[counter.getAndIncrement()]));
	}

	@Test
	public void testSplitBySpaces()
	{
		List<String> wordsList = asList(words);
		String withSpaces = join(wordsList);
		String[] split = splitBySpaces(withSpaces);
		Iterator<String> it = wordsList.iterator();

		assertEquals(split.length, words.length);
		stream(split).forEach(s -> assertEquals(s, it.next()));
		assertArrayEquals(splitBySpaces(null), new String[0]);
	}

	@Test
	public void testProduct()
	{
		String[] otherWords = new String[]{"a", "b", "c"};
		String[] noWords = new String[0];
		String[] emptyWord = new String[]{""};

		List<String> wordsList = asList(words);
		List<String> regularProduct = product(asList(otherWords), wordsList);
		List<String> emptyProduct = product(wordsList, asList(noWords));
		List<String> oneProduct = product(wordsList, asList(emptyWord));

		Iterator<String> it1 = wordsList.iterator();
		oneProduct.stream().forEach(s -> assertEquals(s, it1.next() + " "));
		assertEquals(emptyProduct, Collections.<String>emptyList());
		assertEquals(regularProduct.size(), otherWords.length * words.length);
		stream(otherWords).allMatch(s -> wordsList.stream().allMatch(r -> regularProduct.contains(s + " " + r)));
	}
}
