package me.hugmanrique.cellarium.v2.simple;

import me.hugmanrique.cellarium.v2.Key;
import me.hugmanrique.cellarium.v2.Repository;

import java.util.Objects;
import java.util.Optional;

/**
 * An immutable {@link Key}.
 *
 * @param <T> the type of value instances that can be mapped from this key
 * @see Builder to create instances of this class
 */
public class SimpleKey<T> implements Key<T> {

    private final Class<T> type;
    // Since the default value is immutable, we can cache the Optional,
    // avoiding object allocations when calling #defaultValue.
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<T> defaultValue;

    private SimpleKey(Builder<T> builder) {
        this.type = builder.type;
        this.defaultValue = Optional.ofNullable(builder.defaultValue);
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public Optional<T> defaultValue() {
        return defaultValue;
    }

    /**
     * Used to build instances of {@link SimpleKey} from values configured by
     * the setters. A {@link SimpleKey} object created by a {@code Builder} is
     * well-formed.
     *
     * @param <T> the type of value instances
     */
    public static class Builder<T> {

        private final Class<T> type;
        private T defaultValue;

        /**
         * Construct a new {@code Builder} that can create instances of {@link SimpleKey}
         * that can be mapped to values with the specified value type in a
         * {@link Repository}.
         *
         * @param type type of value instances that can be mapped from the
         *             built {@link SimpleKey}
         */
        public Builder(Class<T> type) {
            this.type = Objects.requireNonNull(type, "type");
        }

        /**
         * Specifies the default value returned from a {@link Repository} when it
         * contains no mapping for the key.
         *
         * @param defaultValue the default value associated to the key
         * @return this builder
         */
        public Builder<T> defaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        /**
         * Creates a {@link SimpleKey}.
         *
         * <p>This method does not alter the state of this {@link Builder} instance, so
         * it can be invoked again to create multiple independent keys.
         *
         * @return a {@link SimpleKey} having the specified values
         */
        public SimpleKey<T> build() {
            return new SimpleKey<>(this);
        }
    }
}
