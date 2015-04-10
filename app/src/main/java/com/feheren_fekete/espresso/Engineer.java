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
        mState = new EngineerState().setId(id).setWorking(true).setBusy(false);
        mBusySteps = 0;
        mBusyCheckSteps = 0;
        mStepsUntilNeedCoffee = Math.round(Common.random.nextFloat() * parameters.stepsUntilNeedCoffee);
        if (Common.eventHappens(parameters.busyProb)) {
            mState.setBusy(true);
            mBusySteps = parameters.busySteps;
        }
    }

    public boolean hasNewState() {
        return mState.getChangedState() != EngineerState.CHANGED_NOTHING;
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

        EngineerState reportedState = new EngineerState(mState);
        return reportedState;
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
        mState.setChangedState(EngineerState.CHANGED_IS_BUSY);
    }
    
    private void makeLessBusy() {
        --mBusySteps;
        mState.setChangedState(EngineerState.CHANGED_BUSY_PROGRESS);
    }
    
    private void workAndDrinkTheCoffee() {
        --mStepsUntilNeedCoffee;
        mState.setChangedState(EngineerState.CHANGED_NEED_COFFEE_PROGRESS);
    }

    private void goForCoffee() {
        assert isWorking();
        mState.setWorking(false);
        mStepsUntilNeedCoffee = 0;
        mState.setChangedState(EngineerState.CHANGED_IS_WORKING);
    }

    private void goToWork() {
        assert isQueuing();
        mState.setWorking(true);
        mStepsUntilNeedCoffee = mSimulationParameters.stepsUntilNeedCoffee;
        mState.setChangedState(EngineerState.CHANGED_IS_WORKING);
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
        mState.clearChangedStates();

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
