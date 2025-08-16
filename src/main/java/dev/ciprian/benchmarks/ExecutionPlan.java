package dev.ciprian.benchmarks;

import java.util.stream.LongStream;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class ExecutionPlan {

    public long[] nums;

    @Setup(Level.Invocation)
    public void setUp() {
        nums = LongStream.range(0, 10_000_000).toArray();
    }
}
