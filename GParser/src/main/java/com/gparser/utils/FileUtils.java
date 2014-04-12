package com.gparser.utils;

import com.gparser.parsing.Line;
import com.gparser.parsing.LineFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

/**
 * Utility class for files handling.
 * Created by Gilad Ber on 4/12/14.
 */
public class FileUtils
{

	public static Stream<Line> toLines(File source) throws IOException
	{
		LineFactory lineFactory = new LineFactory();
		return Files.lines(source.toPath()).map(lineFactory::next);
	}
}
