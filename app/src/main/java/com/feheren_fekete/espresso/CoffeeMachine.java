package com.feheren_fekete.espresso;

public class CoffeeMachine {

    private SimulationParameters mSimulationParameters;
    private CoffeeMachineState mState;
    private long mStepsUntilCoffeeReady;

    public CoffeeMachine(SimulationParameters parameters) {
        mSimulationParameters = parameters;
        mState = new CoffeeMachineState(false, false, 0);
        mStepsUntilCoffeeReady = 0;
    }

    public boolean hasNewState() {
        return mState.isStateChanged();
    }

    public CoffeeMachineState getState() {
        return new CoffeeMachineState(mState);
    }

    private void brewTheCoffee() {
        mStepsUntilCoffeeReady = Math.max(0, mStepsUntilCoffeeReady - 1);
        int progress =
                Math.round(
                        (float)(mSimulationParameters.stepsUntilCoffeeReady - mStepsUntilCoffeeReady)
                                * 100 / mSimulationParameters.stepsUntilCoffeeReady);
        progress = ((progress + 10) / 10) * 10;
        mState.setBrewingProgress(progress);
     }

    public void doOneStep(boolean isQueueEmpty) {
        mState.clearStateChanged();
        if (mState.isCoffeeReady()) {
            if (!isQueueEmpty) {
                takeCoffee();
            }
        } else if (!mState.isBrewing()) {
            if (!isQueueEmpty) {
                startBrewing();
            }
        } else {
            brewTheCoffee();
            if (mStepsUntilCoffeeReady <= 0) {
                mStepsUntilCoffeeReady = 0;
                mState.setCoffeeReady(true);
                mState.setBrewing(false);
            }
        }
    }

    public boolean isCoffeeReady() {
        return mState.isCoffeeReady();
    }

    public void setCoffeeReady(boolean isCoffeeReady) {
        mState.setCoffeeReady(isCoffeeReady);
        if (isCoffeeReady) {
            mState.setBrewing(false);
            mStepsUntilCoffeeReady = 0;
        } else {
            mStepsUntilCoffeeReady = mSimulationParameters.stepsUntilCoffeeReady;
        }
    }

    public boolean isIdle() {
        return !mState.isBrewing();
    }

    public void takeCoffee() {
        assert mState.isCoffeeReady();
        mState.setBrewing(false);
        mState.setCoffeeReady(false);
        mStepsUntilCoffeeReady = 0;
    }

    public void startBrewing() {
        assert !mState.isBrewing();
        mState.setBrewing(true);
        mState.setCoffeeReady(false);
        mStepsUntilCoffeeReady = mSimulationParameters.stepsUntilCoffeeReady;
    }
}
