package com.feheren_fekete.espresso.unittest;

import com.feheren_fekete.espresso.CoffeeMachine;
import com.feheren_fekete.espresso.InputValues;
import com.feheren_fekete.espresso.SimulationParameters;

import junit.framework.TestCase;

public class CoffeeMachineTest extends TestCase {
    SimulationParameters mParameters;

    @Override
    protected void setUp() throws Exception {
        InputValues inputValues = new InputValues();
        inputValues.secondsUntilCoffeeReady = 30;
        mParameters = new SimulationParameters(inputValues);
    }

    public void testBrewingProcess() {
        CoffeeMachine machine = new CoffeeMachine(mParameters);
        assertEquals(true, machine.isIdle());
        assertEquals(false, machine.isCoffeeReady());

        machine.startBrewing();
        assertEquals(false, machine.isIdle());
        assertEquals(false, machine.isCoffeeReady());

        for (int i = 0; i < mParameters.stepsUntilCoffeeReady; ++i) {
            assertEquals(false, machine.isIdle());
            assertEquals(false, machine.isCoffeeReady());
            machine.doOneStep(true);
        }
        assertEquals(true, machine.isCoffeeReady());
        assertEquals(true, machine.isIdle());
    }

    public void testSomebodyTakesReadyCoffee() {
        CoffeeMachine machine = new CoffeeMachine(mParameters);
        assertEquals(true, machine.isIdle());
        assertEquals(false, machine.isCoffeeReady());

        machine.setCoffeeReady(true);
        assertEquals(true, machine.isIdle());
        assertEquals(true, machine.isCoffeeReady());

        machine.doOneStep(false);
        assertEquals(true, machine.isIdle());
        assertEquals(false, machine.isCoffeeReady());
    }

    public void testNobodyTakesReadyCoffee() {
        CoffeeMachine machine = new CoffeeMachine(mParameters);
        assertEquals(true, machine.isIdle());
        assertEquals(false, machine.isCoffeeReady());

        machine.setCoffeeReady(true);
        assertEquals(true, machine.isIdle());
        assertEquals(true, machine.isCoffeeReady());

        machine.doOneStep(true);
        assertEquals(true, machine.isIdle());
        assertEquals(true, machine.isCoffeeReady());
    }

    public void testStartBrewingIfSomeoneWaiting() {
        CoffeeMachine machine = new CoffeeMachine(mParameters);
        assertEquals(true, machine.isIdle());
        assertEquals(false, machine.isCoffeeReady());

        machine.doOneStep(false);
        assertEquals(false, machine.isIdle());
        assertEquals(false, machine.isCoffeeReady());
    }
}
