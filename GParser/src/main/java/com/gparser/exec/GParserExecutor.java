package com.gparser.exec;

import com.gparser.actions.ChannelAction;
import com.gparser.files.ChannelFileData;
import com.gparser.files.ChannelFileDataWriter;
import com.gparser.parsing.LineFactory;
import com.gparser.parsing.ParsedFileData;
import com.gparser.parsing.StringParser;
import com.gparser.validation.ValidationResult;
import com.gparser.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Executes general gparser plans.
 * It should be noted that the plans are executed in the exact order they are given as input,
 * with validations running before the actions.
 * Created by Gilad Ber on 4/12/14.
 */
public class GParserExecutor
{
	private final List<ChannelAction> actions = new ArrayList<>();
	private final List<Validator> validators = new ArrayList<>();
	private final StringParser parser;
	private final ChannelFileDataWriter writer;

	private static final Logger logger = LoggerFactory.getLogger(GParserExecutor.class);

	public GParserExecutor(StringParser parser, List<Validator> validators, List<ChannelAction> actions, ChannelFileDataWriter writer)
	{
		this.parser = parser;
		this.validators.addAll(validators);
		this.actions.addAll(actions);
		this.writer = writer;

		String validatorsString = validators.stream().map(v -> v.getClass().getSimpleName()).collect(Collectors.joining(", "));
		String actionsString = actions.stream().map(a -> a.getClass().getSimpleName()).collect(Collectors.joining(", "));
		logger.info("Created GParser execution plan with validations: {}, actions: {}", validatorsString, actionsString);
	}

	public void execute(File source, File dest) throws IOException
	{
		logger.info("Beginning parsing of execution plan from file {} to file {}", source, dest);
		LineFactory lineFactory = new LineFactory();
		ParsedFileData data = parser.parse(Files.lines(source.toPath()).
			map(lineFactory::next), source.getName());

		ChannelFileData baseFileData = ChannelFileData.create(data.getDataLines(), data.getTitles(), data.getCommentedLines());
		logger.info("Created new channel data from parsed data for file {}", source);
		validate(baseFileData);
		logger.info("Finished validations on file {}", source);

		ChannelAction compositeAction = actions.stream().sequential().
			reduce(ChannelAction::compose).orElseGet(ChannelAction::getUnit);
		ChannelFileData result = compositeAction.apply(baseFileData);
		logger.info("Finished performing all actions on file {}", source);

		logger.info("Now persisting result of actions on file {} to file {}", source, dest);
		writer.write(data.getMetaData(), result, dest.getAbsolutePath());
		logger.info("Finished persisting results to file {}", dest);
	}

	private void validate(ChannelFileData base) throws IllegalArgumentException
	{
		Validator validator = validators.stream().
			reduce(Validator::compose).
			orElseGet(Validator::empty);
		
		ValidationResult result = validator.validate(base);
		if (!result.isSucceeded())
		{
			throw new IllegalArgumentException(result.getMsg());
		}
	}
}
