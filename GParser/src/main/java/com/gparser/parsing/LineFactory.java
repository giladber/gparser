package com.gparser.parsing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static final Logger logger = LoggerFactory.getLogger(LineFactory.class);

	public Line next(String line)
	{
		logger.trace("Created line #{}", index + 1);
		return new Line(index++, line);
	}
}
