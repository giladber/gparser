package com.gparser.tests.mock;

import com.gparser.files.ChannelFileData;
import com.gparser.parsing.BasicRawParser;
import com.gparser.parsing.ParsedFileData;
import com.gparser.parsing.ParserMetaData;
import com.gparser.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for creating ChannelFileData mocks;
 * Created by Gilad Ber on 4/12/14.
 */
public class ChannelFileDataFactory
{

	public ChannelFileData build(String fileLoc, String commentIndicator, String titleIndicator) throws IOException
	{
		ParserMetaData metaData = new ParserMetaData.Builder().
			commentIndicator(commentIndicator).titleIndicator(titleIndicator).build();

		BasicRawParser parser = new BasicRawParser(metaData);
		ParsedFileData parsedFileData = parser.parse(FileUtils.toLines(new File(fileLoc)), fileLoc);
		return ChannelFileData.
			create(parsedFileData.getDataLines(), parsedFileData.getTitles(), parsedFileData.getCommentedLines());
	}

}
