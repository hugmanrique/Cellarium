package me.hugmanrique.cellarium.v2;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * A repository that forwards all its method calls to another repository.
 * Subclasses can override any method to modify the default behavior of the
 * backing repository.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Decorator_pattern">Decorator pattern</a>
 */
public class ForwardingRepository implements Repository {

    private final Repository repository;

    public ForwardingRepository(Repository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    @Nullable
    @Override
    public <T> T get(Key<T> key) {
        return repository.get(key);
    }

    @Nullable
    @Override
    public <T> T put(Key<T> key, T value) {
        return repository.put(key, value);
    }

    @Nullable
    @Override
    public <T> T putIfAbsent(Key<T> key, T value) {
        return repository.putIfAbsent(key, value);
    }

    @Nullable
    @Override
    public <T> T replace(Key<T> key, T value) {
        return repository.replace(key, value);
    }

    @Override
    public <T> boolean replace(Key<T> key, T oldValue, T newValue) {
        return repository.replace(key, oldValue, newValue);
    }

    @Nullable
    @Override
    public <T> T remove(Key<T> key) {
        return repository.remove(key);
    }

    @Override
    public <T> T remove(Key<T> key, T value) {
        return repository.remove(key, value);
    }

    @Override
    public void clear() {
        repository.clear();
    }

    @Override
    public boolean contains(Key<?> key) {
        return repository.contains(key);
    }

    @Override
    public int size() {
        return repository.size();
    }

    @Override
    public boolean isEmpty() {
        return repository.isEmpty();
    }
}
