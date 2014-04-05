package com.gparser.raw;

import com.gparser.utils.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * Created by Gilad Ber on 4/5/14.
 */
public class ChannelFileDataWriter
{
	public void write(ParserMetaData metaData, ChannelFileData data, String targetFileLoc)
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFileLoc, false)))
		{
			writeStream(writer, data.getComments().stream());
			writeRow(writer, metaData.titleIndicator + StringUtils.join(data.getTitles()));
			writeStream(writer, data.getRowData().stream());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private <T> void writeStream(BufferedWriter writer, Stream<T> stream)
	{
		stream.sequential().
			forEachOrdered(obj -> writeRow(writer, obj.toString()));
	}

	private void writeRow(BufferedWriter writer, String row)
	{
		try
		{
			writer.write(row + "\n");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
