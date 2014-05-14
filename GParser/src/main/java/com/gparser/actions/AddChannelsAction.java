package com.gparser.actions;

import com.gparser.files.ChannelFileData;
import com.gparser.files.FileChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Action to add new channels to an existing channel file data object.
 * Created by Gilad Ber on 5/2/14.
 */
public class AddChannelsAction implements ChannelAction
{
	private final List<FileChannel> channels = new ArrayList<>();

	public AddChannelsAction(List<FileChannel> channels)
	{
		Objects.requireNonNull(channels);
		this.channels.addAll(channels);
	}

	@Override
	public ChannelFileData apply(ChannelFileData data)
	{
		ChannelFileData result = ChannelFileData.create(data.getRowData(), data.getTitles(), data.getComments());
		channels.forEach(result::addChannel);
		return result;
	}
}
