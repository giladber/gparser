package com.gparser.exec;

import com.gparser.actions.KHCompleteAction;
import com.gparser.actions.KHCompleteActionInput;
import com.gparser.files.ChannelFileDataWriter;
import com.gparser.parsing.BasicRawParser;
import com.gparser.parsing.ParserMetaData;

import java.io.File;
import java.io.IOException;
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
			System.out.println("Usage: GParserMain.java <input_file_name> <comment_indicator> <title_indicator> <output_file> [mode:1,2]");
			return;
		}

		long s = System.nanoTime();
		ParserMetaData metaData = new ParserMetaData.Builder().commentIndicator(args[1]).titleIndicator(args[2]).build();
		BasicRawParser parser = new BasicRawParser(metaData);

		long s1 = System.nanoTime();
		System.out.println("Time to parse: " + nano2ms(s1 - s) + "ms");

		double[][] delBounds = new double[][]{{0, 20}, {-180, 180}, {-180, 180}};
		double[][] xcgBounds = new double[][]{{0, 20}, {-180, 180}, {0, 100}};
		double[][] completionValues = args.length >= 5 && args[4].equals("2") ? xcgBounds : delBounds;
		KHCompleteAction khAction = new KHCompleteAction(new KHCompleteActionInput(3, completionValues));

		new GParserExecutor(parser, Collections.emptyList(), Collections.singletonList(khAction), new ChannelFileDataWriter()).
			execute(new File(args[0]), new File(args[3]));

		long s2 = System.nanoTime();

		System.out.println("Time to execute: " + nano2ms(s2 - s1) + "ms");
	}

	private static double nano2ms(double nano)
	{
		return nano / 1E6;
	}
}
