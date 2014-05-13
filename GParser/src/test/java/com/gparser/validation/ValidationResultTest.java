package com.gparser.validation;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for ValidationResult class.
 * Created by Gilad Ber on 5/14/14.
 */
public class ValidationResultTest
{

	@Test
	public void testCompose()
	{
		ValidationResult vr1 = new ValidationResult(true, "msg");
		ValidationResult vr2 = new ValidationResult(false, "msg2");

		assertFalse(vr1.compose(vr2).isSucceeded());
		assertTrue(vr1.compose(vr1).isSucceeded());
		assertFalse(vr2.compose(vr2).isSucceeded());
	}

	@Test
	public void testEmptyValidationResult()
	{
		assertTrue(ValidationResult.emptySuccess().isSucceeded());
	}
}
