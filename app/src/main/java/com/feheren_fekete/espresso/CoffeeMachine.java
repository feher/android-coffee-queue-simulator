package com.feheren_fekete.espresso;

public class CoffeeMachine {

    private SimulationParameters mSimulationParameters;
    private ProgressReporter mProgressReporter;
    private boolean mIsIdle;
    private boolean mIsCoffeeReady;
    private long mStepsRemainingUntilReady;

    public CoffeeMachine(SimulationParameters parameters, ProgressReporter reporter) {
        mSimulationParameters = parameters;
        mProgressReporter = reporter;
        mIsIdle = true;
        mIsCoffeeReady = false;
        mStepsRemainingUntilReady = 0;
    }

    public CoffeeMachine(CoffeeMachine other) {
        mIsIdle = other.mIsIdle;
        mIsCoffeeReady = other.mIsCoffeeReady;
        mStepsRemainingUntilReady = other.mStepsRemainingUntilReady;
        mProgressReporter = other.mProgressReporter;
        mSimulationParameters = other.mSimulationParameters;
    }

    private void reportStateChange() {
        int progress =
                Math.round(
                        (float)(mSimulationParameters.stepsUntilCoffeeReady - mStepsRemainingUntilReady)
                                * 100 / mSimulationParameters.stepsUntilCoffeeReady);
        CoffeeMachineState state = new CoffeeMachineState(!isIdle(), isCoffeeReady(), progress);
        mProgressReporter.reportStateChange(state);
    }

    public void doOneStep() {
        if (mIsIdle) {
            return;
        }
        if (mStepsRemainingUntilReady == 0) {
            mIsCoffeeReady = true;
            mIsIdle = true;
            reportStateChange();
        } else {
            mStepsRemainingUntilReady -= 1;
            reportStateChange();
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
        mStepsRemainingUntilReady = 0;
        reportStateChange();
    }

    public void startBrewing() {
        assert mIsIdle;
        mIsIdle = false;
        mIsCoffeeReady = false;
        mStepsRemainingUntilReady = mSimulationParameters.stepsUntilCoffeeReady;
        reportStateChange();
    }
}
