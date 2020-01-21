package me.hugmanrique.cellarium.util;

import me.hugmanrique.cellarium.Key;
import me.hugmanrique.cellarium.Repository;

import java.util.function.UnaryOperator;

/**
 * Repository utilities related to integer keys and values.
 *
 * <p>Some methods on this class can be used as remapping functions
 * when calling {@link Repository} {@code compute} methods.
 */
public final class IntegerValues {

    /**
     * Attempts to compute a new value for the specified key by adding its current
     * mapped value (the key's default value or zero if there is no current mapping)
     * and the specified addend.
     *
     * <p>The computation can overflow.
     *
     * @param repository repository whose mappings are to be modified
     * @param key key with which the computed value is to be associated
     * @param addend value to be added to the current mapped value
     * @return the new value associated with the specified key
     */
    public static int increaseBy(Repository repository, Key<Integer> key, int addend) {
        //noinspection ConstantConditions
        return repository.compute(key, previous -> {
            if (previous == null) {
                // Start counting at zero
                previous = 0;
            }

            return previous + addend;
        });
    }

    /**
     * Attempts to compute a new value for the specified key by subtracting the
     * specified subtrahend from its current mapped value (the key's default value
     * or zero if there is no current mapping).
     *
     * <p>The computation can underflow.
     *
     * @param repository repository whose mappings are to be modified
     * @param key key with which the computed value is to be associated
     * @param subtrahend value to be subtracted from the current mapped value
     * @return the new value associated with the specified key
     */
    public static int decreaseBy(Repository repository, Key<Integer> key, int subtrahend) {
        return increaseBy(repository, key, -subtrahend);
    }

    /**
     * Returns the given value {@code + 1}. This method can overflow.
     *
     * @param value the operand
     * @return the incremented value
     * @see Repository#compute(Key, UnaryOperator)
     */
    public static int increment(int value) {
        return ++value;
    }

    /**
     * Returns the given value {@code - 1}. This method can underflow.
     *
     * @param value the operand
     * @return the decremented value
     * @see Repository#compute(Key, UnaryOperator)
     */
    public static int decrement(int value) {
        return --value;
    }

    private IntegerValues() {
        throw new AssertionError();
    }
}
