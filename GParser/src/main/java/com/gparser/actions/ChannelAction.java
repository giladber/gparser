package com.gparser.actions;

import com.gparser.files.ChannelFileData;

/**
 * A channel action is an action which may be performed on data with channel-like structure.
 * Channel actions *should not* change the underlying data of the input structure, instead they return
 * a new instance with the updated data, after the action is performed.
 * Created by Gilad Ber on 4/4/14.
 */
public interface ChannelAction
{
	public ChannelFileData perform(ChannelFileData data);
}
