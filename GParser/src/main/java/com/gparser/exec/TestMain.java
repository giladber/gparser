package com.gparser.exec;

import com.gparser.actions.KHCompleteAction;
import com.gparser.files.ChannelFileData;
import com.gparser.files.ChannelFileDataWriter;
import com.gparser.parsing.BasicRawParser;
import com.gparser.parsing.LineFactory;
import com.gparser.parsing.ParsedFileData;
import com.gparser.parsing.ParserMetaData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Main!
 * Created by Gilad Ber on 4/4/14.
 */
public class TestMain
{

	//	public static final int INDEPENDENT_CHANNELS_NUM = 2;

	public static void main(String[] args) throws IOException
	{
		if (args.length < 4)
		{
			System.out.println("Usage: TestMain.java <input_file_name> <comment_indicator> <title_indicator> <output_file>");
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
		ChannelFileData finalData = new KHCompleteAction().apply(cfd);
		System.out.println(finalData);

		new ChannelFileDataWriter().write(metaData, finalData, args[3]);
	}
}
