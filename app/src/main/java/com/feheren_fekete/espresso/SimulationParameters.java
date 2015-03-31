package com.feheren_fekete.espresso;

public class SimulationParameters {
    public final double realSecondsPerStep;
    public final double engineerSecondsPerStep;
    public final int engineerCount;
    public final int busyProb;
    public final long busyCheckSteps;
    public final long busySteps;
    public final long stepsUntilNeedCoffee;
    public final long stepsUntilCoffeeReady;
    public final int maxQueueLengthWhenBusy;

    public SimulationParameters(
                 double realSecondsPerStep,
                 double engineerSecondsPerStep,
                 int engineerCount,
                 int busyProb,
                 int busyCheckSeconds,
                 int busySeconds,
                 int secondsUntilNeedCoffee,
                 int secondsUntilCoffeeReady,
                 int maxQueueLengthWhenBusy) {
        this.realSecondsPerStep = realSecondsPerStep;
        this.engineerSecondsPerStep = engineerSecondsPerStep;
        this.engineerCount = engineerCount;
        this.busyProb = busyProb;
        this.busyCheckSteps = Math.round(busyCheckSeconds / engineerSecondsPerStep);
        this.busySteps = Math.round(busySeconds / engineerSecondsPerStep);
        this.stepsUntilNeedCoffee = Math.round(secondsUntilNeedCoffee / engineerSecondsPerStep);
        this.stepsUntilCoffeeReady = Math.round(secondsUntilCoffeeReady / engineerSecondsPerStep);
        this.maxQueueLengthWhenBusy = maxQueueLengthWhenBusy;
    }
}
