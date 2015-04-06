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

    public void testBrewing() {
        CoffeeMachine machine = new CoffeeMachine(mParameters);
        assertEquals(true, machine.isIdle());
        assertEquals(false, machine.isCoffeeReady());

        machine.startBrewing();
        assertEquals(false, machine.isIdle());
        for (int i = 0; i <= mParameters.stepsUntilCoffeeReady; ++i) {
            machine.doOneStep(true);
        }
        assertEquals(true, machine.isCoffeeReady());
    }
}
