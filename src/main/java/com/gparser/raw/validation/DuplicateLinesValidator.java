package com.gparser.raw.validation;

import com.gparser.raw.ChannelFileData;
import com.gparser.raw.ParsedFileData;

/**
 * Created by Gilad Ber on 4/5/14.
 */
public class DuplicateLinesValidator implements Validator
{
	private final int numIndependentChannels;

	public DuplicateLinesValidator(int numIndependentChannels)
	{
		this.numIndependentChannels = numIndependentChannels;
	}

	@Override
	/**
	 * Validates that there are no duplicate lines in the input, where duplicate lines will consist of the same data
	 * for all independent channels.
	 * It is assumed that the independent channels are the first in the file.
	 */
	public ValidationResult validate(ParsedFileData data)
	{
		ChannelFileData cfd = ChannelFileData.create(data.getDataLines(), data.getTitles(), data.getCommentedLines());

		return null;
	}
}
