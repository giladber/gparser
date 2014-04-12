package com.gparser.validation;

/**
 * This class defined the model for results of a validation test.
 * Created by Gilad Ber on 4/5/14.
 */
public class ValidationResult
{
	private final boolean succeeded;
	private final String msg;

	public ValidationResult(boolean success, String msg)
	{
		this.succeeded = success;
		this.msg = msg;
	}

	public boolean isSucceeded()
	{
		return succeeded;
	}

	public String getMsg()
	{
		return msg;
	}
}
