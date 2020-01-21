package me.hugmanrique.cellarium.tests;

import me.hugmanrique.cellarium.util.BooleanValues;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BooleanValuesTests {

    @SuppressWarnings("ConstantConditions")
    @Test
    void testOpposite() {
        assertFalse(BooleanValues.opposite(true));
        assertTrue(BooleanValues.opposite(false));
    }
}
