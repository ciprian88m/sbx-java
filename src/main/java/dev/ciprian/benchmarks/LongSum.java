package dev.ciprian.benchmarks;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, warmups = 2)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.AverageTime)
public class LongSum {

    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
    public long sumLoop(ExecutionPlan plan) {
        long sum = 0;
        for (var num : plan.nums) {
            sum += num;
        }
        return sum;
    }

    @Benchmark
    public long streamLoop(ExecutionPlan plan) {
        return Arrays.stream(plan.nums).sum();
    }

    @Benchmark
    public long parallelStreamLoop(ExecutionPlan plan) {
        return Arrays.stream(plan.nums).parallel().sum();
    }
}
