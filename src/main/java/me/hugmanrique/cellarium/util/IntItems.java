package me.hugmanrique.cellarium.util;

import me.hugmanrique.cellarium.Item;
import me.hugmanrique.cellarium.Repository;

import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

/**
 * Repository and item utilities related to integers.
 */
public final class IntItems {

    private IntItems() {
        throw new AssertionError();
    }

    /**
     * Increments the value for the given item by {@code addend}.
     *
     * @param repository the repository to perform the operation on
     * @param item the item whose associated value is to be incremented
     * @param addend value to be added to the current value of item
     * @throws NoSuchElementException if the item has no associated value nor default value
     * @return the item's value after incrementation
     */
    public static int addToValue(Repository repository, Item<Integer> item, int addend) {
        int oldValue = repository.getValue(item).get();
        int newValue = oldValue + addend;

        repository.setValue(item, newValue);

        return newValue;
    }

    /**
     * Decrements the value for the given item by {@code subtrahend}.
     *
     * @param repository the repository to perform the operation on
     * @param item the item whose associated value is to be decremented
     * @param subtrahend value to be subtracted from the current value of item
     * @throws NoSuchElementException if the item has no associated value nor default value
     * @return the item's value after subtraction
     */
    public static int subtractFromValue(Repository repository, Item<Integer> item, int subtrahend) {
        return addToValue(repository, item, -subtrahend);
    }

    /**
     * Increments the value for the given item by {@code 1}.
     *
     * @param repository the repository to perform the operation on
     * @param item the item whose associated value is to be incremented
     * @throws NoSuchElementException if the item has no associated value nor default value
     * @return the item's value after incrementation
     * @deprecated use {@link Repository#apply(Item, UnaryOperator)} with {@link #increment(int)} instead
     */
    @Deprecated
    public static int incrementValue(Repository repository, Item<Integer> item) {
        return addToValue(repository, item, 1);
    }

    /**
     * Returns the given value {@code + 1}.
     *
     * @param value the operand
     * @return the value after incrementation
     */
    public static int increment(int value) {
        return value + 1;
    }
}
