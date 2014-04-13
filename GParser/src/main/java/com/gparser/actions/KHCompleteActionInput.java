package com.gparser.actions;

import java.util.Arrays;

/**
 * Input class for the data required to create a KHCompleteAction object.
 * Created by Gilad Ber on 4/13/14.
 */
public class KHCompleteActionInput
{
	private final int numIndependentChannels;
	private final double[][] channelBounds;

	public KHCompleteActionInput(int numIndependentChannels, double[][] channelBounds)
	{
		boolean integrityCheck = channelBounds != null && Arrays.stream(channelBounds).
			map(arr -> arr.length == 2).
			reduce((a, b) -> a && b).
			orElse(false) && channelBounds.length == numIndependentChannels;

		if (!integrityCheck)
		{
			throw new IllegalArgumentException("Bounds array must be 2 times the length of the number of indenpendent channels!");
		}

		this.numIndependentChannels = numIndependentChannels;
		this.channelBounds = channelBounds;
	}

	public int getNumIndependentChannels()
	{
		return numIndependentChannels;
	}

	public double[][] getChannelBounds()
	{
		return channelBounds;
	}
}
