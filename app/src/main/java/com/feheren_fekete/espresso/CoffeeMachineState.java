package com.feheren_fekete.espresso;

public class CoffeeMachineState {
    public final boolean isBrewing;
    public final boolean isCoffeeReady;
    public final int brewingProgress;

    public CoffeeMachineState(boolean isBrewing, boolean isCoffeeReady, int brewingProgress) {
        this.isBrewing = isBrewing;
        this.isCoffeeReady = isCoffeeReady;
        this.brewingProgress = brewingProgress;
    }
}
