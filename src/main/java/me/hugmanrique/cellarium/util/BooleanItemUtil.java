package me.hugmanrique.cellarium.util;

import me.hugmanrique.cellarium.Item;
import me.hugmanrique.cellarium.Repository;

import java.util.NoSuchElementException;

/**
 * Repository and item utilities related to booleans.
 */
public final class BooleanItemUtil {

    private BooleanItemUtil() {
        throw new AssertionError();
    }

    /**
     * Toggles the value of the given item.
     *
     * @param repository the repository to perform the operation on
     * @param item the item whose associated value is to be toggled
     * @throws NoSuchElementException if the item has no associated value nor default value
     * @return the item's value after toggling
     */
    public static boolean toggleValue(Repository repository, Item<Boolean> item) {
        boolean newValue = !repository.getValue(item).orElseThrow();

        repository.setValue(item, newValue);

        return newValue;
    }
}
