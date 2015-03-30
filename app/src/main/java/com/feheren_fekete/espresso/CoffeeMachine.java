package com.feheren_fekete.espresso;

public class CoffeeMachine implements Cloneable {

    private SimulationParameters mSimulationParameters;
    private ProgressReporter mProgressReporter;
    private boolean mIsIdle;
    private boolean mIsCoffeeReady;
    private int mStepsRemainingUntilReady;

    public CoffeeMachine(SimulationParameters parameters, ProgressReporter reporter) {
        mSimulationParameters = parameters;
        mProgressReporter = reporter;
        mIsIdle = true;
        mIsCoffeeReady = false;
        mStepsRemainingUntilReady = 0;
    }

    private CoffeeMachine() {
        mSimulationParameters = null;
        mProgressReporter = null;
        mIsIdle = true;
        mIsCoffeeReady = false;
        mStepsRemainingUntilReady = 0;
    }

    @Override
    public Object clone() {
        CoffeeMachine c = new CoffeeMachine();
        c.mIsIdle = mIsIdle;
        c.mIsCoffeeReady = mIsCoffeeReady;
        c.mStepsRemainingUntilReady = mStepsRemainingUntilReady;
        c.mProgressReporter = mProgressReporter;
        c.mSimulationParameters = mSimulationParameters;
        return c;
    }

    private void reportStateChange() {
        String state;
        if (isIdle()) {
            state = "I";
        } else if (isCoffeeReady()) {
            state = "R";
        } else {
            state = "B";
        }
        int progress =
                Math.round(
                        (float)(mSimulationParameters.stepsUntilCoffeeReady - mStepsRemainingUntilReady)
                        * 100 / mSimulationParameters.stepsUntilCoffeeReady);
        mProgressReporter.reportStateChange(Common.STATE_CHANGE_COFFEE_MACHINE + state + progress);
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

    public String getStateText() {
        String state;
        if (mIsIdle) {
            state = "idle ";
            if (mIsCoffeeReady) {
                state += ", coffee ready";
            }
        } else {
            state = "brewing, ready in " + mStepsRemainingUntilReady + " steps";
        }
        return state;
    }
}
