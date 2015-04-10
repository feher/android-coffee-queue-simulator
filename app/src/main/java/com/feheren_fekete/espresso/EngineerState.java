package com.feheren_fekete.espresso;

public class EngineerState {
    private static int CHANGED_NOTHING = 0;
    private static int CHANGED_IS_BUSY = 1 << 0;
    private static int CHANGED_IS_WORKING = 1 << 1;
    private static int CHANGED_BUSY_PROGRESS = 1 << 2;
    private static int CHANGED_NEED_COFFEE_PROGRESS = 1 << 3;

    private Data mData;

    private static class Data {
        public int id;
        public boolean isBusy;
        public boolean isWorking;
        public int busyProgress;
        public int needCoffeeProgress;
        public int changedState;
        public Data() {
        }
        public Data(Data other) {
            id = other.id;
            isBusy = other.isBusy;
            isWorking = other.isWorking;
            busyProgress = other.busyProgress;
            needCoffeeProgress = other.needCoffeeProgress;
            changedState = other.changedState;
        }
    }

    public static class Builder {
        private Data mData;
        public Builder() {
            mData = new Data();
        }
        public Builder setId(int id) {
            mData.id = id;
            return this;
        }
        public Builder setBusy(boolean isBusy) {
            mData.isBusy = isBusy;
            return this;
        }
        public Builder setWorking(boolean isWorking) {
            mData.isWorking = isWorking;
            return this;
        }
        public Builder setBusyProgress(int busyProgress) {
            mData.busyProgress = busyProgress;
            return this;
        }
        public Builder setNeedCoffeeProgress(int needCoffeeProgress) {
            mData.needCoffeeProgress = needCoffeeProgress;
            return this;
        }
        public Builder setChangedState(int changedState) {
            mData.changedState = changedState;
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
        return (mData.changedState != CHANGED_NOTHING);
    }

    public void clearChangedStates() {
        mData.changedState = CHANGED_NOTHING;
    }

    public void setBusy(boolean isBusy) {
        if (mData.isBusy != isBusy) {
            mData.changedState |= CHANGED_IS_BUSY;
        }
        mData.isBusy = isBusy;
    }

    public void setWorking(boolean isWorking) {
        if (mData.isWorking != isWorking) {
            mData.changedState |= CHANGED_IS_WORKING;
        }
        mData.isWorking = isWorking;
    }

    public void setBusyProgress(int busyProgress) {
        if (mData.busyProgress != busyProgress) {
            mData.changedState |= CHANGED_BUSY_PROGRESS;
        }
        mData.busyProgress = busyProgress;
    }

    public void setNeedCoffeeProgress(int needCoffeeProgress) {
        if (mData.needCoffeeProgress != needCoffeeProgress) {
            mData.changedState |= CHANGED_NEED_COFFEE_PROGRESS;
        }
        mData.needCoffeeProgress = needCoffeeProgress;
    }

    public int getId() {
        return mData.id;
    }

    public boolean isBusy() {
        return mData.isBusy;
    }

    public boolean isWorking() {
        return mData.isWorking;
    }

    public boolean isQueuing() {
        return !isWorking();
    }

    public int getBusyProgress() {
        return mData.busyProgress;
    }

    public int getNeedCoffeeProgress() {
        return mData.needCoffeeProgress;
    }

    @Override
    public String toString() {
        String place = mData.isWorking ? "Working" : "Queuing for coffee";
        String business = mData.isBusy ? "Busy" : "Not busy";

        return "Id: " + mData.id + "\n" + place + "\n" + business;
    }
}
