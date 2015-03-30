package com.feheren_fekete.espresso;

public class SimulationParameters {
    public final float realSecondsPerStep;
    public final int engineerSecondsPerStep;
    public final int engineerCount;
    public final int busyProb;
    public final int busySteps;
    public final int stepsUntilNeedCoffee;
    public final int stepsUntilCoffeeReady;
    public final int maxQueueLengthWhenBusy;
    public final int busyCheckSteps;

    public SimulationParameters(
                 float realSecondsPerStep,
                 int engineerSecondsPerStep,
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
        this.busySteps = Math.round((float)busySeconds / engineerSecondsPerStep);
        this.stepsUntilNeedCoffee = Math.round((float)secondsUntilNeedCoffee / engineerSecondsPerStep);
        this.stepsUntilCoffeeReady = Math.round((float)secondsUntilCoffeeReady / engineerSecondsPerStep);
        this.maxQueueLengthWhenBusy = maxQueueLengthWhenBusy;
        this.busyCheckSteps = Math.round((float)busyCheckSeconds / engineerSecondsPerStep);
    }
}
