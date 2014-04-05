package com.gparser.raw;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * Created by Gilad Ber on 4/5/14.
 */
public interface StringParser
{
	public ParsedFileData parse(Stream<String> data, String sourceName) throws IOException;
}
