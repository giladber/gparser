package com.gparser.tests.mock;

import com.gparser.files.ChannelFileData;
import com.gparser.files.ChannelFileDataWriter;
import com.gparser.parsing.ParserMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

/**
 * Main class for creating random files for tests etc.
 * Created by Gilad Ber on 5/1/14.
 */
public class RandomFileCreator
{
	private static final Logger logger = LoggerFactory.getLogger(RandomFileCreator.class);
	public static final int TITLES_FIRST_POS = 3;

	public static void main(String[] args)
	{
		if (args.length < 4)
		{
			System.out.println("Usage: RandomFileCreator <output_file> <max_channel_length> <num_dependent_columns> <title1> ... <titleN>");
			return;
		}

		File targetFile = new File(args[0]);
		long length = Long.parseLong(args[1]);
		int numDependentChannels = Integer.parseInt(args[2]);
		String[] titles = new String[args.length - TITLES_FIRST_POS];
		System.arraycopy(args, TITLES_FIRST_POS, titles, 0, args.length - TITLES_FIRST_POS);

		logger.info("RFC params: len={}, nDC={}, titles={}, target={}", length, numDependentChannels, Arrays.toString(titles), targetFile);


		ChannelFileData rnd = new RandomChannelFileDataBuilder().randomize(length, titles, numDependentChannels);
		ParserMetaData metaData = new ParserMetaData.Builder().commentIndicator("%#").titleIndicator("%%").build();
		new ChannelFileDataWriter().write(metaData, rnd, targetFile.getAbsolutePath());
	}
}
