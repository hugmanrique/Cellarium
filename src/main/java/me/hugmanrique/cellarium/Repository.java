package me.hugmanrique.cellarium;

import java.util.Optional;

/**
 * Represents a typesafe heterogeneous container of items.
 */
public interface Repository {

    /**
     * Returns {@code true} if the given item has an associated value in this repository.
     *
     * @param item the item whose presence in the repository is to be tested
     * @return {@code true} if the given item has an associated value
     */
    boolean hasValue(Item<?> item);

    /**
     * Returns the value of the given item. If no value is set,
     * returns the item's default value (which can be null, in which
     * case {@link Optional#empty()} is returned).
     *
     * @param item the item whose associated value is to be returned
     * @param <T> the value's type
     * @return the value of the given item, or {@link Optional#empty()} if no value and default value are set
     */
    <T> Optional<T> getValue(Item<T> item);

    /**
     * Sets the value for the given item. If the item already had an associated
     * value, the old value is replaced by the specified value.
     *
     * @param item the item whose associated value is to be set
     * @param value the new value
     * @param <T> the value's type
     * @return the previous item's value, or {@link Optional#empty()} if no value was set
     */
    <T> Optional<T> setValue(Item<T> item, T value);

    /**
     * Removes the value associated with the given item.
     *
     * @param item the item whose associated value is to be removed
     * @param <T> the value's type
     * @return the previous item's value, or {@link Optional#empty()} if no value was set
     */
    <T> Optional<T> removeValue(Item<T> item);
}
