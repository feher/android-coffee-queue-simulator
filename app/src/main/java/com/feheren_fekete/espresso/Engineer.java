package com.feheren_fekete.espresso;

import java.util.Random;

public class Engineer {
    public static final char STATE_TEXT_BUSY = 'B';
    public static final char STATE_TEXT_NOT_BUSY = 'N';
    public static final char STATE_TEXT_WORKING = 'W';
    public static final char STATE_TEXT_QUEUING_FOR_COFFEE = 'C';
    public static final char STATE_TEXT_SEPARATOR = '|';

    private SimulationParameters mSimulationParameters;
    private ProgressReporter mProgressReporter;
    private int mId;
    private boolean mIsWorking;
    private boolean mIsBusy;
    private int mBusySteps;
    private int mBusyCheckSteps;
    private int mStepsUntilNeedCoffee;

    public Engineer(int id, SimulationParameters parameters, ProgressReporter reporter) {
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

    private void setBusy(CoffeeQueue coffeeQueue, boolean isBusy) {
        assert mIsBusy != isBusy;
        mIsBusy = isBusy;
        mBusySteps = mSimulationParameters.busySteps;
        if (isQueuing()) {
            coffeeQueue.remove(mId);
            coffeeQueue.add(mId, mIsBusy);
        }
    }

    public boolean isWorking() {
        return mIsWorking;
    }

    private boolean isQueuing() {
        return !isWorking();
    }

    private void goForCoffee(CoffeeQueue coffeeQueue) {
        assert isWorking();
        coffeeQueue.add(mId, mIsBusy);
        mIsWorking = false;
    }

    private void goToWork(CoffeeMachine coffeeMachine,
                          CoffeeQueue coffeeQueue) {
        assert isQueuing();
        coffeeMachine.takeCoffee();
        coffeeQueue.remove(mId);
        mIsWorking = true;
        mStepsUntilNeedCoffee = mSimulationParameters.stepsUntilNeedCoffee;
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

    public void doOneStep(CoffeeMachine coffeeMachine,
                          CoffeeQueue coffeeQueue,
                          CoffeeMachine coffeeMachineCopy,
                          CoffeeQueue coffeeQueueCopy) {
        final boolean wasBusy = isBusy();
        if (isWorking()) {
            if (isBusy()) {
                if (mBusySteps == 0) {
                    setBusy(coffeeQueueCopy, false);
                    reportStateChange();
                } else {
                    --mBusySteps;
                    reportStateChange();
                }
            }
            if (mStepsUntilNeedCoffee == 0) {
                if (!wasBusy
                    || (wasBusy && coffeeQueueCopy.getLength() < mSimulationParameters.maxQueueLengthWhenBusy)) {
                    goForCoffee(coffeeQueueCopy);
                }
                reportStateChange();
            } else {
                --mStepsUntilNeedCoffee;
                reportStateChange();
            }
        } else {
            Integer nextIdInQueue = coffeeQueue.next();
            if (nextIdInQueue == mId) {
                if (coffeeMachine.isCoffeeReady()) {
                    goToWork(coffeeMachineCopy, coffeeQueueCopy);
                    reportStateChange();
                } else if (coffeeMachine.isIdle()) {
                    coffeeMachineCopy.startBrewing();
                    reportStateChange();
                }
            }
        }

        if (!wasBusy) {
            if (mBusyCheckSteps == mSimulationParameters.busyCheckSteps) {
                if (Common.eventHappens(mSimulationParameters.busyProb)) {
                    setBusy(coffeeQueueCopy, true);
                    reportStateChange();
                }
                mBusyCheckSteps = 0;
            } else {
                ++mBusyCheckSteps;
            }
        }
    }

    public String getState() {
        String state = getId() + "[";
        if (isBusy()) {
            state += "B,";
        } else {
            state += "_,";
        }
        state += mBusySteps + "," + mStepsUntilNeedCoffee + "]";
        return state;
    }
}
