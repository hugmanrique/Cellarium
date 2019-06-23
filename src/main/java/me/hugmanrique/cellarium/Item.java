package me.hugmanrique.cellarium;

import java.util.Optional;

/**
 * Represents an item that can be associated with values in repositories.
 *
 * @param <T> the type of values that can be associated with this item
 */
public interface Item<T> {

    /**
     * Returns the ID of this item.
     *
     * @return this item's ID
     */
    String getId();

    /**
     * Returns the type token of this item.
     *
     * @return this item's type token
     */
    Class<T> getType();

    /**
     * Returns the optional default value associated to this item.
     *
     * @return this item's default value, or {@link Optional#empty()} if not defined
     */
    Optional<T> getDefaultValue();

    /**
     * Casts an object to the class represented by {@link #getType()}.
     *
     * @param object the object to be cast
     * @return the object after casting, or {@code null} if object is null
     * @throws ClassCastException if the object is not assignable to {@link #getType()}
     * @see Class#cast(Object)
     */
    default T cast(Object object) {
        return getType().cast(object);
    }
}
