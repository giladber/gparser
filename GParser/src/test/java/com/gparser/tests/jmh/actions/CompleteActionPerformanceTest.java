package com.gparser.tests.jmh.actions;

import com.gparser.actions.CompleteAction;
import com.gparser.files.ChannelFileData;
import com.gparser.tests.mock.ChannelFileDataFactory;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;

/**
 * Performance test for complete channel action.
 * Created by Gilad Ber on 4/12/14.
 */
@State(Scope.Thread)
@SuppressWarnings("unused")
public class CompleteActionPerformanceTest
{
	public static final ChannelFileDataFactory CFD_FACTORY = new ChannelFileDataFactory();
	public static final String TITLE_INDICATOR = "%%";
	public static final String COMMENT_INDICATOR = "%#";
	private ChannelFileData smallData;
	private ChannelFileData verySmallData;
	private ChannelFileData mediumData;
	private ChannelFileData largeData;
	private CompleteAction action;

	@Setup(Level.Trial)
	public void init()
	{
		try
		{
			this.smallData = CFD_FACTORY.build("C:/parser/result_small.txt", COMMENT_INDICATOR, TITLE_INDICATOR);
			this.verySmallData = CFD_FACTORY.build("C:/parser/result_verysmall.txt", COMMENT_INDICATOR, TITLE_INDICATOR);
			this.mediumData = CFD_FACTORY.build("C:/parser/result_medium.txt", COMMENT_INDICATOR, TITLE_INDICATOR);
			this.largeData = CFD_FACTORY.build("C:/parser/result_large.txt", COMMENT_INDICATOR, TITLE_INDICATOR);
			this.action = new CompleteAction(3, -33, 3);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@GenerateMicroBenchmark
	public void testCompleteSmallData()
	{
		action.apply(smallData);
	}

	@GenerateMicroBenchmark
	public void testCompleteVerySmallData()
	{
		action.apply(verySmallData);
	}

	@GenerateMicroBenchmark
	public void testCompleteMediumData()
	{
		action.apply(mediumData);
	}

	@GenerateMicroBenchmark
	public void testCompleteLargeData()
	{
		action.apply(largeData);
	}
}
