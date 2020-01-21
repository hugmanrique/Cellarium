package me.hugmanrique.cellarium.v2;

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

    @Override
    public boolean contains(Key<?> key) {
        return repository.contains(key);
    }
}
