package me.hugmanrique.cellarium.util;

import me.hugmanrique.cellarium.Key;
import me.hugmanrique.cellarium.Repository;

import java.util.function.UnaryOperator;

/**
 * Repository utilities related to {@link Enum} keys and values.
 *
 * <p>Some methods on this class can be used as remapping functions
 * when calling {@link Repository} {@code compute} methods.
 */
public final class EnumValues {

    /**
     * Returns the enum element {@code offset} positions away from the
     * specified previous element (by declaration order). If the element
     * index falls out of bounds, resets to the first declared element.
     *
     * @param element initial enum element
     * @param offset how many elements to skip
     * @param <T> the enum type of the value
     * @return the enum element {@code offset} positions away from {@code element}
     */
    public static <T extends Enum<T>> T nextValue(T element, int offset) {
        T[] constants = element.getDeclaringClass().getEnumConstants();
        int previousIndex = -1;

        // Lineally search previous index
        for (int i = 0; i < constants.length; i++) {
            T constant = constants[i];

            if (constants[i] == element) {
                previousIndex = i;
                break;
            }
        }

        if (previousIndex == -1) {
            // The specified previous enum element was not found in the enum's
            // constant array. Maybe the user reflectively instantiated an
            // enum instance?
            throw new AssertionError("Enum instance not found in constant array");
        }

        int newIndex = Math.floorMod(previousIndex + offset, constants.length);

        return constants[newIndex];
    }

    /**
     * Returns the next enum element relative to the specified element
     * (by declaration order). If {@code element} is the last declared
     * element, returns the first declared element.
     *
     * @param element initial enum element
     * @param <T> the enum type of the value
     * @return the next enum element relative to {@code element}
     * @see Repository#compute(Key, UnaryOperator)
     */
    public static <T extends Enum<T>> T nextValue(T element) {
        return nextValue(element, 1);
    }

    /**
     * Returns the previous enum element relative to the specified element
     * (by declaration order). If {@code element} is the first declared
     * element, returns the last declared element.
     *
     * @param element initial enum element
     * @param <T> the enum type of the value
     * @return the previous enum element relative to {@code element}
     * @see Repository#compute(Key, UnaryOperator)
     */
    public static <T extends Enum<T>> T previousValue(T element) {
        return nextValue(element, -1);
    }

    private EnumValues() {
        throw new AssertionError();
    }
}
