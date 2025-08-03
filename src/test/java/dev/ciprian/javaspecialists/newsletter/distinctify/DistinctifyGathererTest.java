package dev.ciprian.javaspecialists.newsletter.distinctify;

import static dev.ciprian.javaspecialists.newsletter.distinctify.DistinctifyMethods.METHOD_COMPARATOR;
import static dev.ciprian.javaspecialists.newsletter.distinctify.DistinctifyMethods.METHOD_RETURN_TYPE_AND_PARAMS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class DistinctifyGathererTest {

    @Test
    public void testBeaches() {
        var beaches = Stream.of("Kalathas", "Stavros", "STAVROS", "marathi", "kalathas", "baLos", "Balos")
                .gather(DistinctifyGatherer.of(
                        DistinctifyStrings.HASH_CODE, DistinctifyStrings.EQUALS, DistinctifyStrings.MERGE))
                .toList();

        assertThat(beaches).containsExactlyElementsOf(List.of("kalathas", "Stavros", "marathi", "baLos"));
    }

    @Test
    public void testArrayDequeMethods() {
        var allMethods = Stream.of(ArrayDeque.class.getMethods())
                .filter(method -> method.getName().equals("clone"))
                .map(METHOD_RETURN_TYPE_AND_PARAMS)
                .toList();

        var distinct = Stream.of(ArrayDeque.class.getMethods())
                .filter(method -> method.getName().equals("clone"))
                .gather(DistinctifyGatherer.of(
                        DistinctifyMethods.HASH_CODE, DistinctifyMethods.EQUALS, DistinctifyMethods.MERGE))
                .map(METHOD_RETURN_TYPE_AND_PARAMS)
                .toList();

        assertThat(allMethods).containsExactlyElementsOf(List.of("ArrayDeque clone()", "Object clone()"));
        assertThat(distinct).containsExactlyElementsOf(List.of("ArrayDeque clone()"));
    }

    @Test
    public void testConcurrentSkipListSetMethods() {
        var allMethods = Stream.of(ConcurrentSkipListSet.class.getMethods())
                .filter(method -> method.getName().contains("Set"))
                .sorted(METHOD_COMPARATOR)
                .map(METHOD_RETURN_TYPE_AND_PARAMS)
                .toList();

        var distinct = Stream.of(ConcurrentSkipListSet.class.getMethods())
                .filter(method -> method.getName().contains("Set"))
                .gather(DistinctifyGatherer.of(
                        DistinctifyMethods.HASH_CODE, DistinctifyMethods.EQUALS, DistinctifyMethods.MERGE))
                .sorted(METHOD_COMPARATOR)
                .map(METHOD_RETURN_TYPE_AND_PARAMS)
                .toList();

        assertThat(allMethods)
                .containsExactlyElementsOf(List.of(
                        "NavigableSet descendingSet()",
                        "NavigableSet headSet(Object, boolean)",
                        "NavigableSet headSet(Object)",
                        "SortedSet headSet(Object)",
                        "NavigableSet subSet(Object, boolean, Object, boolean)",
                        "SortedSet subSet(Object, Object)",
                        "NavigableSet subSet(Object, Object)",
                        "NavigableSet tailSet(Object, boolean)",
                        "NavigableSet tailSet(Object)",
                        "SortedSet tailSet(Object)"));
        assertThat(distinct)
                .containsExactlyElementsOf(List.of(
                        "NavigableSet descendingSet()",
                        "NavigableSet headSet(Object, boolean)",
                        "NavigableSet headSet(Object)",
                        "NavigableSet subSet(Object, boolean, Object, boolean)",
                        "NavigableSet subSet(Object, Object)",
                        "NavigableSet tailSet(Object, boolean)",
                        "NavigableSet tailSet(Object)"));
    }
}
