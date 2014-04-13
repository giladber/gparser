package com.gparser.files;

import com.gparser.parsing.Line;
import com.gparser.parsing.ParserMetaData;
import com.gparser.utils.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * Writes channel file data to a persistent storage.
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
			writeStream(writer, data.getRowData().stream().map(Line::getData));
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

	/**
	 * I admit this is a pretty bad way to write, but I'm too lazy to change it.
	 *
	 * @param writer BufferedWriter to write with.
	 * @param row    String to write to file.
	 */
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
