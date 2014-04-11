package com.gparser.raw;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * Basic interface for parsing string data into memory.
 * Created by Gilad Ber on 4/5/14.
 */
public interface StringParser
{
	public ParsedFileData parse(Stream<Line> data, String sourceName) throws IOException;
}
