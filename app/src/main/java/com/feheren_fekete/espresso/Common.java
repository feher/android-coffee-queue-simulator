package com.feheren_fekete.espresso;

import java.util.Random;

public class Common {
    public static final String LOG_TAG = "Espresso";
    public static final String SIMULATION_PARAMETERS = "SIMULATION_PARAMETERS";

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
