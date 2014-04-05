package com.gparser.raw;

import com.gparser.utils.StringUtils;

/**
 * Created by Gilad Ber on 4/4/14.
 */
public class ParsedFileDataBuilder
{
	public ParsedFileData build(ParserModel model, String sourceName)
	{
		return new ParsedFileData.Builder().
			commentedLines(model.getCommentLines()).
			dataLines(model.getData()).
			fileName(sourceName).
			metaData(model.getMetaData()).
			titles(StringUtils.splitBySpaces(model.getTitleLine())).
			build();
	}
}
