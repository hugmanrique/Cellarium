package me.hugmanrique.cellarium;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

/**
 * An immutable parameterized key that can be mapped to values in a {@link Repository}.
 *
 * @param <T> the type of value instances that can be mapped from this key
 */
@ThreadSafe
public interface Key<T> {

    /**
     * Returns the type of value instances that can be mapped from this key.
     *
     * @return the type of value instances
     */
    Class<T> type();

    /**
     * Returns the default value associated to this key, or {@code null} if
     * not defined.
     *
     * @return the default value of this key, or {@code null} if not defined
     */
    @Nullable
    T defaultValue();

    /**
     * Casts an object to the type referenced by this key's {@link #type()} object.
     *
     * @param object the object to be cast
     * @return the object after casting, or {@code null} if {@code object} is {@code null}
     * @throws ClassCastException if the object is not null and is not assignable to {@link #type()}
     */
    default T cast(@Nullable Object object) {
        return type().cast(object);
    }
}
