package com.gparser.files.data;

import com.gparser.parsing.ChannelLineRepresentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Channel representation of a file.
 * Created by Gilad Ber on 3/29/14.
 */
public class ChannelFileData<T extends Comparable<T>> implements FileData<ChannelLineRepresentation>
{
	private final List<DataChannel> channels = new ArrayList<>();
	private final List<ChannelLineRepresentation> lineRepresentation = new ArrayList<>();

	public ChannelFileData()
	{

	}

	public ChannelFileData(List<DataChannel> channels)
	{
		this.channels.addAll(channels);
		lineRepresentation.addAll(buildChannelRepresentation());
	}

	@Override
	public List<ChannelLineRepresentation> getData()
	{
		return lineRepresentation;
	}

	private List<ChannelLineRepresentation> buildChannelRepresentation()
	{
		List<ChannelLineRepresentation> result = new ArrayList<>();
		long numberOfLines = channels.get(0).getLength();

		for (long idx = 0; idx < numberOfLines; idx++)
		{
			result.add(new ChannelLineRepresentation());
		}

		long numberOfColumns = channels.size();
		long channelIdx = 0;
		for (ChannelLineRepresentation line : result)
		{
			for (DataChannel channel : channels)
			{
				String val = channel.getData().get((int) channelIdx);
				line.getData().add(val);
			}
			channelIdx = (channelIdx + 1) % numberOfColumns;
		}

		return result;
	}
}
