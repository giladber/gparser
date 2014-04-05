package com.gparser.parsing;

import com.gparser.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract skeletion class for a line representation of tagged data (ie comments).
 * Created by Gilad Ber on 3/29/14.
 */
public abstract class TaggedLineRepresentation implements LineRepresentation
{
	private final List<String> data = new ArrayList<>();

	public TaggedLineRepresentation(List<String> data)
	{
		this.data.addAll(data);
	}

	public TaggedLineRepresentation()
	{

	}

	@Override
	public String resultString()
	{
		return StringUtils.join(data);
	}

	@Override
	public List<String> getData()
	{
		return data;
	}

}
