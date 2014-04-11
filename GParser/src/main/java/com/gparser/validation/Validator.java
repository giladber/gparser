package com.gparser.validation;

import com.gparser.files.ChannelFileData;

/**
 * Basic interface for file data validation.
 * Created by Gilad Ber on 4/5/14.
 */
public interface Validator
{
	public ValidationResult validate(ChannelFileData data);
}
