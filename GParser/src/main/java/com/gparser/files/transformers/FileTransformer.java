package com.gparser.files.transformers;

import com.gparser.files.data.FileData;
import com.gparser.parsing.LineRepresentation;

/**
 * General interface for transforming the model of a file.
 * Created by Gilad Ber on 3/29/14.
 */
public interface FileTransformer
{
	public <T extends LineRepresentation> FileData<T> transform(FileData<T> data);
}
