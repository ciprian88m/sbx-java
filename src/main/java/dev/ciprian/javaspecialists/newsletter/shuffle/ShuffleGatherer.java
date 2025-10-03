package dev.ciprian.javaspecialists.newsletter.shuffle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;
import java.util.stream.Gatherer;

public class ShuffleGatherer {

    public static <T> Gatherer<T, List<T>, T> of() {
        return of(ThreadLocalRandom.current());
    }

    public static <T> Gatherer<T, List<T>, T> of(int windowSize) {
        return of(ThreadLocalRandom.current(), windowSize);
    }

    public static <T> Gatherer<T, List<T>, T> of(RandomGenerator random) {
        return of(random, Integer.MAX_VALUE - 8);
    }

    public static <T> Gatherer<T, List<T>, T> of(RandomGenerator random, int windowSize) {
        return Gatherer.ofSequential(
                ArrayList::new,
                (list, element, downstream) -> {
                    list.add(element);
                    if (list.size() == windowSize) {
                        shuffleAndSend(random, list, downstream);
                    }
                    return true;
                },
                (list, downstream) -> shuffleAndSend(random, list, downstream));
    }

    private static <T> void shuffleAndSend(
            RandomGenerator random, List<T> list, Gatherer.Downstream<? super T> downstream) {
        Collections.shuffle(list, random);
        list.stream().takeWhile(_ -> !downstream.isRejecting()).forEach(downstream::push);
        list.clear();
    }
}
