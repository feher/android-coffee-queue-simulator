package com.feheren_fekete.espresso;

public class EngineerState {
    private int mId;
    private boolean mIsBusy;
    private boolean mIsWorking;
    private int mBusyProgress;
    private int mNeedCoffeeProgress;

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
    }

    public EngineerState() {
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
