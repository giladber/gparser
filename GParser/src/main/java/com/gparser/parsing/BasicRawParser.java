package com.gparser.parsing;

import com.gparser.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger logger = LoggerFactory.getLogger(BasicRawParser.class);

	public BasicRawParser(ParserMetaData metaData)
	{
		this.metaData = metaData;
		this.model = new ParserModel(metaData);
	}

	@Override
	/**
	 * Parses the data from the input stream into a ParsedFileData object.
	 * Since this method accepts any stream and does not apply any intermediate actions on the input stream,
	 * it is the caller's responsibility to make sure this is not an infinite stream, otherwise the method will
	 * never terminate.
	 */
	public ParsedFileData parse(Stream<Line> linesStream, String sourceName) throws IOException
	{
		linesStream.
			map(line -> new Line(line.getIndex(), line.getData().trim())).
			forEachOrdered(line -> {
				String data = line.getData();
				if (data.startsWith(metaData.commentIndicator))
				{
					model.addComment(data);
				}
				else if (data.startsWith(metaData.titleIndicator))
				{
					model.setTitleLine(data.replace(metaData.titleIndicator, ""));
				}
				else
				{
					model.addDataLine(new Line(line.getIndex(), data));
				}
			});

		logger.info("Parsed file {} to memory. Result has {} commented lines, {} channels and {} data lines", sourceName, model.getCommentLines().size(), StringUtils.splitBySpaces(model.getTitleLine()).length, model.getData().size());
		return builder.build(model, sourceName);
	}
}