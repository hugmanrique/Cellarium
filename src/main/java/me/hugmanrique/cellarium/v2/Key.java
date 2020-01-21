package me.hugmanrique.cellarium.v2;

import java.util.Optional;

/**
 * A parameterized key that can be mapped to values in a {@link Repository}.
 *
 * @param <T> the type token of value instances that can be mapped from this key
 */
public interface Key<T> {

    /**
     * Returns the type token of value instances that can be mapped from this key.
     *
     * @return the type token of value instances
     */
    Class<T> type();

    /**
     * Returns the default value associated to this key.
     *
     * @return the default value of this key, or {@link Optional#empty()} if not defined
     */
    Optional<T> defaultValue();

    /**
     * Casts an object to the type referenced by this key's {@link #type()} object.
     *
     * @param object the object to be cast
     * @return the object after casting, or {@code null} if {@code object} is {@code null}
     * @throws ClassCastException if the object is not null and is not assignable to {@link #type()}
     */
    default T cast(Object object) {
        return type().cast(object);
    }
}
