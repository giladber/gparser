package com.gparser.exec;

import com.gparser.actions.ChannelAction;
import com.gparser.actions.CompleteAction;
import com.gparser.actions.SortAction;
import com.gparser.files.ChannelFileDataWriter;
import com.gparser.parsing.BasicRawParser;
import com.gparser.parsing.ParserMetaData;
import com.gparser.validation.Validator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

		List<ChannelAction> actions = new ArrayList<>();
		actions.add(new SortAction(3, true));

		List<ChannelAction> completeActions = IntStream.range(20, 30).
			mapToObj(x -> new CompleteAction(1, x)).
			collect(Collectors.toList());

		actions.addAll(completeActions);

		new GParserExecutor(parser, Collections.<Validator>emptyList(), actions, new ChannelFileDataWriter()).
			execute(new File(args[0]), new File(args[3]));

		long s2 = System.nanoTime();

		System.out.println("Time to execute: " + nano2ms(s2 - s1) + "ms");
	}

	private static double nano2ms(double nano)
	{
		return nano / 1E6;
	}
}
