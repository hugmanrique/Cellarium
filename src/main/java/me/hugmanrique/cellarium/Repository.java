package me.hugmanrique.cellarium;

import me.hugmanrique.cellarium.simple.SimpleRepository;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

/**
 * Represents a typesafe heterogeneous container of items.
 */
public interface Repository {

    /**
     * Creates a new empty repository instance.
     *
     * @return a new empty repository instance
     */
    static Repository create() {
        return new SimpleRepository();
    }

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
     * Applies the given operation on the current item's value. The produced result
     * replaces the old value. If no value is set, the item's default value
     * (which can be null) gets passed as the operand.
     *
     * @param item the item whose associated value is the operator
     * @param operation the operation to apply to the current item's value
     * @param <T> the value's type
     * @return the new item's value returned by {@code operation}
     */
    <T> Optional<T> apply(Item<T> item, UnaryOperator<T> operation);

    /**
     * Applies the given operation on the current item's value. The produced result
     * replaces the old value. If no value is set, the item's default value
     * (which can be null) gets passed as the operand.
     *
     * @param item the item whose associated value is the operator. The first {@link BiFunction#apply(Object, Object)} argument
     * @param operation the operation to apply to the current item's value
     * @param <T> the value's type
     * @return the new item's value returned by {@code operation}
     */
    <T> Optional<T> apply(Item<T> item, BiFunction<Item<T>, T, T> operation);

    /**
     * Removes the value associated with the given item.
     *
     * @param item the item whose associated value is to be removed
     * @param <T> the value's type
     * @return the previous item's value, or {@link Optional#empty()} if no value was set
     */
    <T> Optional<T> removeValue(Item<T> item);
}
