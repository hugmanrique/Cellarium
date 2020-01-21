package me.hugmanrique.cellarium.tests;

import me.hugmanrique.cellarium.v2.simple.SimpleKey;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleKeyTests {

    // Builder tests

    @Test
    void testBuildWithoutDefault() {
        SimpleKey<String> foo = new SimpleKey.Builder<>(String.class).build();

        assertEquals(String.class, foo.type());
        assertNull(foo.defaultValue());
    }

    @Test
    void testBuildWithDefault() {
        SimpleKey<Integer> foo = new SimpleKey.Builder<>(Integer.class)
                .defaultValue(15)
                .build();

        assertEquals(Integer.class, foo.type());
        assertEquals(15, foo.defaultValue());
    }

    @Test
    void testNullValueTypeThrows() {
        assertThrows(NullPointerException.class, () -> {
            new SimpleKey.Builder<>(null);
        });
    }

    @Test
    void testBuilderReuse() {
        SimpleKey.Builder<String> builder = new SimpleKey.Builder<>(String.class);

        SimpleKey<String> foo = builder.defaultValue("foo").build();
        SimpleKey<String> bar = builder.defaultValue("bar").build();

        assertEquals(String.class, foo.type());
        assertEquals(String.class, bar.type());
        assertEquals("foo", foo.defaultValue());
        assertEquals("bar", bar.defaultValue());
    }

    // Key tests

    @Test
    void testKeyCast() {
        SimpleKey<String> foo = new SimpleKey.Builder<>(String.class).build();

        assertEquals("test", foo.cast("test"));
        assertNull(foo.cast(null));
        assertThrows(ClassCastException.class, () -> {
            foo.cast(123);
        });
    }
}
