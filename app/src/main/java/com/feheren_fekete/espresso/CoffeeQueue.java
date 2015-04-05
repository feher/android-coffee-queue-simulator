package com.feheren_fekete.espresso;

import java.util.ArrayList;
import java.util.List;

public class CoffeeQueue {

    private List<Integer> mBusyQueue;
    private List<Integer> mNormalQueue;

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

    private boolean contains(EngineerState engineer) {
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

    private void remove(EngineerState engineer) {
        assert contains(engineer);
        if (!mBusyQueue.isEmpty()) {
            mBusyQueue.remove(new Integer(engineer.getId()));
        }
        if (!mNormalQueue.isEmpty()) {
            mNormalQueue.remove(new Integer(engineer.getId()));
        }
    }

    private void addToQueue(List<Integer> queue, int engineerId) {
        assert !queue.contains(new Integer(engineerId));
        queue.add(engineerId);
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

    public void doOneStep(EngineerState engineer) {
        List<EngineerState> engineers = new ArrayList<EngineerState>();
        engineers.add(engineer);
        doOneStep(engineers);
    }

    public void doOneStep(List<EngineerState> engineers) {
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
