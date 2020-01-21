package me.hugmanrique.cellarium.tests;

import me.hugmanrique.cellarium.Key;
import me.hugmanrique.cellarium.simple.SimpleKey;
import me.hugmanrique.cellarium.simple.SimpleRepository;
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

    // Item management

    private static final int BAR_DEFAULT = 20;

    private static final Key<String> FOO = new SimpleKey.Builder<>(String.class).build();
    private static final Key<Integer> BAR = new SimpleKey.Builder<>(Integer.class)
            .defaultValue(BAR_DEFAULT)
            .build();

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
        assertEquals(BAR_DEFAULT, repository.get(BAR));
    }

    @Test
    void testPutIfAbsent() {
        SimpleRepository repository = SimpleRepository.newInstance();

        repository.putIfAbsent(FOO, "bar");
        assertEquals("bar", repository.get(FOO));

        // Puts even if has default
        repository.putIfAbsent(BAR, 12);
        assertEquals(12, repository.get(BAR));

        // Doesn't replace existing value
        repository.putIfAbsent(FOO, "nope");
        assertEquals("bar", repository.get(FOO));
    }

    @Test
    void testCompute() {
        SimpleRepository repository = SimpleRepository.newInstance();

        // No current mapping

        repository.compute(FOO, previous -> {
            assertNull(previous); // No default value
            return "bar";
        });

        assertEquals("bar", repository.get(FOO));

        repository.compute(BAR, previous -> {
            assertEquals(BAR_DEFAULT, previous);
            return 8;
        });

        assertEquals(8, repository.get(BAR));

        // Current mapping

        repository.compute(FOO, previous -> {
            assertEquals("bar", previous);
            return "bar2";
        });

        assertEquals("bar2", repository.get(FOO));

        repository.compute(BAR, previous -> {
            assertEquals(8, previous);
            return 7;
        });

        assertEquals(7, repository.get(BAR));

        // Removal

        repository.compute(FOO, s -> null);
        assertFalse(repository.contains(FOO));
        assertNull(repository.get(FOO));

        repository.compute(BAR, integer -> null);
        assertFalse(repository.contains(BAR));
        assertEquals(BAR_DEFAULT, repository.get(BAR));
    }

    @Test
    void testReplace() {
        SimpleRepository repository = SimpleRepository.newInstance();

        repository.replace(FOO, "bar");
        assertFalse(repository.contains(FOO));

        repository.replace(BAR, 2);
        assertFalse(repository.contains(BAR));

        // Setup
        repository.put(FOO, "bar");
        repository.put(BAR, 30);

        // Key tests

        repository.replace(FOO, "yes");
        assertEquals("yes", repository.get(FOO));

        repository.replace(BAR, 3);
        assertEquals(3, repository.get(BAR));

        // Key, value tests

        repository.replace(FOO, "no", "no");
        assertEquals("yes", repository.get(FOO));

        repository.replace(BAR, 3, 12);
        assertEquals(12, repository.get(BAR));
    }

    @Test
    void testClear() {
        SimpleRepository repository = SimpleRepository.newInstance();

        repository.put(FOO, "bar");
        repository.put(BAR, 10);

        repository.clear();

        assertTrue(repository.isEmpty());
        assertFalse(repository.contains(FOO));
        assertEquals(BAR_DEFAULT, repository.get(BAR));
    }

    @Test
    void testSizeAndIsEmpty() {
        SimpleRepository repository = SimpleRepository.newInstance();

        assertTrue(repository.isEmpty());
        assertEquals(0, repository.size());

        repository.put(FOO, "bar");
        assertFalse(repository.isEmpty());
        assertEquals(1, repository.size());

        repository.put(FOO, "other");
        assertEquals(1, repository.size());

        repository.put(BAR, 12);
        assertEquals(2, repository.size());
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
