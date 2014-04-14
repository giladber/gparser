package com.gparser.exec;

import com.gparser.actions.KHCompleteAction;
import com.gparser.actions.KHCompleteActionInput;
import com.gparser.files.ChannelFileDataWriter;
import com.gparser.parsing.BasicRawParser;
import com.gparser.parsing.ParserMetaData;
import com.gparser.validation.CartesianProductValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * Main entry method for performing any parsing plans.
 * Created by Gilad Ber on 4/12/14.
 */
public class GParserMain
{

	private static final Logger logger = LoggerFactory.getLogger(GParserMain.class);

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

		ParserMetaData metaData = new ParserMetaData.Builder().commentIndicator(args[1]).titleIndicator(args[2]).build();
		BasicRawParser parser = new BasicRawParser(metaData);

		double[][] delBounds = new double[][]{{0, 20}, {-180, 180}, {-180, 180}};
		double[][] xcgBounds = new double[][]{{0, 20}, {-180, 180}, {0, 100}};
		double[][] completionValues = args.length >= 5 && args[4].equals("2") ? xcgBounds : delBounds;
		KHCompleteAction khAction = new KHCompleteAction(new KHCompleteActionInput(3, completionValues));
		CartesianProductValidator cartesianProductValidator = new CartesianProductValidator(3);

		try
		{
			new GParserExecutor(parser, Collections.singletonList(cartesianProductValidator), Collections.singletonList(khAction), new ChannelFileDataWriter()).
				execute(new File(args[0]), new File(args[3]));
		}
		catch (Exception e)
		{
			logger.error("Parsing execution failed: {}", e);
		}
	}

}
