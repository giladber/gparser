package com.gparser.parsing;

/**
 * Created by Gilad Ber on 4/11/14.
 */
public class LineFactory
{
	public long index = 1;

	public Line next(String line)
	{
		return new Line(index++, line);
	}
}
