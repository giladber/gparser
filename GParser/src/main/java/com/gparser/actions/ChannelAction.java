package com.gparser.actions;

import com.gparser.files.ChannelFileData;

import java.util.function.Function;

/**
 * A channel action is an action which is performed on data with channel-like structure.
 * Channel actions *should not* change the underlying data of the input structure, instead they return
 * a new instance with the updated data, after the action is performed.
 * <p>
 * Since channel actions do not change the underlying state of the input data, they should be safe to use
 * in a concurrent/parallel fashion.
 * <p>
 * Created by Gilad Ber on 4/4/14.
 */
public interface ChannelAction extends Function<ChannelFileData, ChannelFileData>
{
	public ChannelFileData apply(ChannelFileData data);

	public default ChannelAction compose(ChannelAction other)
	{
		return (cfd -> this.apply(other.apply(cfd)));
	}

	public static ChannelAction getUnit()
	{
		return UnitAction.instance;
	}

	static final class UnitAction implements ChannelAction
	{
		private static final UnitAction instance = new UnitAction();

		private UnitAction()
		{

		}

		@Override
		public ChannelFileData apply(ChannelFileData data)
		{
			return data;
		}

	}
}
