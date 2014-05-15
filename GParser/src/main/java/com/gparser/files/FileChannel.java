package com.gparser.files;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A single channel from a file channel.
 * Created by Gilad Ber on 4/4/14.
 */
public class FileChannel
{
	private final String title;
	private final List<String> data;

	public FileChannel(String title)
	{
		Objects.requireNonNull(title);
		this.title = title;
		this.data = new ArrayList<>();
	}

	public FileChannel(String title, List<String> data)
	{
		Objects.requireNonNull(title);
		Objects.requireNonNull(data);
		this.title = title;
		this.data = data;
	}

	public String getTitle()
	{
		return title;
	}

	public List<String> getData()
	{
		return Collections.unmodifiableList(data);
	}

	public void addLine(String s)
	{
		data.add(s);
	}

	@Override
	public String toString()
	{
		return "FileChannel{" +
			"title='" + title + '\'' +
			", data=" + data +
			'}';
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

		FileChannel that = (FileChannel) o;

		if (!data.equals(that.data))
		{
			return false;
		}
		if (!title.equals(that.title))
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = title.hashCode();
		result = 31 * result + data.hashCode();
		return result;
	}
}
