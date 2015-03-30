package com.feheren_fekete.espresso;

import java.util.Random;

public class Engineer implements Cloneable {
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

    private Engineer() {
        mSimulationParameters = null;
        mProgressReporter = null;
        mId = -1;
        mIsWorking = true;
        mIsBusy = false;
        mBusySteps = 0;
        mBusyCheckSteps = 0;
        mStepsUntilNeedCoffee = 0;
    }

    public static Engineer make(int id, SimulationParameters parameters, ProgressReporter reporter) {
        Engineer e = new Engineer();
        e.mSimulationParameters = parameters;
        e.mProgressReporter = reporter;
        e.mId = id;
        e.mIsWorking = true;
        e.mIsBusy = false;
        e.mBusySteps = 0;
        e.mBusyCheckSteps = 0;
        e.mStepsUntilNeedCoffee = Math.round(Common.random.nextFloat() * parameters.stepsUntilNeedCoffee);
        if (Common.eventHappens(parameters.busyProb)) {
            e.mIsBusy = true;
            e.mBusySteps = parameters.busySteps;
        }
        return e;
    }

    @Override
    public Object clone() {
        Engineer e = new Engineer();
        e.mSimulationParameters = mSimulationParameters;
        e.mProgressReporter = mProgressReporter;
        e.mId = mId;
        e.mIsWorking = mIsWorking;
        e.mIsBusy = mIsBusy;
        e.mBusySteps = mBusySteps;
        e.mBusyCheckSteps = mBusyCheckSteps;
        e.mStepsUntilNeedCoffee = mStepsUntilNeedCoffee;
        return e;
    }

    public int getId() {
        return mId;
    }

    public boolean isBusy() {
        return mIsBusy;
    }

    private void setBusy(CoffeeQueue coffeeQueue, boolean isBusy) {
        mIsBusy = isBusy;
        mBusySteps = mSimulationParameters.busySteps;
        if (isQueuing()) {
            coffeeQueue.update(mId, mIsBusy);
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
        String busy = isBusy() ? "B" : "N";
        int busyProgress =
                Math.round(
                        (float)(mSimulationParameters.busySteps - mBusySteps) * 100 / mSimulationParameters.busySteps);
        String working = isWorking() ? "W" : "C";
        int needCoffeeProgress =
                Math.round(
                        (float)(mSimulationParameters.stepsUntilNeedCoffee - mStepsUntilNeedCoffee) * 100
                        / mSimulationParameters.stepsUntilNeedCoffee);
        mProgressReporter.reportStateChange(
                Common.STATE_CHANGE_ENGINEER
                + getId() + "|"
                + busy + busyProgress + "|"
                + working + needCoffeeProgress);
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
                    mProgressReporter.reportLog(
                            getState() + " goes for coffee",
                            coffeeMachineCopy, coffeeQueueCopy);
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
                    mProgressReporter.reportLog(getState() + " takes coffee and goes to work",
                            coffeeMachineCopy, coffeeQueueCopy);
                    reportStateChange();
                } else if (coffeeMachine.isIdle()) {
                    coffeeMachineCopy.startBrewing();
                    mProgressReporter.reportLog(
                            getState() + " starts brewing coffee",
                            coffeeMachineCopy, coffeeQueueCopy);
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
