package me.hugmanrique.cellarium.v2.simple;

import me.hugmanrique.cellarium.v2.Key;
import me.hugmanrique.cellarium.v2.Repository;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

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
    public static SimpleRepository newThreadSafeInstance() {
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
        return key.cast(
                items.get(requireNonNull(key, "key")));
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
    public <T> T remove(Key<T> key, T value) {
        requireNonNull(key, "key");

        return key.cast(
                items.remove(key, requireNonNull(value, "value")));
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
