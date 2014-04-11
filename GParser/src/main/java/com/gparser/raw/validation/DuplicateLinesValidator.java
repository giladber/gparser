package com.gparser.raw.validation;

import com.gparser.raw.ChannelFileData;
import com.gparser.raw.Line;
import com.gparser.utils.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Validator class for checking that a channel file has no duplicate lines.
 * Any line is considered a duplicate of another if its independent channel values are the same as the other line's.
 * Created by Gilad Ber on 4/5/14.
 */
public class DuplicateLinesValidator implements Validator
{
	public static final int MAX_FAILED_VALIDATIONS = 50;
	public static final String VALIDATION_OK_MSG = "success";

	private final int numIndependentChannels;

	public DuplicateLinesValidator(int numIndependentChannels)
	{
		this.numIndependentChannels = numIndependentChannels;
	}

	@Override
	/**
	 * Validates that there are no duplicate lines in the input, where duplicate lines will consist of the same data
	 * for all independent channels.
	 * It is assumed that the independent channels are the first channels.
	 */
	public ValidationResult validate(ChannelFileData data)
	{
		Set<List<String>> channels = new HashSet<>();
		Stream<String> resultsStream = data.getRowData().stream().
			map(line -> {
				String[] split = StringUtils.splitBySpaces(line.getData());
				List<String> independentChannels = Arrays.stream(split).limit(numIndependentChannels).collect(Collectors.toList());
				boolean wasContained = !channels.add(independentChannels);

				return buildValidationResult(line, wasContained);
			}).
			filter(vr -> !vr.isSucceeded()).
			limit(MAX_FAILED_VALIDATIONS).
			map(ValidationResult::getMsg);

		return buildFinalResult(resultsStream);
	}

	private ValidationResult buildFinalResult(Stream<String> resultsStream)
	{
		String result = StringUtils.join(resultsStream, "\n");
		if (result.length() == 0)
		{
			return new ValidationResult(true, VALIDATION_OK_MSG);
		}
		else
		{
			return new ValidationResult(false, result);
		}
	}

	private ValidationResult buildValidationResult(Line line, boolean wasContained)
	{
		if (!wasContained)
		{
			return new ValidationResult(true, VALIDATION_OK_MSG);
		}
		else
		{
			return new ValidationResult(false, "Duplicate line: " + line);
		}
	}
}
