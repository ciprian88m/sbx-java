package dev.ciprian;

import dev.ciprian.immutable.Foobar;
import dev.ciprian.immutable.ImmutableFoobar;

public class Main {
    public static void main(String[] args) {
        Foobar foobar =
                ImmutableFoobar.builder().foo(2).bar("Bar").addBuz(1, 3, 4).build();

        System.out.println("foobar.foo() = " + foobar.foo());
    }
}
