package com.feheren_fekete.espresso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoffeeQueue {

    public static final int INVALID_ID = -1;

    private IdQueue mBusyQueue;
    private IdQueue mNormalQueue;
    private boolean mIsStateChanged;

    private static class IdQueue {
        private int[] mArray;
        private int mSize;

        public IdQueue(int maxSize) {
            mArray = new int[maxSize];
            mSize = 0;
        }

        public void copyIds(int []destination, int offset) {
            for (int i = 0; i < mSize; ++i) {
                destination[offset + i] = mArray[i];
            }
        }

        public int size() {
            return mSize;
        }

        public void add(int item) {
            assert mSize < mArray.length;
            mArray[mSize] = item;
            ++mSize;
        }

        public boolean contains(int item) {
            for (int i = 0; i < mSize; ++i) {
                if (mArray[i] == item) {
                    return true;
                }
            }
            return false;
        }

        public int get(int position) {
            assert position < mSize;
            return mArray[position];
        }

        public boolean isEmpty() {
            return mSize == 0;
        }

        public int remove(int item) {
            int removedCount = 0;
            for (int i = 0; i < mSize; ++i) {
                if (mArray[i] == item) {
                    mArray[i] = INVALID_ID;
                    ++removedCount;
                }
            }
            if (removedCount > 0) {
                int p = 0;
                for (int i = 0; i < mSize; ++i) {
                    if (mArray[i] != INVALID_ID) {
                        if (i != p) {
                            mArray[p] = mArray[i];
                        }
                        ++p;
                    }
                }
                mSize = p;
            }
            return removedCount;
        }
    }

    public CoffeeQueue(int maxCapacity) {
        mBusyQueue = new IdQueue(maxCapacity);
        mNormalQueue = new IdQueue(maxCapacity);
        mIsStateChanged = false;
    }

    public boolean hasNewState() {
        return mIsStateChanged;
    }

    public CoffeeQueueState getState() {
        return new CoffeeQueueState(getIds());
    }

    public boolean isEmpty() {
        return mBusyQueue.isEmpty() && mNormalQueue.isEmpty();
    }

    public int length() {
        return mBusyQueue.size() + mNormalQueue.size();
    }

    private boolean contains(EngineerState engineer) {
        return mBusyQueue.contains(engineer.getId()) || mNormalQueue.contains(engineer.getId());
    }

    public int getNext() {
        if (!mBusyQueue.isEmpty()) {
            return mBusyQueue.get(0);
        }
        if (!mNormalQueue.isEmpty()) {
            return mNormalQueue.get(0);
        }
        return INVALID_ID;
    }

    private void remove(EngineerState engineer) {
        assert contains(engineer);
        if (!mBusyQueue.isEmpty()) {
            mIsStateChanged |= (mBusyQueue.remove(engineer.getId()) != 0);
        }
        if (!mNormalQueue.isEmpty()) {
            mIsStateChanged |= (mNormalQueue.remove(engineer.getId()) != 0);
        }
    }

    private void addToQueue(IdQueue queue, int engineerId) {
        assert !queue.contains(engineerId);
        queue.add(engineerId);
        mIsStateChanged = true;
    }

    public void add(EngineerState engineer) {
        assert !contains(engineer);
        if (engineer.isBusy()) {
            addToQueue(mBusyQueue, engineer.getId());
        } else {
            addToQueue(mNormalQueue, engineer.getId());
        }
    }

    private void update(EngineerState engineer) {
        assert contains(engineer);
        int id = engineer.getId();
        if (engineer.isBusy()) {
            if (!mBusyQueue.contains(id)) {
                if (mNormalQueue.contains(id)) {
                    mNormalQueue.remove(id);
                }
                mBusyQueue.add(id);
            }
        } else {
            if (!mNormalQueue.contains(id)) {
                if (mBusyQueue.contains(id)) {
                    mBusyQueue.remove(id);
                }
                mNormalQueue.add(id);
            }
        }
    }

    public int[] getIds() {
        int[] ids = new int[mBusyQueue.size() + mNormalQueue.size()];
        mBusyQueue.copyIds(ids, 0);
        mNormalQueue.copyIds(ids, mBusyQueue.size());
        return ids;
    }

    public void doOneStep(EngineerState engineer) {
        List<EngineerState> engineers = new ArrayList<>();
        engineers.add(engineer);
        doOneStep(engineers);
    }

    public void doOneStep(List<EngineerState> engineers) {
        mIsStateChanged = false;
        for (EngineerState engineer : engineers) {
            if (engineer.isQueuing()) {
                if (contains(engineer)) {
                    update(engineer);
                } else {
                    add(engineer);
                }
            } else if (engineer.isWorking()) {
                if (contains(engineer)) {
                    remove(engineer);
                }
            }
        }
    }
}
