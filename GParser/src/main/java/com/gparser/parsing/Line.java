package com.gparser.parsing;

/**
 * Class representing data from a single line in a file.
 * Created by Gilad Ber on 4/11/14.
 */
public class Line
{
	private final long index;
	private final String data;

	public Line(long index, String data)
	{
		this.index = index;
		this.data = data;
	}

	public long getIndex()
	{
		return index;
	}

	public String getData()
	{
		return data;
	}

	public String toString()
	{
		return "Line " + index + ": " + data;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		Line line = (Line) o;

		if (index != line.index)
		{
			return false;
		}
		if (data != null ? !data.equals(line.data) : line.data != null)
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = (int) (index ^ (index >>> 32));
		result = 31 * result + (data != null ? data.hashCode() : 0);
		return result;
	}
}
