package me.hugmanrique.cellarium.tests;

import me.hugmanrique.cellarium.util.EnumValues;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumValuesTests {

    enum Vehicle {
        CAR, BUS, METRO
    }

    enum Color {
        RED, YELLOW, GREEN, BLUE
    }

    @Test
    void testNextValue_offset() {
        assertEquals(Vehicle.CAR, EnumValues.nextValue(Vehicle.CAR, 0));
        assertEquals(Vehicle.METRO, EnumValues.nextValue(Vehicle.CAR, 2));
        assertEquals(Vehicle.BUS, EnumValues.nextValue(Vehicle.BUS, 3));

        assertEquals(Color.YELLOW, EnumValues.nextValue(Color.YELLOW, 0));
        assertEquals(Color.RED, EnumValues.nextValue(Color.GREEN, -2));
        assertEquals(Color.GREEN, EnumValues.nextValue(Color.YELLOW, -3));
        assertEquals(Color.BLUE, EnumValues.nextValue(Color.BLUE, -4));
    }

    @Test
    void testNextValue() {
        assertEquals(Vehicle.BUS, EnumValues.nextValue(Vehicle.CAR));
        assertEquals(Vehicle.METRO, EnumValues.nextValue(Vehicle.BUS));
        assertEquals(Vehicle.CAR, EnumValues.nextValue(Vehicle.METRO));

        assertEquals(Color.YELLOW, EnumValues.nextValue(Color.RED));
        assertEquals(Color.GREEN, EnumValues.nextValue(Color.YELLOW));
        assertEquals(Color.BLUE, EnumValues.nextValue(Color.GREEN));
        assertEquals(Color.RED, EnumValues.nextValue(Color.BLUE));
    }

    @Test
    void testPreviousValue() {
        assertEquals(Vehicle.CAR, EnumValues.previousValue(Vehicle.BUS));
        assertEquals(Vehicle.BUS, EnumValues.previousValue(Vehicle.METRO));
        assertEquals(Vehicle.METRO, EnumValues.previousValue(Vehicle.CAR));

        assertEquals(Color.RED, EnumValues.previousValue(Color.YELLOW));
        assertEquals(Color.YELLOW, EnumValues.previousValue(Color.GREEN));
        assertEquals(Color.GREEN, EnumValues.previousValue(Color.BLUE));
        assertEquals(Color.BLUE, EnumValues.previousValue(Color.RED));
    }
}
