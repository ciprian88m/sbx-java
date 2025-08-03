package dev.ciprian.javaspecialists.newsletter.sequenced;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SequencedLinkedHashSetTest {

    @Test
    void testAddFirstAndAddLast() {
        var list = new ArrayList<>();
        Collections.addAll(list, "one", "two", "three");

        var reversedList = list.reversed();
        reversedList.addFirst("first");
        reversedList.addLast("last");

        assertThat(list).containsExactlyElementsOf(List.of("last", "one", "two", "three", "first"));

        var set = new LinkedHashSet<>();
        Collections.addAll(set, "one", "two", "three");

        var reversedSet = set.reversed();
        reversedSet.addFirst("first");
        reversedSet.addLast("last");

        assertThat(set).containsExactlyElementsOf(List.of("last", "one", "two", "three", "first"));
        assertThat(set).containsExactlyElementsOf(list);
    }

    @Test
    void testAdd() {
        var list = new ArrayList<>();
        Collections.addAll(list, "one", "two", "three");

        var reversedList = list.reversed();
        list.add("list.add()");
        reversedList.add("reversed.add()");

        assertThat(list).containsExactlyElementsOf(List.of("reversed.add()", "one", "two", "three", "list.add()"));

        var set = new LinkedHashSet<>();
        Collections.addAll(set, "one", "two", "three");

        var reversedSet = set.reversed();
        set.add("set.add()");
        reversedSet.add("reversed.add()");

        assertThat(set).containsExactlyElementsOf(List.of("one", "two", "three", "set.add()", "reversed.add()"));
        // cannot equal because 'reversed.add()' adds at the end of the set, not at the beginning
    }
}
