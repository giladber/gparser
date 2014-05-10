package com.gparser.validation;

import com.gparser.files.ChannelFileData;

/**
 * Basic interface for file data validation.
 * Implementing classes should be aware *not* to change any state of the input data.
 * Instead, if any state is to be changed, it should be done by copying.
 * Indication of whether the validation has succeeded and why will be present in the returned
 * ValidationResult object.
 * Validations are not expected to throw any checked exceptions, and should take extra care to also not throw
 * any unchecked ones.
 * Created by Gilad Ber on 4/5/14.
 */
@FunctionalInterface
public interface Validator
{
	public ValidationResult validate(ChannelFileData data);

	public default Validator compose(Validator other)
	{
		return cfd -> this.validate(cfd).compose(other.validate(cfd));
	}

	public static Validator empty()
	{
		return cfd -> ValidationResult.emptySuccess();
	}
}
