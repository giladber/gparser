package com.gparser.files;

import com.gparser.parsing.Line;
import com.gparser.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representing data of a channel file.
 * Created by Gilad Ber on 4/4/14.
 */
public class ChannelFileData
{
	private static final RowDataToFileChannelConverter CONVERTER = new RowDataToFileChannelConverter();
	private final List<Line> rowData;
	private final List<FileChannel> channels;
	private final List<String> titles;
	private final List<String> comments;

	private ChannelFileData(List<Line> rowData, List<FileChannel> channels, List<String> comments)
	{
		this.channels = channels;
		this.rowData = rowData;
		this.titles = extractTitles(this.channels);
		this.comments = comments;
	}

	public static ChannelFileData create(List<Line> rowData, List<String> titles, List<String> comments)
	{
		List<FileChannel> channels = CONVERTER.toChannels(rowData, titles);
		return new ChannelFileData(rowData, channels, comments);
	}

	public List<String> getTitles()
	{
		return titles;
	}

	public List<Line> getRowData()
	{
		return rowData;
	}

	public List<FileChannel> getChannels()
	{
		return Collections.unmodifiableList(channels);
	}

	public void addChannel(FileChannel channel)
	{
		if (channel.getData().size() != this.rowData.size())
		{
			throw new IllegalArgumentException("Illegal channel size: must be " + rowData.size());
		}

		Iterator<String> channelIterator = channel.getData().iterator();
		List<Line> newLines = rowData.stream().sequential().
			map(line -> new Line(line.getIndex(), line.getData() + " " + channelIterator.next())).
			collect(Collectors.toList());

		rowData.clear();
		rowData.addAll(newLines);
		channels.add(channel);
		titles.add(channel.getTitle());
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
		public List<FileChannel> toChannels(List<Line> rowData, List<String> titles)
		{
			List<FileChannel> channels = new ArrayList<>(titles.size());
			titles.stream().forEachOrdered(title -> channels.add(new FileChannel(title)));

			rowData.stream().
				filter(s -> !s.getData().isEmpty()).
				forEachOrdered(line -> {
					validateLineChannelNum(channels, line);
					Iterator<FileChannel> channelIterator = channels.iterator();

					Arrays.stream(StringUtils.lineToArray(line)).
						forEachOrdered(val -> channelIterator.next().getData().add(val));
				});

			return channels;
		}

		private void validateLineChannelNum(List<FileChannel> channels, Line line)
		{
			int length = StringUtils.lineToArray(line).length;
			if (length > channels.size())
			{
				throw new IllegalArgumentException("Excess data in line: " + line);
			}
			else if (length < channels.size())
			{
				throw new IllegalArgumentException("Missing data in line: " + line);
			}
		}
	}
}
