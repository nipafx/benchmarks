package dev.nipafx.lab.benchmarks.stream.vectorization;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Fork(value = 1)
@Warmup(iterations = 2, time = 30, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 2, time = 30, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class VectorizationBenchmark {

	@Param({ "false", "true" })
	private boolean parallel = false;

	@Param({ "200000" })
	private int number = 200_000;

	@Benchmark
	public BigInteger factorial() {
		var numbers = IntStream
				.rangeClosed(1, number)
				.mapToObj(BigInteger::valueOf);
		if (parallel)
			numbers = numbers.parallel();
		return numbers.reduce(BigInteger.ONE, BigInteger::multiply);
	}

}
