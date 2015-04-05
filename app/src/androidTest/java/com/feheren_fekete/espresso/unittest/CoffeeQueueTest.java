package com.feheren_fekete.espresso.unittest;

import com.feheren_fekete.espresso.CoffeeQueue;
import com.feheren_fekete.espresso.EngineerState;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class CoffeeQueueTest extends TestCase {
    public void testNobodyGoesForCoffeeYet() {
        boolean isCoffeeReady = false;
        boolean isBusy = false;
        boolean isWorking = true;
        int busyProgress = 0;
        int needCoffeeProgress = 0;

        List<EngineerState> engineers = new ArrayList< EngineerState>();
        engineers.add(new EngineerState(0, isBusy, isWorking, busyProgress, needCoffeeProgress));
        engineers.add(new EngineerState(1, isBusy, isWorking, busyProgress, needCoffeeProgress));

        CoffeeQueue queue = new CoffeeQueue();
        queue.doOneStep(isCoffeeReady, engineers);

        assertEquals(true, queue.isEmpty());
        assertEquals(true, queue.getIds().isEmpty());
        assertEquals(null, queue.getNext());
    }

    public void testOneEngineerGoesForCoffee() {
        boolean isCoffeeReady = false;
        boolean isBusy = false;
        boolean isWorking = true;
        int busyProgress = 0;
        int needCoffeeProgress = 0;

        List<EngineerState> engineers = new ArrayList<EngineerState>();
        engineers.add(new EngineerState(0, isBusy, isWorking, busyProgress, needCoffeeProgress));
        engineers.add(new EngineerState(1, isBusy, !isWorking, busyProgress, needCoffeeProgress));

        CoffeeQueue queue = new CoffeeQueue();
        queue.doOneStep(isCoffeeReady, engineers);

        assertEquals(false, queue.isEmpty());
        assertEquals(1, queue.getIds().size());
        assertEquals(new Integer(1), queue.getNext());
    }

    public void testOneEngineerWaitsForCoffee() {
        int engineerId = 1;

        // Engineer #1 is waiting in the queue by default.
        CoffeeQueue queue = new CoffeeQueue();
        List<Integer> busyQueue = new ArrayList<Integer>();
        busyQueue.add(new Integer(engineerId));
        queue.setBusyQueue(busyQueue);

        // Engineer #1 wants to go to work.
        List<EngineerState> engineerStates = new ArrayList<EngineerState>();
        engineerStates.add(new EngineerState(engineerId, false, true, 0, 0));

        // Coffee is not ready yet, so engineer #1 has to wait.
        boolean isCoffeeReady = false;
        queue.doOneStep(isCoffeeReady, engineerStates);

        assertEquals(false, queue.isEmpty());
        assertEquals(1, queue.getIds().size());
        assertEquals(new Integer(1), queue.getNext());
    }

    public void testOneEngineerGoesToWork() {
        int engineerId = 1;

        // Engineer #1 is waiting in the queue by default.
        CoffeeQueue queue = new CoffeeQueue();
        List<Integer> busyQueue = new ArrayList<Integer>();
        busyQueue.add(new Integer(engineerId));
        queue.setBusyQueue(busyQueue);

        // Engineer #1 wants to go to work.
        List<EngineerState> engineerStates = new ArrayList<EngineerState>();
        engineerStates.add(new EngineerState(engineerId, false, true, 0, 0));

        // Coffee is ready, so engineer #1 can go back to work.
        boolean isCoffeeReady = true;
        queue.doOneStep(isCoffeeReady, engineerStates);

        assertEquals(true, queue.isEmpty());
        assertEquals(0, queue.getIds().size());
        assertEquals(null, queue.getNext());
    }

}
