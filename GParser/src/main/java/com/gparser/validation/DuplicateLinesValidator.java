package com.gparser.validation;

import com.gparser.files.ChannelFileData;
import com.gparser.parsing.Line;
import com.gparser.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Validator class for checking that a channel file has no duplicate lines.
 * Any line is considered a duplicate of another if its independent channel values are the same as the other line's.
 * Created by Gilad Ber on 4/5/14.
 */
public class DuplicateLinesValidator implements Validator
{
	private static final int MAX_FAILED_VALIDATIONS = 50;
	private static final String VALIDATION_OK_MSG = "success";
	private static final Logger logger = LoggerFactory.getLogger(DuplicateLinesValidator.class);

	private final int numIndependentChannels;

	public DuplicateLinesValidator(int numIndependentChannels)
	{
		if (numIndependentChannels < 0)
		{
			throw new IllegalArgumentException("Number of independent channels must be >= 0, is: " + numIndependentChannels);
		}
		this.numIndependentChannels = numIndependentChannels;
	}

	@Override
	/**
	 * Validates that there are no duplicate lines in the input, where duplicate lines will consist of the same data
	 * for all independent channels.
	 * It is assumed that the independent channels are the first channels.
	 */
	public ValidationResult validate(Optional<ChannelFileData> fileDataOptional)
	{
		ChannelFileData data = fileDataOptional.orElse(ChannelFileData.empty());
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

		ValidationResult result = buildFinalResult(resultsStream);
		logger.info("Duplicate lines validation result: {}", result.isSucceeded() ? "success" : "failure");
		return result;
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
