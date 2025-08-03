package dev.ciprian.javaspecialists.newsletter.distinctify;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.ToIntFunction;
import java.util.stream.Gatherer;

public class DistinctifyGatherer {

    public static <T> Gatherer<T, ?, T> of(
            ToIntFunction<T> hashCode, BiPredicate<T, T> equals, BinaryOperator<T> merger) {

        class Key {
            private final T t;

            public Key(T t) {
                this.t = t;
            }

            @Override
            public int hashCode() {
                return hashCode.applyAsInt(t);
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof Key that && equals.test(this.t, that.t);
            }
        }

        return Gatherer.<T, Map<Key, Key>, T>ofSequential(
                LinkedHashMap::new,
                (state, element, ignored) -> {
                    var key = new Key(element);

                    var existing = state.get(key);

                    if (existing != null) {
                        T merged = merger.apply(existing.t, key.t);
                        key = new Key(merged);
                    }

                    state.put(key, key);

                    return true;
                },
                (keys, downstream) -> keys.values().stream()
                        .takeWhile(ignored -> !downstream.isRejecting())
                        .map(key -> key.t)
                        .forEach(downstream::push));
    }
}
