package com.feheren_fekete.espresso;

public class EngineerState {
    public static int CHANGED_NOTHING = 0;
    public static int CHANGED_IS_BUSY = 1 << 0;
    public static int CHANGED_IS_WORKING = 1 << 1;
    public static int CHANGED_BUSY_PROGRESS = 1 << 2;
    public static int CHANGED_NEED_COFFEE_PROGRESS = 1 << 3;

    private Data mData;

    private static class Data {
        public int mId;
        public boolean mIsBusy;
        public boolean mIsWorking;
        public int mBusyProgress;
        public int mNeedCoffeeProgress;
        public int mChangedState;
        public Data() {
        }
        public Data(Data other) {
            mId = other.mId;
            mIsBusy = other.mIsBusy;
            mIsWorking = other.mIsWorking;
            mBusyProgress = other.mBusyProgress;
            mNeedCoffeeProgress = other.mNeedCoffeeProgress;
            mChangedState = other.mChangedState;
        }
    }

    public static class Builder {
        private Data mData;
        public Builder() {
            mData = new Data();
        }
        public Builder setId(int id) {
            mData.mId = id;
            return this;
        }
        public Builder setBusy(boolean isBusy) {
            mData.mIsBusy = isBusy;
            return this;
        }
        public Builder setWorking(boolean isWorking) {
            mData.mIsWorking = isWorking;
            return this;
        }
        public Builder setBusyProgress(int busyProgress) {
            mData.mBusyProgress = busyProgress;
            return this;
        }
        public Builder setNeedCoffeeProgress(int needCoffeeProgress) {
            mData.mNeedCoffeeProgress = needCoffeeProgress;
            return this;
        }
        public Builder setChangedState(int changedState) {
            mData.mChangedState = changedState;
            return this;
        }
        public EngineerState build() {
            return new EngineerState(mData);
        }
    }


    private EngineerState(Data data) {
        mData = data;
    }

    public EngineerState(EngineerState other) {
        mData = new Data(other.mData);
    }

    public boolean isStateChanged() {
        return (mData.mChangedState != CHANGED_NOTHING);
    }

    public void clearChangedStates() {
        mData.mChangedState = CHANGED_NOTHING;
    }

    public void setBusy(boolean isBusy) {
        if (mData.mIsBusy != isBusy) {
            mData.mChangedState |= CHANGED_IS_BUSY;
        }
        mData.mIsBusy = isBusy;
    }

    public void setWorking(boolean isWorking) {
        if (mData.mIsWorking != isWorking) {
            mData.mChangedState |= CHANGED_IS_WORKING;
        }
        mData.mIsWorking = isWorking;
    }

    public void setBusyProgress(int busyProgress) {
        if (mData.mBusyProgress != busyProgress) {
            mData.mChangedState |= CHANGED_BUSY_PROGRESS;
        }
        mData.mBusyProgress = busyProgress;
    }

    public void setNeedCoffeeProgress(int needCoffeeProgress) {
        if (mData.mNeedCoffeeProgress != needCoffeeProgress) {
            mData.mChangedState |= CHANGED_NEED_COFFEE_PROGRESS;
        }
        mData.mNeedCoffeeProgress = needCoffeeProgress;
    }

    public int getId() {
        return mData.mId;
    }

    public boolean isBusy() {
        return mData.mIsBusy;
    }

    public boolean isWorking() {
        return mData.mIsWorking;
    }

    public boolean isQueuing() {
        return !isWorking();
    }

    public int getBusyProgress() {
        return mData.mBusyProgress;
    }

    public int getNeedCoffeeProgress() {
        return mData.mNeedCoffeeProgress;
    }

    @Override
    public String toString() {
        String place = mData.mIsWorking ? "Working" : "Queuing for coffee";
        String business = mData.mIsBusy ? "Busy" : "Not busy";

        return "Id: " + mData.mId + "\n" + place + "\n" + business;
    }
}
