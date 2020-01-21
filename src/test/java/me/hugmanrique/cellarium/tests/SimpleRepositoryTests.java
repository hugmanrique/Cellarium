package me.hugmanrique.cellarium.tests;

import me.hugmanrique.cellarium.v2.Key;
import me.hugmanrique.cellarium.v2.simple.SimpleKey;
import me.hugmanrique.cellarium.v2.simple.SimpleRepository;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleRepositoryTests {

    // Creation

    @Test
    void testNewInstance() {
        SimpleRepository repository = SimpleRepository.newInstance();

        assertNotNull(repository);
        assertTrue(repository.isEmpty());
    }

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

    private static final Key<String> FOO = new SimpleKey.Builder<>(String.class).build();
    private static final Key<Integer> BAR = new SimpleKey.Builder<>(Integer.class)
            .defaultValue(20)
            .build();

    // Retrieval

    @Test
    void testNonMappedGets() {
        SimpleRepository repository = SimpleRepository.newInstance();

        assertFalse(repository.contains(FOO));
        assertNull(repository.get(FOO));
        assertFalse(repository.contains(FOO));
        assertEquals(20, repository.get(BAR));
    }

    @Test
    void testGetAndPut() {
        SimpleRepository repository = SimpleRepository.newInstance();

        repository.put(FOO, "bar");
        assertTrue(repository.contains(FOO));
        assertEquals("bar", repository.get(FOO));

        repository.put(BAR, 15);
        assertTrue(repository.contains(BAR));
        assertEquals(15, repository.get(BAR));
    }

    @Test
    void testRemove() {
        SimpleRepository repository = SimpleRepository.newInstance();
        repository.put(FOO, "bar");

        // Returns null after remove
        repository.remove(FOO);
        assertFalse(repository.contains(FOO));
        assertNull(repository.get(FOO));

        // Returns default after remove
        repository.remove(BAR);
        assertFalse(repository.contains(BAR));
        assertEquals(20, repository.get(BAR));
    }

    @Test
    void testPutIfAbsent() {
        SimpleRepository repository = SimpleRepository.newInstance();

        repository.putIfAbsent(FOO, "bar");
        assertTrue(repository.contains(FOO));
        assertEquals("bar", repository.get(FOO));

        // Puts even if has default
        repository.putIfAbsent(BAR, 12);
        assertTrue(repository.contains(BAR));
        assertEquals(12, repository.get(BAR));

        // Doesn't replace existing value
        repository.putIfAbsent(FOO, "nonbar");
        assertTrue(repository.contains(FOO));
        assertEquals("bar", repository.get(FOO));
    }

    @Test
    void testReplace() {

    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void testNullParams() {
        SimpleRepository repository = SimpleRepository.newInstance();

        assertThrows(NullPointerException.class, () -> repository.get(null));
        assertThrows(NullPointerException.class, () -> repository.put(null, "dummy"));
        assertThrows(NullPointerException.class, () -> repository.put(FOO, null));
        assertThrows(NullPointerException.class, () -> repository.putIfAbsent(null, "dummy"));
        assertThrows(NullPointerException.class, () -> repository.putIfAbsent(BAR, null));
        assertThrows(NullPointerException.class, () -> repository.replace(null, "dummy"));
        assertThrows(NullPointerException.class, () -> repository.replace(FOO, null));
        assertThrows(NullPointerException.class, () -> repository.replace(null, "dummy", "dummy"));
        assertThrows(NullPointerException.class, () -> repository.replace(BAR, null, 14));
        assertThrows(NullPointerException.class, () -> repository.replace(BAR, 17, null));
        assertThrows(NullPointerException.class, () -> repository.remove(null));
        assertThrows(NullPointerException.class, () -> repository.remove(FOO, null));
    }
}
