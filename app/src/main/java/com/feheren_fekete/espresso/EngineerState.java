package com.feheren_fekete.espresso;

public class EngineerState {
    private final int mId;
    private final boolean mIsBusy;
    private final boolean mIsWorking;
    private final int mBusyProgress;
    private final int mNeedCoffeeProgress;

    public EngineerState(int id, boolean isBusy, boolean isWorking, int busyProgress, int needCoffeeProgress) {
        mId = id;
        mIsBusy = isBusy;
        mIsWorking = isWorking;
        mBusyProgress = busyProgress;
        mNeedCoffeeProgress = needCoffeeProgress;
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
