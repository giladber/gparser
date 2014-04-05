package com.gparser.parsing;

import com.gparser.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a channel file.
 * Created by Gilad Ber on 3/22/14.
 */
public class ChannelLineRepresentation implements LineRepresentation
{
	private final List<String> data = new ArrayList<>();

	@Override
	public List<String> getData()
	{
		return data;
	}

	@Override
	public String resultString()
	{
		return StringUtils.join(data);
	}


}
