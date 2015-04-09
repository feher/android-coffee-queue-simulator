package com.feheren_fekete.espresso;

import java.util.Random;

public class Engineer {
    private boolean mShouldReportStateChange;
    private SimulationParameters mSimulationParameters;
    private int mId;
    private boolean mIsWorking;
    private boolean mIsBusy;
    private long mBusySteps;
    private long mBusyCheckSteps;
    private long mStepsUntilNeedCoffee;

    public Engineer(int id, SimulationParameters parameters) {
        mShouldReportStateChange = false;
        mSimulationParameters = parameters;
        mId = id;
        mIsWorking = true;
        mIsBusy = false;
        mBusySteps = 0;
        mBusyCheckSteps = 0;
        mStepsUntilNeedCoffee = Math.round(Common.random.nextFloat() * parameters.stepsUntilNeedCoffee);
        if (Common.eventHappens(parameters.busyProb)) {
            mIsBusy = true;
            mBusySteps = parameters.busySteps;
        }
    }

    public boolean hasNewState() {
        return mShouldReportStateChange;
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

        return new EngineerState(getId(), isBusy(), isWorking(), busyProgress, needCoffeeProgress);
    }

    public int getId() {
        return mId;
    }

    public boolean isBusy() {
        return mIsBusy;
    }

    public boolean isWorking() {
        return mIsWorking;
    }

    public boolean isQueuing() {
        return !isWorking();
    }

    private void setBusy(boolean isBusy) {
        assert mIsBusy != isBusy;
        mIsBusy = isBusy;
        mBusySteps = mSimulationParameters.busySteps;
        mShouldReportStateChange = true;
    }
    
    private void makeLessBusy() {
        --mBusySteps;
        mShouldReportStateChange = true;
    }
    
    private void workAndDrinkTheCoffee() {
        --mStepsUntilNeedCoffee;
        mShouldReportStateChange = true;
    }

    private void goForCoffee() {
        assert isWorking();
        mIsWorking = false;
        mStepsUntilNeedCoffee = 0;
        mShouldReportStateChange = true;
    }

    private void goToWork() {
        assert isQueuing();
        mIsWorking = true;
        mStepsUntilNeedCoffee = mSimulationParameters.stepsUntilNeedCoffee;
        mShouldReportStateChange = true;
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
        mShouldReportStateChange = false;

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
