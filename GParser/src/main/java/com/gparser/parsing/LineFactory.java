package com.gparser.parsing;

/**
 * A factory class for creating Lines.
 * <p>
 * Each single source of data should use a new LineFactory, since each factory keeps in state,
 * and produces this state, into the indices of lines it creates and has created.
 * Created by Gilad Ber on 4/11/14.
 */
public class LineFactory
{
	private long index = 1;

	public Line next(String line)
	{
		return new Line(index++, line);
	}
}
