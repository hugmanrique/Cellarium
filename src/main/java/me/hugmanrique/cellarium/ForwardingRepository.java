package me.hugmanrique.cellarium;

import java.util.Objects;
import java.util.Optional;

/**
 * A repository that forwards all its method calls to another repository.
 * Subclasses can override any method to modify the behavior of the
 * backing repository.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Decorator_pattern">Decorator pattern</a>
 */
public class ForwardingRepository implements Repository {
    private final Repository repository;

    public ForwardingRepository(Repository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public boolean hasValue(Item<?> item) {
        return repository.hasValue(item);
    }

    @Override
    public <T> Optional<T> getValue(Item<T> item) {
        return repository.getValue(item);
    }

    @Override
    public <T> Optional<T> setValue(Item<T> item, T value) {
        return repository.setValue(item, value);
    }

    @Override
    public <T> Optional<T> removeValue(Item<T> item) {
        return repository.removeValue(item);
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return repository.equals(obj);
    }

    @Override
    public int hashCode() {
        return repository.hashCode();
    }

    @Override
    public String toString() {
        return repository.toString();
    }
}
