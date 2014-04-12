package com.gparser.parsing;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic model for a string parser.
 * Created by Gilad Ber on 4/4/14.
 */
public class ParserModel
{
	private final ParserMetaData metaData;
	private final List<String> commentLines = new ArrayList<>();
	private String titleLine;
	private final List<Line> data = new ArrayList<>();

	public ParserModel(ParserMetaData metaData)
	{
		this.metaData = metaData;
	}

	public void addComment(String comment)
	{
		this.commentLines.add(comment);
	}

	public void setTitleLine(String titleLine)
	{
		this.titleLine = titleLine;
	}

	public void addDataLine(Line dataLine)
	{
		this.data.add(dataLine);
	}

	public ParserMetaData getMetaData()
	{
		return metaData;
	}

	public List<String> getCommentLines()
	{
		return commentLines;
	}

	public String getTitleLine()
	{
		return titleLine;
	}

	public List<Line> getData()
	{
		return data;
	}
}
