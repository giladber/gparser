package com.gparser.validation;

import com.gparser.files.ChannelFileData;
import com.gparser.tests.mock.ChannelFileDataFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static com.gparser.tests.ResourceUtils.getResourceLocation;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for DuplicateLinesValidator class.
 * Created by Gilad Ber on 5/14/14.
 */
public class DuplicateLinesValidatorTest
{
	private static DuplicateLinesValidator val1;
	private static DuplicateLinesValidator val2;
	private static DuplicateLinesValidator val3;
	private static DuplicateLinesValidator val4;
	private static DuplicateLinesValidator val0;

	private static ChannelFileData duplicateLinesData;
	private static ChannelFileData mediumFileData;

	@BeforeClass
	public static void setup() throws IOException
	{
		val0 = new DuplicateLinesValidator(0);
		val1 = new DuplicateLinesValidator(1);
		val2 = new DuplicateLinesValidator(2);
		val3 = new DuplicateLinesValidator(3);
		val4 = new DuplicateLinesValidator(4);

		ChannelFileDataFactory factory = new ChannelFileDataFactory();
		duplicateLinesData = factory.build(getResourceLocation("result_duplicate_lines.txt"), "%#", "%%");
		mediumFileData = factory.build(getResourceLocation("result_medium.txt"), "%#", "%%");
	}

	@Test
	public void testDuplicateLinesFile()
	{
		assertFalse(val3.validate(Optional.of(duplicateLinesData)).isSucceeded());
	}

	@Test
	public void testRegular()
	{
		assertTrue(val3.validate(Optional.of(mediumFileData)).isSucceeded());
	}

	@Test
	public void testNull()
	{
		assertTrue(val1.validate(Optional.empty()).isSucceeded());
		assertTrue(val2.validate(Optional.empty()).isSucceeded());
		assertTrue(val3.validate(Optional.empty()).isSucceeded());
		assertTrue(val4.validate(Optional.empty()).isSucceeded());
	}

	@Test
	public void testEmpty()
	{
		Optional<ChannelFileData> emptyOpt = Optional.of(ChannelFileData.empty());
		assertTrue(val1.validate(emptyOpt).isSucceeded());
		assertTrue(val2.validate(emptyOpt).isSucceeded());
		assertTrue(val3.validate(emptyOpt).isSucceeded());
		assertTrue(val4.validate(emptyOpt).isSucceeded());
	}

	@Test
	public void testNoChannelsValidator()
	{
		assertTrue(val0.validate(Optional.of(ChannelFileData.empty())).isSucceeded());
		assertFalse(val0.validate(Optional.of(duplicateLinesData)).isSucceeded());
		assertFalse(val0.validate(Optional.of(mediumFileData)).isSucceeded());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeChannelsThrows()
	{
		new DuplicateLinesValidator(-1);
	}
}
