package com.gparser.exec;

import com.gparser.actions.ChannelAction;
import com.gparser.actions.KHCompleteAction;
import com.gparser.files.ChannelFileDataWriter;
import com.gparser.parsing.BasicRawParser;
import com.gparser.parsing.ParserMetaData;
import com.gparser.validation.Validator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Main entry method for performing any parsing plans.
 * Created by Gilad Ber on 4/12/14.
 */
public class GParserMain
{
	public static void main(String[] args) throws IOException
	{
		execute(args);
	}

	private static void execute(String[] args) throws IOException
	{
		if (args.length < 4)
		{
			System.out.println("Usage: GParserMain.java <input_file_name> <comment_indicator> <title_indicator> <output_file>");
			return;
		}

		long s = System.nanoTime();
		ParserMetaData metaData = new ParserMetaData.Builder().commentIndicator(args[1]).titleIndicator(args[2]).build();
		BasicRawParser parser = new BasicRawParser(metaData);

		long s1 = System.nanoTime();
		System.out.println("Time to parse: " + nano2ms(s1 - s) + "ms");

		new GParserExecutor(parser, Collections.<Validator>emptyList(), Arrays.<ChannelAction>asList(new KHCompleteAction()), new ChannelFileDataWriter()).
			execute(new File(args[0]), new File(args[3]));

		long s2 = System.nanoTime();

		System.out.println("Time to execute: " + nano2ms(s2 - s1) + "ms");
	}

	private static double nano2ms(double nano)
	{
		return nano / 1E6;
	}
}
