package com.gparser.files.data;

import com.gparser.parsing.LineRepresentation;

import java.util.List;

/**
 * Represents a file's inner data model.
 * Created by Gilad Ber on 3/29/14.
 */
public interface FileData<T extends LineRepresentation>
{
	public List<T> getData();
}
