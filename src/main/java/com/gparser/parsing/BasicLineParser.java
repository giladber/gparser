package com.gparser.parsing;

import java.util.Arrays;
import java.util.List;

/**
 * Basic parser for parsing a channel file.
 * Created by Gilad Ber on 3/22/14.
 */
public class BasicLineParser implements LineParser
{

	@Override
	public ChannelLineRepresentation parseLine(String line)
	{
		ChannelLineRepresentation rep = new ChannelLineRepresentation();
		return parseLine(line, rep);
	}

	private ChannelLineRepresentation parseLine(String line, ChannelLineRepresentation current)
	{
		List<String> stringList = Arrays.asList(line.trim().split(""));

		ChannelLineRepresentation result = new ChannelLineRepresentation();
		result.getData().addAll(stringList);

		return result;
	}

}
