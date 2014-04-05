package com.gparser.raw;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * Created by Gilad Ber on 4/4/14.
 */
public class BasicRawParser
{
	private final ParserModel model;
	private final ParserMetaData metaData;
	private final ParsedFileDataBuilder builder = new ParsedFileDataBuilder();

	public BasicRawParser(ParserMetaData metaData)
	{
		this.metaData = metaData;
		this.model = new ParserModel(metaData);
	}

	public ParsedFileData parse(Stream<String> linesStream, String sourceName) throws IOException
	{
		linesStream.
			forEachOrdered(s -> {
				String trimmed = s.trim();
				if (trimmed.startsWith(metaData.commentIndicator))
				{
					model.addComment(trimmed);
				}
				else if (trimmed.startsWith(metaData.titleIndicator))
				{
					model.setTitleLine(trimmed.replace(metaData.titleIndicator, ""));
				}
				else
				{
					model.addDataLine(trimmed);
				}
			});
		return builder.build(model, sourceName);
	}
}