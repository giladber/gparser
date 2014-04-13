package com.gparser.tests.jmh;

import org.openjdk.jmh.logic.results.Result;
import org.openjdk.jmh.logic.results.RunResult;
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
			shouldDoGC(true).
			warmupTime(TimeValue.seconds(10)).
			measurementTime(TimeValue.seconds(15)).
			warmupIterations(1).
			measurementIterations(3).
			jvmArgs("-server", "-Xmx1024m", "-Xms256m").
			build();

		Map<BenchmarkRecord, RunResult> records = new Runner(opts).run();
		records.entrySet().stream().
			map(e -> e.getValue().getPrimaryResult()).
			forEach(JMHTestsMain::printResult);
	}

	private static void printResult(Result result)
	{
		System.out.println("Benchmark " + result.getLabel() + " score: " + result.getScore() +
			" " + result.getScoreUnit() + " over " + result.getStatistics().getN() + " iterations");
	}
}
