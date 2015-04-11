package com.feheren_fekete.espresso;

import java.util.Random;

public class Engineer {
    private SimulationParameters mSimulationParameters;
    private EngineerState mState;
    private long mBusySteps;
    private long mBusyCheckSteps;
    private long mStepsUntilNeedCoffee;

    public Engineer(int id, SimulationParameters parameters) {
        mSimulationParameters = parameters;
        mState = new EngineerState.Builder().setId(id).setWorking(true).setBusy(false).build();
        mBusySteps = 0;
        mBusyCheckSteps = 0;
        mStepsUntilNeedCoffee = Math.round(Common.random.nextFloat() * parameters.stepsUntilNeedCoffee);
        if (Common.eventHappens(parameters.busyProb)) {
            mState.setBusy(true);
            mBusySteps = parameters.busySteps;
        }
    }

    public boolean hasNewState() {
        return mState.isStateChanged();
    }

    public EngineerState getState() {
        int busyProgress =
                Math.round(
                        (float)(mSimulationParameters.busySteps - mBusySteps)
                                * 100 / mSimulationParameters.busySteps);
        int needCoffeeProgress =
                Math.round(
                        (float)(mSimulationParameters.stepsUntilNeedCoffee - mStepsUntilNeedCoffee)
                                * 100 / mSimulationParameters.stepsUntilNeedCoffee);

        mState.setBusyProgress(busyProgress);
        mState.setNeedCoffeeProgress(needCoffeeProgress);

        return new EngineerState(mState);
    }

    public int getId() {
        return mState.getId();
    }

    public boolean isBusy() {
        return mState.isBusy();
    }

    public boolean isWorking() {
        return mState.isWorking();
    }

    public boolean isQueuing() {
        return !isWorking();
    }

    private void setBusy(boolean isBusy) {
        assert mState.isBusy() != isBusy;
        mState.setBusy(isBusy);
        mBusySteps = mSimulationParameters.busySteps;
    }
    
    private void makeLessBusy() {
        --mBusySteps;
    }
    
    private void workAndDrinkTheCoffee() {
        --mStepsUntilNeedCoffee;
    }

    private void goForCoffee() {
        assert isWorking();
        mState.setWorking(false);
        mStepsUntilNeedCoffee = 0;
    }

    private void goToWork() {
        assert isQueuing();
        mState.setWorking(true);
        mStepsUntilNeedCoffee = mSimulationParameters.stepsUntilNeedCoffee;
    }

    private void doOneWorkingStep() {
        if (isBusy()) {
            makeLessBusy();
            if (mBusySteps <= 0) {
                setBusy(false);
            }
        }

        workAndDrinkTheCoffee();
        if (mStepsUntilNeedCoffee <= 0) {
            goForCoffee();
        }
    }

    private void doOneQueuingStep(boolean isCoffeeReady,
                                  int nextIdInQueue) {
        assert nextIdInQueue != CoffeeQueue.INVALID_ID;
        if (isCoffeeReady && nextIdInQueue == getId()) {
            goToWork();
        }
    }
    
    private void maybeBecomeBusy() {
        ++mBusyCheckSteps;
        if (mBusyCheckSteps >= mSimulationParameters.busyCheckSteps) {
            mBusyCheckSteps = 0;
            if (Common.eventHappens(mSimulationParameters.busyProb)) {
                setBusy(true);
            }
        }
    }
    
    public void doOneStep(boolean isCoffeeReady,
                          int nextIdInQueue) {
        mState.clearStateChanged();

        if (!isBusy()) {
            maybeBecomeBusy();
        }
        
        if (isWorking()) {
            doOneWorkingStep();
        } else {
            doOneQueuingStep(isCoffeeReady, nextIdInQueue);
        }
    }
}
