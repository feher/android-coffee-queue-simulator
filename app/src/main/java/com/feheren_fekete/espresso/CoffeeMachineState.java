package com.feheren_fekete.espresso;

public class CoffeeMachineState {
    private final boolean mIsBrewing;
    private final boolean mIsCoffeeReady;
    private final int mBrewingProgress;

    public CoffeeMachineState(boolean isBrewing, boolean isCoffeeReady, int brewingProgress) {
        mIsBrewing = isBrewing;
        mIsCoffeeReady = isCoffeeReady;
        mBrewingProgress = brewingProgress;
    }

    public boolean isBrewing() {
        return mIsBrewing;
    }

    public boolean isCoffeeReady() {
        return mIsCoffeeReady;
    }

    public int getBrewingProgress() {
        return mBrewingProgress;
    }
}
