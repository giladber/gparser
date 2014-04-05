package com.gparser.parsing;

import java.util.List;

/**
 * Line representation of a comment line.
 * Created by Gilad Ber on 3/29/14.
 */
public class CommentLineRepresentation extends TaggedLineRepresentation
{
	public CommentLineRepresentation(List<String> data)
	{
		super(data);
	}

	public CommentLineRepresentation()
	{
		super();
	}

}
