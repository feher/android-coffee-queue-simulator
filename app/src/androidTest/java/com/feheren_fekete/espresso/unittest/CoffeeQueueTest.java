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

        List<EngineerState> engineers = new ArrayList< EngineerState>();
        engineers.add(new EngineerState().setId(0).setWorking(true));
        engineers.add(new EngineerState().setId(1).setWorking(true));

        queue.doOneStep(engineers);
        assertEquals(true, queue.isEmpty());
        assertEquals(0, queue.length());
        assertEquals(CoffeeQueue.INVALID_ID, queue.getNext());
    }

    public void testOneEngineerGoesForCoffee() {
        CoffeeQueue queue = new CoffeeQueue(10);
        assertEquals(true, queue.isEmpty());

        List<EngineerState> engineers = new ArrayList< EngineerState>();
        engineers.add(new EngineerState().setId(0).setWorking(true));
        engineers.add(new EngineerState().setId(1).setWorking(false));

        queue.doOneStep(engineers);
        assertEquals(false, queue.isEmpty());
        assertEquals(1, queue.getNext());
    }

    public void testOneEngineerWaitsForCoffee() {
        // Engineer is waiting in the queue by default.
        CoffeeQueue queue = new CoffeeQueue(10);
        EngineerState engineer = new EngineerState().setId(1).setWorking(false);
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
        queue.add(new EngineerState().setId(1).setWorking(false));

        // Engineer goes back to work.
        queue.doOneStep(new EngineerState().setId(1).setWorking(true));
        assertEquals(true, queue.isEmpty());
        assertEquals(0, queue.length());
        assertEquals(CoffeeQueue.INVALID_ID, queue.getNext());
    }

    public void testArbitraryEngineerGoesToWork() {
        // We have 2 engineers in the queue.
        CoffeeQueue queue = new CoffeeQueue(10);
        queue.add(new EngineerState().setId(3).setWorking(false));
        queue.add(new EngineerState().setId(1).setWorking(false));

        // Engineer #1 goes back to work.
        queue.doOneStep(new EngineerState().setId(1).setWorking(true));
        assertEquals(false, queue.isEmpty());
        assertEquals(1, queue.length());
        assertEquals(3, queue.getNext());
    }

    public void testNextEngineerGoesToWork() {
        // We have 2 engineers in the queue.
        CoffeeQueue queue = new CoffeeQueue(10);
        queue.add(new EngineerState().setId(3).setWorking(false));
        queue.add(new EngineerState().setId(1).setWorking(false));

        // Engineer #1 goes back to work.
        queue.doOneStep(new EngineerState().setId(3).setWorking(true));
        assertEquals(false, queue.isEmpty());
        assertEquals(1, queue.length());
        assertEquals(1, queue.getNext());
    }

    public void testEngineerBecomesBusy() {
        // We have 2 engineers in the queue.
        CoffeeQueue queue = new CoffeeQueue(10);
        queue.add(new EngineerState().setId(3).setWorking(false));
        queue.add(new EngineerState().setId(1).setWorking(false));
        queue.add(new EngineerState().setId(5).setWorking(false));
        assertEquals(false, queue.isEmpty());
        assertEquals(3, queue.length());
        assertEquals(3, queue.getNext());

        // Engineer #1 goes back to work.
        queue.doOneStep(new EngineerState().setId(1).setBusy(true));
        assertEquals(false, queue.isEmpty());
        assertEquals(3, queue.length());
        assertEquals(1, queue.getNext());
    }

}
