package com.gparser.validation;

import com.gparser.files.ChannelFileData;

/**
 * Basic interface for file data validation.
 * <p>
 * Implementing classes should be aware *not* to change any state of the input data.
 * Instead, if any state is to be changed, it should be done by copying.
 * <p>
 * Indication of whether the validation has succeeded and why will be present in the returned
 * ValidationResult object.
 * <p>
 * Validations are not expected to throw *any* exceptions, not even unchecked ones.
 * Created by Gilad Ber on 4/5/14.
 */
public interface Validator
{
	public ValidationResult validate(ChannelFileData data);
}
