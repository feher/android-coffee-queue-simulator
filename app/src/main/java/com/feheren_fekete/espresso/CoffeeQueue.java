package com.feheren_fekete.espresso;

import java.util.ArrayList;
import java.util.List;

public class CoffeeQueue {

    private final ProgressReporter mProgressReporter;
    private final List<Integer> mBusyQueue;
    private final List<Integer> mNormalQueue;

    public CoffeeQueue(ProgressReporter reporter) {
        mBusyQueue = new ArrayList<Integer>();
        mNormalQueue = new ArrayList<Integer>();
        mProgressReporter = reporter;
    }

    public CoffeeQueue(CoffeeQueue other) {
        mBusyQueue = new ArrayList<Integer>(other.mBusyQueue);
        mNormalQueue = new ArrayList<Integer>(other.mNormalQueue);
        mProgressReporter = other.mProgressReporter;
    }

    public Integer next() {
        if (!mBusyQueue.isEmpty()) {
            return mBusyQueue.get(0);
        }
        if (!mNormalQueue.isEmpty()) {
            return mNormalQueue.get(0);
        }
        return null;
    }

    private void reportStateChange() {
        mProgressReporter.reportStateChange(new CoffeeQueueState(getIds()));
    }

    private void addToQueue(List<Integer> queue, int engineerId) {
        for (Integer id : queue) {
            if (id == engineerId) {
                return;
            }
        }
        queue.add(engineerId);
    }

    public void add(int engineerId, boolean isBusy) {
        if (isBusy) {
            addToQueue(mBusyQueue, engineerId);
        } else {
            addToQueue(mNormalQueue, engineerId);
        }
        reportStateChange();
    }

    public void remove(int engineerId) {
        mBusyQueue.remove(new Integer(engineerId));
        mNormalQueue.remove(new Integer(engineerId));
        reportStateChange();
    }

    public List<Integer> getIds() {
        List<Integer> ids = new ArrayList<Integer>(mBusyQueue);
        ids.addAll(mNormalQueue);
        return ids;
    }

    public int getLength() {
        return mBusyQueue.size() + mNormalQueue.size();
    }
}
