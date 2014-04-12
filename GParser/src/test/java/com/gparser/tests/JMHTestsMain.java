package com.gparser.tests;

import org.openjdk.jmh.logic.results.Result;
import org.openjdk.jmh.logic.results.RunResult;
import org.openjdk.jmh.output.OutputFormatType;
import org.openjdk.jmh.runner.BenchmarkRecord;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.parameters.TimeValue;

import java.util.Map;

/**
 * Main class for executing JMH tests.
 * Created by Gilad Ber on 4/12/14.
 */
public class JMHTestsMain
{


	public static void main(String[] args) throws RunnerException
	{
		Options opts = new OptionsBuilder().
			include(".*").
			forks(1).
			syncIterations(false).
			shouldDoGC(true).
			warmupTime(TimeValue.seconds(10)).
			measurementTime(TimeValue.seconds(15)).
			warmupIterations(1).
			measurementIterations(3).
			jvmArgs("-client -Xms64m -Xmx1024m").
			outputFormat(OutputFormatType.TextReport).
			build();

		Map<BenchmarkRecord, RunResult> records = new Runner(opts).run();
		records.entrySet().stream().
			map(e -> e.getValue().getPrimaryResult()).
			forEach(JMHTestsMain::printResult);
	}

	private static <T extends Result<T>> void printResult(Result<T> result)
	{
		System.out.println("Benchmark score: " + result.getScore() +
			" " + result.getScoreUnit() + " over " + result.getStatistics().getN() + " iterations");
	}
}
