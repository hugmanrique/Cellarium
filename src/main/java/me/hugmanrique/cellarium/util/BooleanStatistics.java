package me.hugmanrique.cellarium.util;

import me.hugmanrique.cellarium.Item;
import me.hugmanrique.cellarium.Repository;

import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;

/**
 * Repository and item utilities related to booleans.
 */
public final class BooleanStatistics {

    private BooleanStatistics() {
        throw new AssertionError();
    }

    /**
     * Toggles the value of the given item.
     *
     * @param repository the repository to perform the operation on
     * @param item the item whose associated value is to be toggled
     * @throws NoSuchElementException if the item has no associated value nor default value
     * @return the item's value after toggling
     * @deprecated use {@link Repository#apply(Item, UnaryOperator)} with {@link #opposite(boolean)}
     */
    @Deprecated
    public static boolean toggleValue(Repository repository, Item<Boolean> item) {
        boolean newValue = !repository.getValue(item).orElseThrow();

        repository.setValue(item, newValue);

        return newValue;
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
