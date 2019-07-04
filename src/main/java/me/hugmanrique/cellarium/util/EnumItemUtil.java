package me.hugmanrique.cellarium.util;

import me.hugmanrique.cellarium.Item;
import me.hugmanrique.cellarium.Repository;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Repository and item utilities related to enumerations.
 */
public final class EnumItemUtil {

    private EnumItemUtil() {
        throw new AssertionError();
    }

    /**
     * Returns the next enum's constant of {@code Class<T>} by ordinal order.
     * If {@code previous} is the last enum constant, returns the first constant.
     *
     * @param item the item whose associated value is to be updated
     * @param previous the previous enum constant value
     * @param <T> the enum's type
     * @return the new item's value
     */
    public static <T extends Enum<?>> T getNextValue(Item<T> item, T previous) {
        T[] constants = item.getType().getEnumConstants();
        int index = Arrays.asList(constants).indexOf(previous);

        if (index == -1) {
            // Repository is type-safe which means it will always return an enum's constant
            throw new AssertionError();
        }

        int newIndex = index + 1;

        if (newIndex >= constants.length) {
            // Cycle back to first item
            newIndex = 0;
        }

        return constants[newIndex];
    }

    /**
     * Gets the next enum's constant of {@code Class<T>}, and sets it
     * as the value of the given item.
     *
     * @param repository the repository to perform the operation on
     * @param item the item whose associated value is to be updated
     * @param <T> the enum's type
     * @throws NoSuchElementException if the item has no associated value nor default value
     * @return the new item's value
     */
    @Deprecated
    public static <T extends Enum<?>> T setNextValue(Repository repository, Item<T> item) {
        T currentValue = repository.getValue(item).orElseThrow();
        T newValue = getNextValue(item, currentValue);

        repository.setValue(item, newValue);

        return newValue;
    }
}
