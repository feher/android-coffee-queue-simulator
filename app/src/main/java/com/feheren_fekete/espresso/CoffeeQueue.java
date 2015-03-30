package com.feheren_fekete.espresso;

import java.util.ArrayList;

public class CoffeeQueue implements Cloneable {

    private class QueueItem {
        public final int engineerId;
        public boolean isBusy;
        public QueueItem(int engineerId, boolean isBusy) {
            this.engineerId = engineerId;
            this.isBusy = isBusy;
        }
    }

    private ArrayList<QueueItem> mQueue;

    public CoffeeQueue() {
        mQueue = new ArrayList<QueueItem>();
    }

    @Override
    public Object clone() {
        CoffeeQueue coffeeQueue = new CoffeeQueue();
        for (QueueItem item : mQueue) {
            coffeeQueue.mQueue.add(new QueueItem(item.engineerId, item.isBusy));
        }
        return coffeeQueue;
    }

    public Integer next() {
        if (mQueue.isEmpty()) {
            return null;
        }
        for (QueueItem item : mQueue) {
            if (item.isBusy) {
                return item.engineerId;
            }
        }
        return mQueue.get(0).engineerId;
    }

    public void add(int engineerId, boolean isBusy) {
        for (QueueItem item : mQueue) {
            if (item.engineerId == engineerId) {
                item.isBusy = isBusy;
                return;
            }
        }
        mQueue.add(new QueueItem(engineerId, isBusy));
    }

    public void update(int engineerId, boolean isBusy) {
        for (QueueItem item : mQueue) {
            if (item.engineerId == engineerId) {
                item.isBusy = isBusy;
                return;
            }
        }
        throw new RuntimeException("Id not found in queue: " + engineerId);
    }

    public void remove(int engineerId) {
        for (QueueItem item : mQueue) {
            if (item.engineerId == engineerId) {
                mQueue.remove(item);
                return;
            }
        }
    }

    public ArrayList<Integer> getIds() {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (QueueItem item : mQueue) {
            ids.add(item.engineerId);
        }
        return ids;
    }

    public int getLength() {
        return mQueue.size();
    }
}
