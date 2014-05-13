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
 * Unit tests for CartesianProductValidator class.
 * Created by Gilad Ber on 5/13/14.
 */
public class CartesianProductValidatorTest
{

	private static ChannelFileData largeCartesianData;
	private static ChannelFileData largeNonCartesianData;
	private static ChannelFileData smallCartesianData;
	private static ChannelFileData duplicateLinesData;

	private static CartesianProductValidator val0;
	private static CartesianProductValidator val1;
	private static CartesianProductValidator val2;
	private static CartesianProductValidator val3;
	private static CartesianProductValidator val4;

	@BeforeClass
	public static void setup() throws IOException
	{
		ChannelFileDataFactory factory = new ChannelFileDataFactory();
		largeCartesianData = factory.build(getResourceLocation("result_large.txt"), "%#", "%%");
		largeNonCartesianData = factory.build(getResourceLocation("result_large_non_cartesian.txt"), "%#", "%%");
		smallCartesianData = factory.build(getResourceLocation("result_small.txt"), "%#", "%%");
		duplicateLinesData = factory.build(getResourceLocation("result_duplicate_lines.txt"), "%#", "%%");

		val0 = new CartesianProductValidator(0);
		val1 = new CartesianProductValidator(1);
		val2 = new CartesianProductValidator(2);
		val3 = new CartesianProductValidator(3);
		val4 = new CartesianProductValidator(4);
	}

	@Test
	public void testEmpty()
	{
		ChannelFileData empty = ChannelFileData.empty();
		CartesianProductValidator v = new CartesianProductValidator(5);
		assertTrue(v.validate(Optional.of(empty)).isSucceeded());
		assertTrue(v.validate(Optional.empty()).isSucceeded());
	}

	@Test
	public void testRegular() throws IOException
	{
		assertTrue(val3.validate(Optional.of(largeCartesianData)).isSucceeded());
	}

	@Test
	public void testNonCartesian() throws IOException
	{
		assertFalse(val3.validate(Optional.of(largeNonCartesianData)).isSucceeded());
	}

	@Test
	public void testDuplicateLines() throws IOException
	{
		assertFalse(val3.validate(Optional.of(duplicateLinesData)).isSucceeded());
	}

	@Test
	public void testLessIndependentVars() throws IOException
	{
		Optional<ChannelFileData> opt = Optional.of(smallCartesianData);

		assertFalse(val2.validate(opt).isSucceeded());
		assertFalse(val1.validate(opt).isSucceeded());
	}

	@Test
	public void testMoreIndependentVars() throws IOException
	{
		assertFalse(val4.validate(Optional.of(smallCartesianData)).isSucceeded());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeChannelsThrows()
	{
		new CartesianProductValidator(-1);
	}

	@Test
	public void testZeroChannelsValidator()
	{
		assertTrue(val0.validate(Optional.empty()).isSucceeded());
		assertTrue(val0.validate(Optional.of(ChannelFileData.empty())).isSucceeded());
		assertFalse(val0.validate(Optional.of(smallCartesianData)).isSucceeded());
		assertFalse(val0.validate(Optional.of(largeCartesianData)).isSucceeded());
		assertFalse(val0.validate(Optional.of(largeNonCartesianData)).isSucceeded());
		assertFalse(val0.validate(Optional.of(duplicateLinesData)).isSucceeded());

	}
}
