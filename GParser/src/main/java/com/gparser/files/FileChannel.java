package com.gparser.files;

import java.util.ArrayList;
import java.util.List;

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
		this.title = title;
		this.data = new ArrayList<>();
	}

	public FileChannel(String title, List<String> data)
	{
		this.title = title;
		this.data = data;
	}

	public String getTitle()
	{
		return title;
	}

	public List<String> getData()
	{
		return data;
	}

	@Override
	public String toString()
	{
		return "FileChannel{" +
			"title='" + title + '\'' +
			", data=" + data +
			'}';
	}
}
