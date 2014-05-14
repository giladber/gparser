package com.gparser.actions;

import com.gparser.files.ChannelFileData;

import java.util.Objects;

/**
 * A channel action is an action which is performed on data with channel-like structure.
 * Channel actions *should not* change the underlying data of the input structure, instead they return
 * a new instance with the updated data, after the action is performed.
 * Since channel actions do not change the underlying state of the input data, they should be safe to use
 * in a concurrent/parallel fashion.
 * Created by Gilad Ber on 4/4/14.
 */
@FunctionalInterface
public interface ChannelAction
{
	/**
	 * Applies some sort of state-mutating action to the input data.
	 * This action should *not* actually mutate the state of the input, but rather create a new instance of
	 * the ChannelFileData object and perform the required computation on it.
	 *
	 * @param data Input file data for the action.
	 * @return A new copy of the input data after application of the action.
	 */
	public ChannelFileData apply(ChannelFileData data);

	/**
	 * Composes the calling action with the parameter action, in-order.
	 *
	 * @param other Action to compose with
	 * @return A new channel action composed of the calling action and the parameter action.
	 */
	public default ChannelAction compose(ChannelAction other)
	{
		Objects.requireNonNull(other);
		return (cfd -> this.apply(other.apply(cfd)));
	}

	/**
	 * Return the unit action, which performs no computations and returns the same copy of the input.
	 *
	 * @return The unit channel action.
	 */
	public static ChannelAction getUnit()
	{
		return cfd -> cfd;
	}

}
