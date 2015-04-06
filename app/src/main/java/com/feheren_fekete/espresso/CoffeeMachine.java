package com.feheren_fekete.espresso;

public class CoffeeMachine {

    private SimulationParameters mSimulationParameters;
    private boolean mIsIdle;
    private boolean mIsCoffeeReady;
    private long mStepsUntilCoffeeReady;

    public CoffeeMachine(SimulationParameters parameters) {
        mSimulationParameters = parameters;
        mIsIdle = true;
        mIsCoffeeReady = false;
        mStepsUntilCoffeeReady = 0;
    }

    public boolean hasNewState() {
        return true;
    }

    public CoffeeMachineState getState() {
        int progress =
                Math.round(
                        (float)(mSimulationParameters.stepsUntilCoffeeReady - mStepsUntilCoffeeReady)
                                * 100 / mSimulationParameters.stepsUntilCoffeeReady);
        return new CoffeeMachineState(!isIdle(), isCoffeeReady(), progress);
    }

    public void doOneStep(boolean isQueueEmpty) {
        if (mIsCoffeeReady) {
            if (!isQueueEmpty) {
                takeCoffee();
            }
        } else if (mIsIdle) {
            if (!isQueueEmpty) {
                startBrewing();
            }
        } else {
            --mStepsUntilCoffeeReady;
            if (mStepsUntilCoffeeReady <= 0) {
                mStepsUntilCoffeeReady = 0;
                mIsCoffeeReady = true;
                mIsIdle = true;
            }
        }
    }

    public boolean isCoffeeReady() {
        return mIsCoffeeReady;
    }

    public boolean isIdle() {
        return mIsIdle;
    }

    public void takeCoffee() {
        assert mIsCoffeeReady;
        mIsIdle = true;
        mIsCoffeeReady = false;
        mStepsUntilCoffeeReady = 0;
    }

    public void startBrewing() {
        assert mIsIdle;
        mIsIdle = false;
        mIsCoffeeReady = false;
        mStepsUntilCoffeeReady = mSimulationParameters.stepsUntilCoffeeReady;
    }
}
