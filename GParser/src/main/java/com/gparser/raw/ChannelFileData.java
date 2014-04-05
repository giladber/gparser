package com.gparser.raw;

import com.gparser.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Gilad Ber on 4/4/14.
 */
public class ChannelFileData
{
	public static final RowDataToFileChannelConverter CONVERTER = new RowDataToFileChannelConverter();
	private final List<String> rowData;
	private final List<FileChannel> channels;
	private final List<String> titles;
	private final List<String> comments;

	private ChannelFileData(List<String> rowData, List<FileChannel> channels, List<String> comments)
	{
		this.channels = channels;
		this.rowData = rowData;
		this.titles = extractTitles(this.channels);
		this.comments = comments;
	}

	public static ChannelFileData create(List<String> rowData, List<String> titles, List<String> comments)
	{
		List<FileChannel> channels = CONVERTER.toChannels(rowData, titles);
		return new ChannelFileData(rowData, channels, comments);
	}

	public List<String> getTitles()
	{
		return titles;
	}

	public List<String> getRowData()
	{
		return rowData;
	}

	public List<FileChannel> getChannels()
	{
		return channels;
	}

	public List<String> getComments()
	{
		return comments;
	}

	private List<String> extractTitles(List<FileChannel> channels)
	{
		return channels.stream().map(FileChannel::getTitle).collect(Collectors.toList());
	}

	@Override
	public String toString()
	{
		return "ChannelFileData{" +
			"channels=" + channels +
			'}';
	}

	public static final class RowDataToFileChannelConverter
	{
		public List<FileChannel> toChannels(List<String> rowData, List<String> titles)
		{
			List<FileChannel> channels = new ArrayList<>(titles.size());
			titles.stream().forEachOrdered(title -> channels.add(new FileChannel(title)));

			rowData.stream().filter(s -> !s.isEmpty()).
				forEachOrdered(line -> {
					Iterator<FileChannel> channelIterator = channels.iterator();
					String[] splitLine = StringUtils.splitBySpaces(line);

					Arrays.stream(splitLine).forEachOrdered(val -> {
						channelIterator.next().getData().add(val);
					});
				});

			return channels;
		}
	}
}
