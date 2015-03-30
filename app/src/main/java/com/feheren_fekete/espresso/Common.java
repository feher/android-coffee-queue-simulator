package com.feheren_fekete.espresso;

import java.util.Random;

public class Common {
    public static final String SIMULATION_REAL_SECONDS_PER_STEP = "SIMULATION_REAL_SECONDS_PER_STEP";
    public static final String SIMULATION_ENGINEER_SECONDS_PER_STEP = "SIMULATION_ENGINEER_SECONDS_PER_STEP";
    public static final String SIMULATION_ENGINEER_COUNT = "SIMULATION_ENGINEER_COUNT";
    public static final String SIMULATION_BUSY_PROBABILITY = "SIMULATION_BUSY_PROBABILITY";
    public static final String SIMULATION_BUSY_SECONDS = "SIMULATION_BUSY_SECONDS";
    public static final String SIMULATION_SECONDS_UNTIL_NEED_COFFEE = "SIMULATION_SECONDS_UNTIL_NEED_COFFEE";
    public static final String SIMULATION_SECONDS_UNTIL_COFFEE_READY = "SIMULATION_SECONDS_UNTIL_COFFEE_READY";
    public static final String SIMULATION_MAX_QUEUE_LENGTH_WHEN_BUSY = "SIMULATION_MAX_QUEUE_LENGTH_WHEN_BUSY";
    public static final String SIMULATION_BUSY_CHECK_SECONDS = "SIMULATION_BUSY_CHECK_SECONDS";

    public static final String STATE_CHANGE_COFFEE_MACHINE = "CoffeeMachine:";
    public static final String STATE_CHANGE_ENGINEER = "Engineer:";

    public static final String LOG_TAG = "Espresso";

    public static final Random random = new Random();

    /**
     * Checks whether the event with the given probability would happen.
     * @param probability The probability of the event in percentage. Between 1..100.
     * @return true if the even happens, false otherwise.
     */
    public static boolean eventHappens(int probability) {
        int r = random.nextInt(100);
        return r < probability;
    }

}
