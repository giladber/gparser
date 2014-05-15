package com.gparser.files;

import com.gparser.parsing.Line;
import com.gparser.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class representing data of a channel file.
 * Created by Gilad Ber on 4/4/14.
 */
public class ChannelFileData
{
	private static final RowDataToFileChannelConverter CONVERTER = new RowDataToFileChannelConverter();
	private static final ChannelFileData EMPTY = new Empty();
	private final List<Line> rowData = new ArrayList<>();
	private final List<FileChannel> channels = new ArrayList<>();
	private final List<String> titles = new ArrayList<>();
	private final List<String> comments = new ArrayList<>();

	protected ChannelFileData(List<Line> rowData, List<FileChannel> channels, List<String> comments)
	{
		this.channels.addAll(channels);
		this.rowData.addAll(rowData);
		this.titles.addAll(extractTitles(this.channels));
		this.comments.addAll(comments);
	}

	public static ChannelFileData create(List<Line> rowData, List<String> titles, List<String> comments)
	{
		List<FileChannel> channels = CONVERTER.toChannels(rowData, titles);
		return new ChannelFileData(rowData, channels, comments);
	}

	public static ChannelFileData empty()
	{
		return EMPTY;
	}

	public List<String> getTitles()
	{
		return Collections.unmodifiableList(titles);
	}

	public List<Line> getRowData()
	{
		return Collections.unmodifiableList(rowData);
	}

	public List<FileChannel> getChannels()
	{
		return Collections.unmodifiableList(channels);
	}

	public ChannelFileData addChannel(FileChannel channel)
	{
		Objects.requireNonNull(channel);
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

		return this;
	}

	public List<String> getComments()
	{
		return Collections.unmodifiableList(comments);
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
			List<List<String>> channelDatum = IntStream.range(0, titles.size()).
				boxed().
				map(x -> new ArrayList<String>()).
				collect(Collectors.toList());

			rowData.stream().
				filter(s -> !s.getData().isEmpty()).
				forEachOrdered(line -> {
					validateLineChannelNum(titles.size(), line);
					Iterator<List<String>> channelIterator = channelDatum.iterator();

					Arrays.stream(StringUtils.lineToArray(line)).
						forEach(s -> channelIterator.next().add(s));
				});

			Iterator<String> titleIterator = titles.iterator();
			return channelDatum.stream().
				map(list -> new FileChannel(titleIterator.next(), list)).
				collect(Collectors.toList());
		}

		private void validateLineChannelNum(int numChannels, Line line)
		{
			int length = StringUtils.lineToArray(line).length;
			if (length > numChannels)
			{
				throw new IllegalArgumentException("Excess data in line: " + line);
			}
			else if (length < numChannels)
			{
				throw new IllegalArgumentException("Missing data in line: " + line);
			}
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (!(o instanceof ChannelFileData))
		{
			return false;
		}

		ChannelFileData that = (ChannelFileData) o;

		if (!comments.equals(that.comments))
		{
			return false;
		}
		if (!rowData.equals(that.rowData))
		{
			return false;
		}
		if (!titles.equals(that.titles))
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = rowData.hashCode();
		result = 31 * result + titles.hashCode();
		result = 31 * result + comments.hashCode();
		return result;
	}

	private static final class Empty extends ChannelFileData
	{
		Empty()
		{
			super(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
		}
	}
}
