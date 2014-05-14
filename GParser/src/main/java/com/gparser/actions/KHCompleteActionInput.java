package com.gparser.actions;

import java.util.Arrays;
import java.util.Objects;

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
		Objects.requireNonNull(channelBounds);
		Arrays.stream(channelBounds).forEach(Objects::requireNonNull);

		if (numIndependentChannels < 1)
		{
			throw new IllegalArgumentException("Number of independent channels must be > 0; is: " + numIndependentChannels);
		}

		boolean integrityCheck = Arrays.stream(channelBounds).
			map(arr -> arr.length == 2).
			reduce((a, b) -> a && b).
			orElse(false) && channelBounds.length == numIndependentChannels;

		if (!integrityCheck)
		{
			throw new IllegalArgumentException("Bounds array must be 2 times the length of the number of independent channels!");
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
