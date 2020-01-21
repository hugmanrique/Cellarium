package me.hugmanrique.cellarium.tests;

import me.hugmanrique.cellarium.Key;
import me.hugmanrique.cellarium.simple.SimpleKey;
import me.hugmanrique.cellarium.simple.SimpleRepository;
import me.hugmanrique.cellarium.util.IntegerValues;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegerValuesTests {

    private static final int CHECK_COUNT = 1000;

    private static final Key<Integer> CARS = new SimpleKey.Builder<>(Integer.class).build();
    private static final Key<Integer> BOATS = new SimpleKey.Builder<>(Integer.class)
            .defaultValue(5).build();

    private static final Random random = new Random(0xB0B);

    @Test
    void testIncreaseBy() {
        SimpleRepository repository = SimpleRepository.newInstance();

        int value = IntegerValues.increaseBy(repository, CARS, 7);
        assertEquals(7, value);

        value = IntegerValues.increaseBy(repository, BOATS, 8);
        assertEquals(13, value);
    }

    @Test
    void testDecreaseBy() {
        SimpleRepository repository = SimpleRepository.newInstance();

        int value = IntegerValues.decreaseBy(repository, CARS, 7);
        assertEquals(-7, value);

        value = IntegerValues.decreaseBy(repository, BOATS, 8);
        assertEquals(-3, value);
    }

    @Test
    void testIncrement() {
        for (int i = 0; i < CHECK_COUNT; i++) {
            int value = random.nextInt();
            assertEquals(value + 1, IntegerValues.increment(value));
        }
    }

    @Test
    void testDecrement() {
        for (int i = 0; i < CHECK_COUNT; i++) {
            int value = random.nextInt();
            assertEquals(value - 1, IntegerValues.decrement(value));
        }
    }
}
