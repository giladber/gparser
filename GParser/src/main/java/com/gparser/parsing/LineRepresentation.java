package com.gparser.parsing;

import java.util.List;

/**
 * Basic interface for the representation of a line element.
 * Created by Gilad Ber on 3/22/14.
 */
public interface LineRepresentation
{
	public List<String> getData();

	public String resultString();
}
