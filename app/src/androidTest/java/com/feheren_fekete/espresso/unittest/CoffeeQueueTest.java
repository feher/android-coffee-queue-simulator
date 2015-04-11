package com.feheren_fekete.espresso.unittest;

import com.feheren_fekete.espresso.CoffeeQueue;
import com.feheren_fekete.espresso.EngineerState;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class CoffeeQueueTest extends TestCase {

    public void testNobodyGoesForCoffeeYet() {
        CoffeeQueue queue = new CoffeeQueue(10);
        assertEquals(true, queue.isEmpty());

        List<EngineerState> engineers = new ArrayList<>();
        engineers.add(new EngineerState.Builder().setId(0).setWorking(true).build());
        engineers.add(new EngineerState.Builder().setId(1).setWorking(true).build());

        queue.doOneStep(engineers);
        assertEquals(true, queue.isEmpty());
        assertEquals(0, queue.length());
        assertEquals(CoffeeQueue.INVALID_ID, queue.getNext());
    }

    public void testOneEngineerGoesForCoffee() {
        CoffeeQueue queue = new CoffeeQueue(10);
        assertEquals(true, queue.isEmpty());

        List<EngineerState> engineers = new ArrayList<>();
        engineers.add(new EngineerState.Builder().setId(0).setWorking(true).build());
        engineers.add(new EngineerState.Builder().setId(1).setWorking(false).build());

        queue.doOneStep(engineers);
        assertEquals(false, queue.isEmpty());
        assertEquals(1, queue.getNext());
    }

    public void testOneEngineerWaitsForCoffee() {
        // Engineer is waiting in the queue by default.
        CoffeeQueue queue = new CoffeeQueue(10);
        EngineerState engineer = new EngineerState.Builder().setId(1).setWorking(false).build();
        queue.add(engineer);

        // Engineer did nothing, still waiting in queue.
        queue.doOneStep(engineer);
        assertEquals(false, queue.isEmpty());
        assertEquals(1, queue.length());
        assertEquals(1, queue.getNext());
    }

    public void testOneEngineerGoesToWork() {
        // Engineer is waiting in the queue by default.
        CoffeeQueue queue = new CoffeeQueue(10);
        queue.add(new EngineerState.Builder().setId(1).setWorking(false).build());

        // Engineer goes back to work.
        queue.doOneStep(new EngineerState.Builder().setId(1).setWorking(true).build());
        assertEquals(true, queue.isEmpty());
        assertEquals(0, queue.length());
        assertEquals(CoffeeQueue.INVALID_ID, queue.getNext());
    }

    public void testArbitraryEngineerGoesToWork() {
        // We have 2 engineers in the queue.
        CoffeeQueue queue = new CoffeeQueue(10);
        queue.add(new EngineerState.Builder().setId(3).setWorking(false).build());
        queue.add(new EngineerState.Builder().setId(1).setWorking(false).build());

        // Engineer #1 goes back to work.
        queue.doOneStep(new EngineerState.Builder().setId(1).setWorking(true).build());
        assertEquals(false, queue.isEmpty());
        assertEquals(1, queue.length());
        assertEquals(3, queue.getNext());
    }

    public void testNextEngineerGoesToWork() {
        // We have 2 engineers in the queue.
        CoffeeQueue queue = new CoffeeQueue(10);
        queue.add(new EngineerState.Builder().setId(3).setWorking(false).build());
        queue.add(new EngineerState.Builder().setId(1).setWorking(false).build());

        // Engineer #1 goes back to work.
        queue.doOneStep(new EngineerState.Builder().setId(3).setWorking(true).build());
        assertEquals(false, queue.isEmpty());
        assertEquals(1, queue.length());
        assertEquals(1, queue.getNext());
    }

    public void testEngineerBecomesBusy() {
        // We have 2 engineers in the queue.
        CoffeeQueue queue = new CoffeeQueue(10);
        queue.add(new EngineerState.Builder().setId(3).setWorking(false).build());
        queue.add(new EngineerState.Builder().setId(1).setWorking(false).build());
        queue.add(new EngineerState.Builder().setId(5).setWorking(false).build());
        assertEquals(false, queue.isEmpty());
        assertEquals(3, queue.length());
        assertEquals(3, queue.getNext());

        // Engineer #1 goes back to work.
        queue.doOneStep(new EngineerState.Builder().setId(1).setBusy(true).build());
        assertEquals(false, queue.isEmpty());
        assertEquals(3, queue.length());
        assertEquals(1, queue.getNext());
    }

}
