package me.hugmanrique.cellarium.tests.repository;

import me.hugmanrique.cellarium.Key;
import me.hugmanrique.cellarium.Repository;
import me.hugmanrique.cellarium.simple.SimpleKey;
import me.hugmanrique.cellarium.simple.SimpleRepository;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleRepositoryTests extends RepositoryTests {

    @Override
    protected Repository newRepository() {
        return SimpleRepository.newInstance();
    }

    // Creation

    @Test
    void testNewConcurrentInstance() {
        SimpleRepository repository = SimpleRepository.newConcurrentInstance();

        assertNotNull(repository);
        assertTrue(repository.isEmpty());
    }

    @Test
    void testValidNewInstance() {
        SimpleRepository repository = SimpleRepository.newInstance(HashMap::new);

        assertNotNull(repository);
        assertTrue(repository.isEmpty());
    }

    @Test
    void testNullSuppliedMapThrows() {
        assertThrows(NullPointerException.class, () -> {
            SimpleRepository.newInstance(() -> null);
        });
    }

    @Test
    void testNonEmptySuppliedMapThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            SimpleRepository.newInstance(() -> {
                Map<Key<?>, Object> items = new HashMap<>();

                Key<String> foo = new SimpleKey.Builder<>(String.class).build();
                items.put(foo, "bar");

                return items;
            });
        });
    }
}
