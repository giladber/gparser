package com.gparser.exec;

import com.gparser.actions.ChannelAction;
import com.gparser.files.ChannelFileData;
import com.gparser.files.ChannelFileDataWriter;
import com.gparser.parsing.LineFactory;
import com.gparser.parsing.ParsedFileData;
import com.gparser.parsing.StringParser;
import com.gparser.validation.ValidationResult;
import com.gparser.validation.Validator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Executes general gparser plans.
 * Created by Gilad Ber on 4/12/14.
 */
public class GParserExecutor
{
	private final List<ChannelAction> actions = new ArrayList<>();
	private final List<Validator> validators = new ArrayList<>();
	private final StringParser parser;
	private final ChannelFileDataWriter writer;

	public GParserExecutor(StringParser parser, List<Validator> validators, List<ChannelAction> actions, ChannelFileDataWriter writer)
	{
		this.parser = parser;
		this.validators.addAll(validators);
		this.actions.addAll(actions);
		this.writer = writer;
	}

	public void execute(File source, File dest) throws IOException
	{
		LineFactory lineFactory = new LineFactory();
		ParsedFileData data = parser.parse(Files.lines(source.toPath()).
			map(lineFactory::next), source.getName());

		ChannelFileData baseFileData = ChannelFileData.create(data.getDataLines(), data.getTitles(), data.getCommentedLines());
		validate(baseFileData);

		ChannelAction compositeAction = actions.stream().sequential().
			reduce(ChannelAction::compose).orElseGet(ChannelAction::getUnit);

		ChannelFileData result = compositeAction.apply(baseFileData);
		writer.write(data.getMetaData(), result, dest.getAbsolutePath());
	}

	private void validate(ChannelFileData base) throws IllegalArgumentException
	{
		Optional<ValidationResult> optionalResult = validators.parallelStream().
			map(v -> v.validate(base)).
			filter(vr -> !vr.isSucceeded()).
			reduce((r1, r2) -> new ValidationResult(false, r1.getMsg() + "\n" + r2.getMsg()));

		ValidationResult result = optionalResult.orElse(new ValidationResult(true, "All validations successful!"));
		if (!result.isSucceeded())
		{
			throw new IllegalArgumentException(result.getMsg());
		}
	}
}
