package dev.ciprian.javaspecialists.newsletter.distinctify;

import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.ToIntFunction;

public abstract class DistinctifyStrings {

    public static final ToIntFunction<String> HASH_CODE = s -> s.toUpperCase().hashCode();

    public static final BiPredicate<String, String> EQUALS =
            (s1, s2) -> s1.toUpperCase().equals(s2.toUpperCase());

    public static final BinaryOperator<String> MERGE =
            (s1, s2) -> s1.chars().sum() < s2.chars().sum() ? s2 : s1;
}
