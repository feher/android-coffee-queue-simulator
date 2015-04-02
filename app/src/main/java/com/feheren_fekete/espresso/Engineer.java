package com.feheren_fekete.espresso;

import java.util.Random;

public class Engineer {
    private boolean mShouldReportStateChange;
    private SimulationParameters mSimulationParameters;
    private ProgressReporter mProgressReporter;
    private int mId;
    private boolean mIsWorking;
    private boolean mIsBusy;
    private long mBusySteps;
    private long mBusyCheckSteps;
    private long mStepsUntilNeedCoffee;

    public Engineer(int id, SimulationParameters parameters, ProgressReporter reporter) {
        mShouldReportStateChange = false;
        mSimulationParameters = parameters;
        mProgressReporter = reporter;
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

    public Engineer(Engineer other) {
        mShouldReportStateChange = other.mShouldReportStateChange;
        mSimulationParameters = other.mSimulationParameters;
        mProgressReporter = other.mProgressReporter;
        mId = other.mId;
        mIsWorking = other.mIsWorking;
        mIsBusy = other.mIsBusy;
        mBusySteps = other.mBusySteps;
        mBusyCheckSteps = other.mBusyCheckSteps;
        mStepsUntilNeedCoffee = other.mStepsUntilNeedCoffee;
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

    private boolean isQueuing() {
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
        mShouldReportStateChange = true;
    }

    private void goToWork() {
        assert isQueuing();
        mIsWorking = true;
        mStepsUntilNeedCoffee = mSimulationParameters.stepsUntilNeedCoffee;
        mShouldReportStateChange = true;
    }

    private void reportStateChange() {
        int busyProgress =
                Math.round(
                        (float)(mSimulationParameters.busySteps - mBusySteps)
                                * 100 / mSimulationParameters.busySteps);
        int needCoffeeProgress =
                Math.round(
                        (float)(mSimulationParameters.stepsUntilNeedCoffee - mStepsUntilNeedCoffee)
                                * 100 / mSimulationParameters.stepsUntilNeedCoffee);
        EngineerState state =
                new EngineerState(getId(), isBusy(), isWorking(), busyProgress, needCoffeeProgress);
        mProgressReporter.reportStateChange(state);
    }

    private void doOneWorkingStep() {
        if (isBusy()) {
            if (mBusySteps == 0) {
                setBusy(false);
            } else {
                makeLessBusy();
            }
        }
        
        if (mStepsUntilNeedCoffee == 0) {
            goForCoffee();
        } else {
            workAndDrinkTheCoffee();
        }
    }

    private void doOneQueuingStep(CoffeeMachine coffeeMachine,
                                  CoffeeQueue coffeeQueue) {
        Integer nextIdInQueue = coffeeQueue.next();
        if (nextIdInQueue == getId() && coffeeMachine.isCoffeeReady()) {
            goToWork();
        }
    }
    
    private void maybeBecomeBusy() {
        if (mBusyCheckSteps == mSimulationParameters.busyCheckSteps) {
            mBusyCheckSteps = 0;
            if (Common.eventHappens(mSimulationParameters.busyProb)) {
                setBusy(true);
            }
        } else {
            ++mBusyCheckSteps;
        }
    }
    
    public void doOneStep(CoffeeMachine coffeeMachine,
                          CoffeeQueue coffeeQueue) {
        mShouldReportStateChange = false;

        if (!isBusy()) {
            maybeBecomeBusy();
        }
        
        if (isWorking()) {
            doOneWorkingStep();
        } else {
            doOneQueuingStep(coffeeMachine, coffeeQueue);
        }
        
        if (mShouldReportStateChange) {
            reportStateChange();
        }
    }
}
