package com.gparser.tests.parsing;

import com.gparser.parsing.*;
import com.gparser.utils.FileUtils;
import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * Performance test for parsing a large file into memory.
 * Created by Gilad Ber on 4/12/14.
 */
@State(Scope.Thread)
@SuppressWarnings("unused")
public class ParsingPerformanceTest
{

	private StringParser parser;
	private File source;
	private Stream<Line> lineStream;

	@Setup(Level.Trial)
	public void init()
	{
		String commentIndicator = "%#";
		String titleIndicator = "%%";
		ParserMetaData metaData = new ParserMetaData.Builder().
			commentIndicator(commentIndicator).titleIndicator(titleIndicator).build();

		this.parser = new BasicRawParser(metaData);
		this.source = new File("c:/parser/result.txt");
	}

	@Setup(Level.Invocation)
	public void initStream()
	{
		try
		{
			this.lineStream = FileUtils.toLines(source);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@GenerateMicroBenchmark
	public ParsedFileData parse() throws IOException
	{
		return parser.parse(this.lineStream, "test data");
	}
}
