package com.gparser.files.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A file channel with floating point data.
 * Created by Gilad Ber on 3/29/14.
 */
public class DataChannel implements Iterable<String>
{
	public final String title;
	public final List<String> data = new ArrayList<>();

	public DataChannel(String title)
	{
		this.title = title;
	}

	public DataChannel(String title, List<String> data)
	{
		this(title);
		this.data.addAll(data);
	}

	public long getLength()
	{
		return data.size();
	}

	/**
	 * @return immutable copy of channel's data
	 */
	public List<String> getData()
	{
		return Collections.unmodifiableList(data);
	}

	public void add(String value)
	{
		data.add(value);
	}

	@Override
	public Iterator<String> iterator()
	{
		return data.iterator();
	}
}
