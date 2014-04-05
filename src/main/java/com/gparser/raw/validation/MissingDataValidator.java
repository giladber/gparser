package com.gparser.raw.validation;

import com.gparser.raw.ParsedFileData;

/**
 * Validates that there is no missing or excess data in a parsed file data object.
 * Created by Gilad Ber on 4/5/14.
 */
public class MissingDataValidator implements Validator
{
	@Override
	public ValidationResult validate(ParsedFileData data)
	{
		return null;
	}
}
