package com.gparser.parsing;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

/**
 * Unit tests for BasicRawParser class
 * Created by Gilad Ber on 5/13/14.
 */
@SuppressWarnings("unused")
public class BasicRawParserTest
{
	private BasicRawParser parser;
	private final String commentIndicator = "%#";
	private final String titleIndicator = "%%";

	@Before
	public void setup()
	{
		ParserMetaData metaData = new ParserMetaData.Builder().
			commentIndicator(commentIndicator).
			titleIndicator(titleIndicator).
			build();

		parser = new BasicRawParser(metaData);
	}

	@Test
	public void testSourceName()
	{
		String sourceName = "C:\\some\\non\\existent\\path\\blah.abc";
		Stream<Line> lines = produceLines(new String[]{"abc"});
		ParsedFileData result = parser.parse(lines, sourceName);

		assertSame(result.getName(), sourceName);
	}

	@Test
	public void testTitleNum()
	{
		String titlesString = titleIndicator + "1 2 3 4 5 6 7 8 9 10";
		Stream<Line> titles = produceLines(new String[]{titlesString, "1 2 3 4 5 6 7 8 9 10"});

		assertEquals(parser.parse(titles, "aaa").getTitles().size(), 10);
	}

	@Test
	public void testCommentNum()
	{
		String[] comments = new String[]{"c1", "c2", "c3", "c4", "c5"};
		List<String> withComment = Arrays.stream(comments).
			map(s -> commentIndicator + s).
			collect(toList());

		assertEquals(parser.parse(produceLines(withComment), "aa").getCommentedLines().size(), 5);
	}

	@Test
	public void testLineOrder()
	{
		String[] data = new String[]{"a b c", "1 2 3", "d e f", "4 5 6", "a l e", "l o l", "w t f"};
		Stream<Line> dataLines = produceLines(data);
		Stream<Line> titles = produceLines(new String[]{titleIndicator + "1 2 3"});
		Stream<Line> lines = Stream.concat(titles, dataLines);

		ParsedFileData pfd = parser.parse(lines, "aa");
		Iterator<String> it = pfd.getDataLines().stream().map(Line::getData).iterator();
		boolean allLinesEqual = Arrays.stream(data).allMatch(s -> s.equals(it.next()));

		assertTrue(allLinesEqual);
	}

	private Stream<Line> produceLines(String[] rawLines)
	{
		LineFactory factory = new LineFactory();
		return Arrays.stream(rawLines).map(factory::next);
	}

	private Stream<Line> produceLines(List<String> rawLines)
	{
		return produceLines(rawLines.toArray(new String[rawLines.size()]));
	}
}

