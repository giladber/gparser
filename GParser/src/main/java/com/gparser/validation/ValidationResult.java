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

	public ValidationResult compose(ValidationResult other)
	{
		boolean success = this.succeeded && other.succeeded;
		String msg = success ? "Validation succeeded" : "Validation failed: " + this.msg + ", " + other.msg;
		return new ValidationResult(success, msg);
	}

	public boolean isSucceeded()
	{
		return succeeded;
	}

	public String getMsg()
	{
		return msg;
	}

	public static ValidationResult emptySuccess()
	{
		return new ValidationResult(true, "Success");
	}
}
