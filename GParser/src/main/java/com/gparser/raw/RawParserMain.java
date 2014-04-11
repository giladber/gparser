package com.gparser.raw;

import com.gparser.raw.actions.KHCompleteAction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Main!
 * Created by Gilad Ber on 4/4/14.
 */
public class RawParserMain
{

	//	public static final int INDEPENDENT_CHANNELS_NUM = 2;

	public static void main(String[] args) throws IOException
	{
		if (args.length < 4)
		{
			System.out.println("Usage: RawParserMain.java <input_file_name> <comment_indicator> <title_indicator> <output_file>");
			return;
		}

		ParserMetaData metaData = new ParserMetaData.Builder().commentIndicator(args[1]).titleIndicator(args[2]).build();
		BasicRawParser parser = new BasicRawParser(metaData);

		LineFactory lineFactory = new LineFactory();
		ParsedFileData data = parser.parse(Files.lines(Paths.get(args[0])).
			map(lineFactory::next), args[0]);

		System.out.println(data);

		ChannelFileData cfd = ChannelFileData.create(data.getDataLines(), data.getTitles(), data.getCommentedLines());
		System.out.println(cfd);
		ChannelFileData finalData = new KHCompleteAction().perform(cfd);
		System.out.println(finalData);
		//		SortAction sortAction = new SortAction(INDEPENDENT_CHANNELS_NUM, true);
		//		ChannelFileData sorted = sortAction.perform(cfd);
		//		System.out.println(cfd);
		//		System.out.println(sorted);
		//
		//		ChannelFileData completed = sortAction.perform(new CompleteAction(1, 20).perform(cfd));
		//		ChannelFileData negativeCompleted = sortAction.perform(new CompleteAction(1, -10).perform(completed));
		//		System.out.println(completed);
		//		System.out.println(negativeCompleted);

		new ChannelFileDataWriter().write(metaData, finalData, args[3]);
	}
}
