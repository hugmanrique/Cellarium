package me.hugmanrique.cellarium.simple;

import me.hugmanrique.cellarium.Item;

import java.util.Objects;
import java.util.Optional;

public class SimpleItem<T> implements Item<T> {

    private final String id;
    private final Class<T> type;
    private final T defaultValue;

    private SimpleItem(Builder<T> builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.defaultValue = builder.defaultValue;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public Optional<T> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    public static class Builder<T> {
        private final String id;
        private final Class<T> type;

        private T defaultValue;

        public Builder(String id, Class<T> type) {
            this.id = Objects.requireNonNull(id);
            this.type = Objects.requireNonNull(type);
        }

        public void defaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
        }

        public SimpleItem<T> build() {
            return new SimpleItem<>(this);
        }
    }
}
