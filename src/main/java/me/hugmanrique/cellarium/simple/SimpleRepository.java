package me.hugmanrique.cellarium.simple;

import me.hugmanrique.cellarium.Key;
import me.hugmanrique.cellarium.Repository;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 * A simple {@link Repository} implementation.
 *
 * <p>A {@code SimpleRepository} is thread-safe only if the wrapped
 * {@link Map} is thread-safe.
 */
public class SimpleRepository implements Repository {

    /**
     * Creates a non thread-safe {@code SimpleRepository} based on
     * a {@link HashMap}.
     *
     * @return a non thread-safe {@code SimpleRepository}
     */
    public static SimpleRepository newInstance() {
        return newInstance(HashMap::new);
    }

    /**
     * Creates a thread-safe {@code SimpleRepository} based on
     * a {@link ConcurrentHashMap}.
     *
     * @return a thread-safe {@code SimpleRepository}
     */
    public static SimpleRepository newConcurrentInstance() {
        return newInstance(ConcurrentHashMap::new);
    }

    /**
     * Creates a {@code SimpleRepository} based on the {@link Map} returned
     * by the specified map supplier {@link Supplier#get()} method.
     *
     * <p>The returned {@code SimpleRepository} is thread-safe only if the
     * {@link Map} returned by the specified map supplier {@link Supplier#get()}
     * method is thread-safe. The behavior of the returned repository is
     * undefined if the supplied map is modified externally.
     *
     * @param mapSupplier an empty map supplier used to obtain a map which the
     *                    returned {@code SimpleRepository} will wrap
     * @return a new {@code SimpleRepository}
     * @throws NullPointerException if the map supplied by the specified supplier
     *                              is {@code null}
     * @throws IllegalArgumentException if the map supplied by the specified supplier
     *                                  is not empty
     */
    public static SimpleRepository newInstance(Supplier<Map<Key<?>, Object>> mapSupplier) {
        Map<Key<?>, Object> values = requireNonNull(mapSupplier.get(), "suppliedMap");

        // By checking if the supplied map is empty we're ensuring type-safety (unless the
        // map is modified externally) since Java's type system is not capable of expressing
        // the relation between key and value.
        if (!values.isEmpty()) {
            throw new IllegalArgumentException("Supplied map is not empty");
        }

        return new SimpleRepository(values);
    }

    private final Map<Key<?>, Object> items;

    private SimpleRepository(Map<Key<?>, Object> items) {
        this.items = items;
    }

    @Nullable
    @Override
    public <T> T get(Key<T> key) {
        T value = key.cast(
                items.get(requireNonNull(key, "key")));

        return value != null ? value : key.defaultValue();
    }

    @Nullable
    @Override
    public <T> T put(Key<T> key, T value) {
        requireNonNull(key, "key");

        return key.cast(
                items.put(key, requireNonNull(value, "value")));
    }

    @Nullable
    @Override
    public <T> T putIfAbsent(Key<T> key, T value) {
        requireNonNull(key, "key");

        return key.cast(
                items.putIfAbsent(key, requireNonNull(value, "value")));
    }

    private <T> BiFunction<? super Key<?>, ? super Object, ?> wrapRemappingFunction(Key<T> key, UnaryOperator<T> remappingFunction) {
        return (key1, object) -> {
            T value = key.cast(object);

            // Fallback to key default value
            if (value == null) {
                value = key.defaultValue();
            }

            return remappingFunction.apply(value);
        };
    }

    @Override
    public <T> T compute(Key<T> key, UnaryOperator<T> remappingFunction) {
        requireNonNull(key, "key");

        return key.cast(
                items.compute(key, wrapRemappingFunction(key, remappingFunction)));
    }

    @Override
    public <T> T computeIfAbsent(Key<T> key, Supplier<? extends T> mappingFunction) {
        requireNonNull(key, "key");

        return key.cast(
                items.computeIfAbsent(key, key1 ->
                        requireNonNull(mappingFunction.get(), "new value")));
    }

    @Nullable
    @Override
    public <T> T computeIfPresent(Key<T> key, UnaryOperator<T> remappingFunction) {
        requireNonNull(key, "key");

        return key.cast(
                items.computeIfPresent(key, wrapRemappingFunction(key, remappingFunction)));
    }

    @Nullable
    @Override
    public <T> T replace(Key<T> key, T value) {
        requireNonNull(key, "key");

        return key.cast(
                items.replace(key, requireNonNull(value, "value")));
    }

    @Override
    public <T> boolean replace(Key<T> key, T oldValue, T newValue) {
        return items.replace(
                requireNonNull(key, "key"),
                requireNonNull(oldValue, "oldValue"),
                requireNonNull(newValue, "newValue"));
    }

    @Nullable
    @Override
    public <T> T remove(Key<T> key) {
        requireNonNull(key, "key");

        return key.cast(items.remove(key));
    }

    @Override
    public <T> boolean remove(Key<T> key, T value) {
        requireNonNull(key, "key");

        return items.remove(
                requireNonNull(key, "key"), requireNonNull(value, "value"));
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public boolean contains(Key<?> key) {
        return items.containsKey(key);
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
