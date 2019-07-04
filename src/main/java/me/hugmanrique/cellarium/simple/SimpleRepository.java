package me.hugmanrique.cellarium.simple;

import me.hugmanrique.cellarium.Item;
import me.hugmanrique.cellarium.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class SimpleRepository implements Repository {

    private final Map<Item<?>, Object> items = new HashMap<>();

    @Override
    public boolean hasValue(Item<?> item) {
        return items.containsKey(item);
    }

    @Override
    public <T> Optional<T> getValue(Item<T> item) {
        Objects.requireNonNull(item);

        return Optional
                // Achieve runtime type safety with dynamic cast
                .ofNullable(item.cast(items.get(item)))
                .or(item::getDefaultValue);
    }

    @Override
    public <T> Optional<T> setValue(Item<T> item, T value) {
        Objects.requireNonNull(item);

        return Optional.ofNullable(
                // Achieve runtime type safety with dynamic casts
                item.cast(items.put(item, item.cast(value)))
        );
    }

    @Override
    public <T> Optional<T> apply(Item<T> item, UnaryOperator<T> operation) {
        Objects.requireNonNull(operation);

        T currentValue = getValue(item).orElse(null);

        return setValue(item, operation.apply(currentValue));
    }

    @Override
    public <T> Optional<T> removeValue(Item<T> item) {
        Objects.requireNonNull(item);

        return Optional.ofNullable(
            item.getType().cast(items.remove(item))
        );
    }
}
