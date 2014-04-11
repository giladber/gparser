package com.gparser.parsing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents the structure of data after being parsed into memory.
 * Created by Gilad Ber on 4/4/14.
 */
public class ParsedFileData
{
	private final String name;
	private final ParserMetaData metaData;
	private final List<String> titles;
	private final List<String> commentedLines;
	private final List<Line> dataLines;

	private ParsedFileData(String fileName, ParserMetaData metaData, List<String> titles, List<String> commentedLines, List<Line> dataLines)
	{
		this.commentedLines = commentedLines;
		this.dataLines = dataLines;
		this.titles = titles;
		this.metaData = metaData;
		this.name = fileName;
	}

	public String getName()
	{
		return name;
	}

	public ParserMetaData getMetaData()
	{
		return metaData;
	}

	public List<String> getTitles()
	{
		return titles;
	}

	public List<String> getCommentedLines()
	{
		return commentedLines;
	}

	public List<Line> getDataLines()
	{
		return dataLines;
	}

	public static final class Builder
	{
		private String name;
		private ParserMetaData metaData = new ParserMetaData.Builder().build();
		private List<String> titles = Collections.emptyList();
		private List<String> commentedLines = Collections.emptyList();
		private List<Line> dataLines = Collections.emptyList();

		public Builder fileName(String name)
		{
			this.name = name;
			return this;
		}

		public Builder metaData(ParserMetaData data)
		{
			this.metaData = data;
			return this;
		}

		public Builder titles(List<String> titles)
		{
			this.titles = titles;
			return this;
		}

		public Builder titles(String[] titles)
		{
			return this.titles(Arrays.asList(titles));
		}

		public Builder commentedLines(List<String> lines)
		{
			this.commentedLines = lines;
			return this;
		}

		public Builder dataLines(List<Line> lines)
		{
			this.dataLines = lines;
			return this;
		}

		public ParsedFileData build()
		{
			return new ParsedFileData(name, metaData, titles, commentedLines, dataLines);
		}

	}

	@Override
	public String toString()
	{
		return "ParsedFileData{" +
			"name='" + name + '\'' +
			", metaData=" + metaData +
			", titles=" + titles +
			", commentedLines=" + commentedLines +
			", dataLines=" + dataLines +
			'}';
	}
}
