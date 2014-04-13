package com.gparser.tests.jmh.validations;

import com.gparser.files.ChannelFileData;
import com.gparser.tests.mock.ChannelFileDataMockFactory;
import com.gparser.validation.CartesianProductValidator;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;

/**
 * Performance test for Cartesian product validation.
 * Created by Gilad Ber on 4/12/14.
 */
@State(Scope.Thread)
@SuppressWarnings("unused")
public class CartesianProductValidatorPerformanceTest
{
	public static final ChannelFileDataMockFactory CFD_FACTORY = new ChannelFileDataMockFactory();
	public static final String TITLE_INDICATOR = "%%";
	public static final String COMMENT_INDICATOR = "%#";
	private CartesianProductValidator validator;
	private ChannelFileData smallData;
	private ChannelFileData verySmallData;
	private ChannelFileData mediumData;
	private ChannelFileData largeData;
	private ChannelFileData largeNonCartesianData;

	@Setup(Level.Trial)
	public void init()
	{
		this.validator = new CartesianProductValidator(3);
		try
		{
			this.smallData = CFD_FACTORY.build("C:/parser/result_small.txt", COMMENT_INDICATOR, TITLE_INDICATOR);
			this.verySmallData = CFD_FACTORY.build("C:/parser/result_verysmall.txt", COMMENT_INDICATOR, TITLE_INDICATOR);
			this.mediumData = CFD_FACTORY.build("C:/parser/result_medium.txt", COMMENT_INDICATOR, TITLE_INDICATOR);
			this.largeData = CFD_FACTORY.build("C:/parser/result_large.txt", COMMENT_INDICATOR, TITLE_INDICATOR);
			this.largeNonCartesianData = CFD_FACTORY.build("C:/parser/result_large_non_cartesian.txt", COMMENT_INDICATOR, TITLE_INDICATOR);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@GenerateMicroBenchmark
	public void validateSmallCartesianData()
	{
		validator.validate(smallData);
	}

	@GenerateMicroBenchmark
	public void validateVerySmallCartesianData()
	{
		validator.validate(verySmallData);
	}

	@GenerateMicroBenchmark
	public void validateMediumCartesianData()
	{
		validator.validate(mediumData);
	}

	@GenerateMicroBenchmark
	public void validateLargeCartesianData()
	{
		validator.validate(largeData);
	}

	@GenerateMicroBenchmark
	public void validateLargeNonCartesianData()
	{
		validator.validate(largeNonCartesianData);
	}
}
