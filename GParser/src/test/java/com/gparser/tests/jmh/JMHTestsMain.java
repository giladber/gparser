package com.gparser.tests.jmh;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Main class for executing JMH tests.
 * Created by Gilad Ber on 4/12/14.
 */
public class JMHTestsMain
{


	public static void main(String[] args) throws RunnerException
	{
		Options opts = new OptionsBuilder().
			include(".*concurrent.*").
			forks(1).
			shouldDoGC(true).
			warmupIterations(10).
			measurementIterations(10).
			//			measurementTime(TimeValue.seconds(5)).
			jvmArgs("-server", "-Xmx512m", "-Xms256m").
			build();

		new Runner(opts).run();
	}
}
