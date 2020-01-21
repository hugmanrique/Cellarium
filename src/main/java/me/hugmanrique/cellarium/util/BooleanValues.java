package me.hugmanrique.cellarium.util;

import me.hugmanrique.cellarium.Repository;

/**
 * Repository utilities related to boolean keys and values.
 *
 * <p>Some methods on this class can be used as remapping functions
 * when calling {@link Repository} {@code compute} methods.
 */
public final class BooleanValues {

    /**
     * Returns the opposite of the given value.
     *
     * @param value the value to invert
     * @return the opposite value
     */
    public static boolean opposite(boolean value) {
        return !value;
    }

    private BooleanValues() {
        throw new AssertionError();
    }
}
