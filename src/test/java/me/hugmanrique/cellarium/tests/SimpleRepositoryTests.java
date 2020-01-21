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

        String previous = repository.put(FOO, "bar");
        assertTrue(repository.contains(FOO));
        assertEquals("bar", repository.get(FOO));
        assertNull(previous);

        Integer previous2 = repository.put(BAR, 15);
        assertTrue(repository.contains(BAR));
        assertEquals(15, repository.get(BAR));
        assertNull(previous2);
    }

    @Test
    void testRemove() {
        SimpleRepository repository = SimpleRepository.newInstance();
        repository.put(FOO, "bar");

        // Returns null after remove
        String previous = repository.remove(FOO);
        assertFalse(repository.contains(FOO));
        assertNull(repository.get(FOO));
        assertEquals("bar", previous);

        // Returns default after remove
        Integer previous2 = repository.remove(BAR);
        assertFalse(repository.contains(BAR));
        assertEquals(BAR_DEFAULT, repository.get(BAR));
        assertNull(previous2);
    }

    @Test
    void testPutIfAbsent() {
        SimpleRepository repository = SimpleRepository.newInstance();

        String previous = repository.putIfAbsent(FOO, "bar");
        assertEquals("bar", repository.get(FOO));
        assertNull(previous);

        // Puts even if has default
        Integer previous2 = repository.putIfAbsent(BAR, 12);
        assertEquals(12, repository.get(BAR));
        assertNull(previous2);

        // Doesn't replace existing value
        previous = repository.putIfAbsent(FOO, "nope");
        assertEquals("bar", repository.get(FOO));
        assertEquals("bar", previous);
    }

    @Test
    void testCompute() {
        SimpleRepository repository = SimpleRepository.newInstance();

        // No current mapping

        String newValue = repository.compute(FOO, previous -> {
            assertNull(previous); // No default value
            return "bar";
        });

        assertEquals("bar", repository.get(FOO));
        assertEquals("bar", newValue);

        Integer newValue2 = repository.compute(BAR, previous -> {
            assertEquals(BAR_DEFAULT, previous);
            return 8;
        });

        assertEquals(8, repository.get(BAR));
        assertEquals(8, newValue2);

        // Current mapping

        newValue = repository.compute(FOO, previous -> {
            assertEquals("bar", previous);
            return "bar2";
        });

        assertEquals("bar2", repository.get(FOO));
        assertEquals("bar2", newValue);

        newValue2 = repository.compute(BAR, previous -> {
            assertEquals(8, previous);
            return 7;
        });

        assertEquals(7, repository.get(BAR));
        assertEquals(7, newValue2);

        // Removal

        newValue = repository.compute(FOO, s -> null);
        assertFalse(repository.contains(FOO));
        assertNull(repository.get(FOO));
        assertNull(newValue);

        newValue2 = repository.compute(BAR, integer -> null);
        assertFalse(repository.contains(BAR));
        assertEquals(BAR_DEFAULT, repository.get(BAR));
        assertNull(newValue2);
    }

    @Test
    void testComputeRethrows() {
        SimpleRepository repository = SimpleRepository.newInstance();

        assertThrows(IllegalArgumentException.class, () -> {
            repository.compute(FOO, previous -> {
                throw new IllegalArgumentException("dummy");
            });
        });

        assertThrows(IllegalStateException.class, () -> {
            repository.compute(FOO, previous -> {
                throw new IllegalStateException("dummy");
            });
        });
    }

    @Test
    void testComputeIfAbsent() {
        SimpleRepository repository = SimpleRepository.newInstance();

        // No current mapping

        String value = repository.computeIfAbsent(FOO, () -> "bar");
        assertEquals("bar", repository.get(FOO));
        assertEquals("bar", value);

        int value2 = repository.computeIfAbsent(BAR, () -> 11);
        assertEquals(11, repository.get(BAR));
        assertEquals(11, value2);

        // Current mapping

        value = repository.computeIfAbsent(FOO, () -> fail("Called mappingFunction while not absent"));
        assertEquals("bar", value);

        value2 = repository.computeIfAbsent(BAR, () -> fail("Called mappingFunction while not absent"));
        assertEquals(11, value2);
    }

    @Test
    void testComputeIfAbsentThrowsNPE() {
        SimpleRepository repository = SimpleRepository.newInstance();

        assertThrows(NullPointerException.class, () -> {
            repository.computeIfAbsent(FOO, () -> null);
        });

        assertThrows(NullPointerException.class, () -> {
            repository.computeIfAbsent(BAR, () -> null);
        });
    }

    @Test
    void testComputeIfAbsentRethrows() {
        SimpleRepository repository = SimpleRepository.newInstance();

        assertThrows(IllegalArgumentException.class, () -> {
            repository.computeIfAbsent(FOO, () -> {
                throw new IllegalArgumentException("dummy");
            });
        });

        assertThrows(IllegalStateException.class, () -> {
            repository.computeIfAbsent(FOO, () -> {
                throw new IllegalStateException("dummy");
            });
        });
    }

    @Test
    void testComputeIfPresent() {
        SimpleRepository repository = SimpleRepository.newInstance();

        // No current mapping

        String value = repository.computeIfPresent(FOO, s -> fail("Called remappingFunction while not present"));
        assertNull(value);

        Integer value2 = repository.computeIfPresent(BAR, integer -> fail("Called remappingFunction while not present"));
        assertNull(value2);

        // Current mapping

        repository.put(FOO, "bar");
        value = repository.computeIfPresent(FOO, previous -> {
            assertEquals("bar", previous);
            return "boo";
        });

        assertEquals("boo", repository.get(FOO));
        assertEquals("boo", value);

        repository.put(BAR, 15);
        value2 = repository.computeIfPresent(BAR, previous -> {
            assertEquals(15, previous);
            return 14;
        });

        assertEquals(14, repository.get(BAR));
        assertEquals(14, value2);

        // Removal

        value = repository.computeIfPresent(FOO, previous -> {
            assertEquals("boo", previous);
            return null;
        });

        assertFalse(repository.contains(FOO));
        assertNull(repository.get(FOO));
        assertNull(value);

        value2 = repository.computeIfPresent(BAR, previous -> {
            assertEquals(14, previous);
            return null;
        });

        assertFalse(repository.contains(BAR));
        assertEquals(BAR_DEFAULT, repository.get(BAR));
        assertNull(value2);
    }

    @Test
    void testComputeIfPresentRethrows() {
        SimpleRepository repository = SimpleRepository.newInstance();

        repository.put(FOO, "bar");
        repository.put(BAR, 12);

        assertThrows(IllegalArgumentException.class, () -> {
            repository.computeIfPresent(FOO, previous -> {
                throw new IllegalArgumentException("dummy");
            });
        });

        assertThrows(IllegalStateException.class, () -> {
            repository.computeIfPresent(FOO, previous -> {
                throw new IllegalStateException("dummy");
            });
        });
    }

    @Test
    void testReplace() {
        SimpleRepository repository = SimpleRepository.newInstance();

        String previous = repository.replace(FOO, "bar");
        assertFalse(repository.contains(FOO));
        assertNull(previous);

        Integer previous2 = repository.replace(BAR, 2);
        assertFalse(repository.contains(BAR));
        assertNull(previous2);

        // Setup
        repository.put(FOO, "bar");
        repository.put(BAR, 30);

        // Key tests

        previous = repository.replace(FOO, "yes");
        assertEquals("yes", repository.get(FOO));
        assertEquals("bar", previous);

        previous2 = repository.replace(BAR, 3);
        assertEquals(3, repository.get(BAR));
        assertEquals(30, previous2);

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
