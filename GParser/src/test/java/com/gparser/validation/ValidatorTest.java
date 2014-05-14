package com.gparser.validation;

import com.gparser.files.ChannelFileData;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for Validator class.
 * Created by Gilad Ber on 5/14/14.
 */
public class ValidatorTest
{
	@Test
	public void testCompose()
	{
		Validator v1 = cfd -> new ValidationResult(false, "");
		Validator v2 = cfd -> new ValidationResult(true, "a");
		Validator v3 = v1.compose(v2);
		Validator v4 = v2.compose(x -> new ValidationResult(true, "abc"));

		assertFalse(v3.validate(Optional.of(ChannelFileData.empty())).isSucceeded());
		assertFalse(v3.validate(Optional.empty()).isSucceeded());
		assertTrue(v4.validate(Optional.of(ChannelFileData.empty())).isSucceeded());
	}

	@Test(expected = NullPointerException.class)
	public void testNullMsgValidator()
	{
		Validator validator = x -> new ValidationResult(false, null);
		Validator composed = validator.compose(x -> new ValidationResult(true, "aa"));

		composed.validate(Optional.empty());
	}
}
