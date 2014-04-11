package com.gparser.raw;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * Basic string parser.
 * Created by Gilad Ber on 4/4/14.
 */
public class BasicRawParser implements StringParser
{
	private final ParserModel model;
	private final ParserMetaData metaData;
	private final ParsedFileDataBuilder builder = new ParsedFileDataBuilder();

	public BasicRawParser(ParserMetaData metaData)
	{
		this.metaData = metaData;
		this.model = new ParserModel(metaData);
	}

	@Override
	/**
	 * Parses the data from the input stream into a ParsedFileData object.
	 * Since this method accepts any stream and does not perform any intermediate actions on the input stream,
	 * it is the caller's responsibility to make sure this is not an infinite stream, otherwise the method will
	 * never terminate.
	 */
	public ParsedFileData parse(Stream<Line> linesStream, String sourceName) throws IOException
	{
		linesStream.
			forEachOrdered(line -> {
				String trimmed = line.getData().trim();
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
					model.addDataLine(new Line(line.getIndex(), trimmed));
				}
			});
		return builder.build(model, sourceName);
	}
}