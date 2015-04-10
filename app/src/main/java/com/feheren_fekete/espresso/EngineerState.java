package com.feheren_fekete.espresso;

public class EngineerState {
    public static int CHANGED_NOTHING = 0;
    public static int CHANGED_IS_BUSY = 1 << 0;
    public static int CHANGED_IS_WORKING = 1 << 1;
    public static int CHANGED_BUSY_PROGRESS = 1 << 2;
    public static int CHANGED_NEED_COFFEE_PROGRESS = 1 << 3;

    private int mId;
    private boolean mIsBusy;
    private boolean mIsWorking;
    private int mBusyProgress;
    private int mNeedCoffeeProgress;
    private int mChangedState;

    public EngineerState(int id,
                         boolean isBusy,
                         boolean isWorking,
                         int busyProgress,
                         int needCoffeeProgress) {
        mId = id;
        mIsBusy = isBusy;
        mIsWorking = isWorking;
        mBusyProgress = busyProgress;
        mNeedCoffeeProgress = needCoffeeProgress;
        mChangedState = CHANGED_NOTHING;
    }

    public EngineerState(EngineerState other) {
        mId = other.mId;
        mIsBusy = other.mIsBusy;
        mIsWorking = other.mIsWorking;
        mBusyProgress = other.mBusyProgress;
        mNeedCoffeeProgress = other.mNeedCoffeeProgress;
        mChangedState = other.mChangedState;
    }

    public EngineerState() {
    }

    public void setChangedState(int stateFlag) {
        mChangedState |= stateFlag;
    }

    public boolean isStateChanged(int stateFlag) {
        return (mChangedState & stateFlag) != 0;
    }

    public void clearChangedStates() {
        mChangedState = CHANGED_NOTHING;
    }

    public int getChangedState() {
        return mChangedState;
    }

    public EngineerState setId(int id) {
        mId = id;
        return this;
    }

    public EngineerState setBusy(boolean isBusy) {
        mIsBusy = isBusy;
        return this;
    }

    public EngineerState setWorking(boolean isWorking) {
        mIsWorking = isWorking;
        return this;
    }

    public EngineerState setBusyProgress(int busyProgress) {
        mBusyProgress = busyProgress;
        return this;
    }

    public EngineerState setNeedCoffeeProgress(int needCoffeeProgress) {
        mNeedCoffeeProgress = needCoffeeProgress;
        return this;
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
        return !mIsWorking;
    }

    public int getBusyProgress() {
        return mBusyProgress;
    }

    public int getNeedCoffeeProgress() {
        return mNeedCoffeeProgress;
    }

    @Override
    public String toString() {
        String place = mIsWorking ? "Working" : "Queuing for coffee";
        String business = mIsBusy ? "Busy" : "Not busy";

        return "Id: " + mId + "\n" + place + "\n" + business;
    }
}
