package dev.ciprian.javaspecialists.newsletter.shuffle;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;
import java.util.stream.Gatherer;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class ShuffleGathererTest {

    @Test
    void testPrimitiveShuffleCollector() {
        var shuffled = printRandom(0, 10, 5, ShuffleGatherer.of());
        IO.println(shuffled);
        assertThat(shuffled.size()).isEqualTo(5);
    }

    @Test
    void testRepeatablePrimitiveShuffleCollector() {
        var shuffled = printRandom(0, 10, 5, ShuffleGatherer.of(new Random(0)));
        assertThat(shuffled).containsExactly(4, 8, 9, 6, 3);
    }

    @Test
    void testRepeatablePrimitiveShuffleCollectorWithLimit() {
        var shuffled = printRandom(0, 1000, 3, ShuffleGatherer.of(new Random(0)));
        assertThat(shuffled).containsExactly(490, 539, 694);
    }

    @Test
    void testPrimitiveShuffleCollectorWithShuffleWindow() {
        var shuffled = printRandom(0, 10, 8, ShuffleGatherer.of(new Random(0), 3));
        assertThat(shuffled).containsExactly(2, 1, 0, 3, 5, 4, 7, 6);
    }

    private static List<Integer> printRandom(
            int from, int upto, int limit, Gatherer<Integer, List<Integer>, Integer> shuffler) {
        return IntStream.range(from, upto).boxed().gather(shuffler).limit(limit).toList();
    }
}
