package com.feheren_fekete.espresso;

public class CoffeeMachineState {
    private boolean mIsBrewing;
    private boolean mIsCoffeeReady;
    private int mBrewingProgress;
    private boolean mIsStateChanged;

    public CoffeeMachineState(boolean isBrewing, boolean isCoffeeReady, int brewingProgress) {
        mIsBrewing = isBrewing;
        mIsCoffeeReady = isCoffeeReady;
        mBrewingProgress = brewingProgress;
        mIsStateChanged = false;
    }

    public CoffeeMachineState(CoffeeMachineState other) {
        mIsBrewing = other.mIsBrewing;
        mIsCoffeeReady = other.mIsCoffeeReady;
        mBrewingProgress = other.mBrewingProgress;
        mIsStateChanged = other.mIsStateChanged;
    }

    public boolean isStateChanged() {
        return mIsStateChanged;
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

    public void clearStateChanged() {
        mIsStateChanged = false;
    }

    public void setBrewing(boolean isBrewing) {
        if (mIsBrewing != isBrewing) {
            mIsBrewing = isBrewing;
            mIsStateChanged = true;
        }
    }

    public void setCoffeeReady(boolean isCoffeeReady) {
        if (mIsCoffeeReady != isCoffeeReady) {
            mIsCoffeeReady = isCoffeeReady;
            mIsStateChanged = true;
        }
    }

    public void setBrewingProgress(int brewingProgress) {
        if (mBrewingProgress != brewingProgress) {
            mBrewingProgress = brewingProgress;
            mIsStateChanged = true;
        }
    }
}
