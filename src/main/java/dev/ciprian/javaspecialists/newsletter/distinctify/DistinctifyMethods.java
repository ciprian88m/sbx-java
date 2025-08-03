package dev.ciprian.javaspecialists.newsletter.distinctify;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class DistinctifyMethods {

    public static final ToIntFunction<Method> HASH_CODE = m -> m.getName().hashCode() + m.getParameterCount();

    public static final BiPredicate<Method, Method> EQUALS =
            (m1, m2) -> m1.getName().equals(m2.getName())
                    && m1.getParameterCount() == m2.getParameterCount()
                    && Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes());

    public static final BinaryOperator<Method> MERGE = (m1, m2) -> {
        if (m1.getReturnType().isAssignableFrom(m2.getReturnType())) return m2;

        if (m2.getReturnType().isAssignableFrom(m1.getReturnType())) return m1;

        throw new IllegalArgumentException(
                "Conflicting return types " + m1.getReturnType().getCanonicalName() + " and "
                        + m2.getReturnType().getCanonicalName());
    };

    public static final Comparator<Method> METHOD_COMPARATOR =
            Comparator.comparing(Method::getName).thenComparing(m -> Arrays.toString(m.getParameterTypes()));

    public static final Function<Method, String> METHOD_RETURN_TYPE_AND_PARAMS = m -> Stream.of(m.getParameterTypes())
            .map(Class::getSimpleName)
            .collect(Collectors.joining(", ", m.getReturnType().getSimpleName() + " " + m.getName() + "(", ")"));
}
