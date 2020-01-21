package me.hugmanrique.cellarium.oldutil;

/**
 * Repository and item utilities related to booleans.
 */
public final class BooleanItems {

    private BooleanItems() {
        throw new AssertionError();
    }

    /**
     * Returns the opposite of the given value.
     *
     * @param value the value to invert
     * @return the opposite value
     */
    public static boolean opposite(boolean value) {
        return !value;
    }
}
