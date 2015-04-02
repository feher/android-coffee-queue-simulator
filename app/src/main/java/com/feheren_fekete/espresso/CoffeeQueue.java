package com.feheren_fekete.espresso;

import java.util.ArrayList;
import java.util.List;

public class CoffeeQueue {

    private final List<Integer> mBusyQueue;
    private final List<Integer> mNormalQueue;

    public CoffeeQueue() {
        mBusyQueue = new ArrayList<Integer>();
        mNormalQueue = new ArrayList<Integer>();
    }

    public boolean hasNewState() {
        return true;
    }

    public CoffeeQueueState getState() {
        return new CoffeeQueueState(getIds());
    }

    public boolean isEmpty() {
        return mBusyQueue.isEmpty() && mNormalQueue.isEmpty();
    }

    public boolean contains(Engineer engineer) {
        return mBusyQueue.contains(engineer.getId()) || mNormalQueue.contains(engineer.getId());
    }

    public Integer getNext() {
        if (!mBusyQueue.isEmpty()) {
            return mBusyQueue.get(0);
        }
        if (!mNormalQueue.isEmpty()) {
            return mNormalQueue.get(0);
        }
        return null;
    }

    public void removeNext() {
        if (!mBusyQueue.isEmpty()) {
            mBusyQueue.remove(0);
        } else if (!mNormalQueue.isEmpty()) {
            mNormalQueue.remove(0);
        }
    }

    private void addToQueue(List<Integer> queue, int engineerId) {
        for (Integer id : queue) {
            if (id == engineerId) {
                return;
            }
        }
        queue.add(engineerId);
    }

    public void add(Engineer engineer) {
        assert !contains(engineer);
        if (engineer.isBusy()) {
            addToQueue(mBusyQueue, engineer.getId());
        } else {
            addToQueue(mNormalQueue, engineer.getId());
        }
    }

    public void update(Engineer engineer) {
        assert contains(engineer);
        int id = engineer.getId();
        if (engineer.isBusy()) {
            if (!mBusyQueue.contains(id)) {
                if (mNormalQueue.contains(id)) {
                    mNormalQueue.remove(new Integer(id));
                }
                mBusyQueue.add(id);
            }
        } else {
            if (!mNormalQueue.contains(id)) {
                if (mBusyQueue.contains(id)) {
                    mBusyQueue.remove(new Integer(id));
                }
                mNormalQueue.add(id);
            }
        }
    }

    public List<Integer> getIds() {
        List<Integer> ids = new ArrayList<Integer>(mBusyQueue);
        ids.addAll(mNormalQueue);
        return ids;
    }
}
