package com.feheren_fekete.espresso;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private SimulationParameters mParameters;
    private CoffeeMachine mCoffeeMachine;
    private CoffeeQueue mCoffeeQueue;
    private List<Engineer> mEngineers;
    private List<Engineer> mEngineersCopy;

    public Simulation(SimulationParameters parameters, ProgressReporter reporter) {
        mParameters = parameters;
        mCoffeeMachine = new CoffeeMachine(parameters, reporter);
        mCoffeeQueue = new CoffeeQueue(reporter);
        mEngineers = new ArrayList<Engineer>();
        mEngineersCopy = null;
        for (int i = 0; i < mParameters.engineerCount; ++i) {
            Engineer engineer = new Engineer(i, parameters, reporter);
            mEngineers.add(engineer);
        }
    }

    public void doOneStep() {
        CoffeeMachine coffeeMachineCopy = new CoffeeMachine(mCoffeeMachine);
        CoffeeQueue coffeeQueueCopy = new CoffeeQueue(mCoffeeQueue);
        mEngineersCopy = new ArrayList<Engineer>();
        for (Engineer engineer : mEngineers) {
            Engineer engineerCopy = new Engineer(engineer);
            mEngineersCopy.add(engineerCopy);
        }

        for (Engineer engineerCopy : mEngineersCopy) {
            engineerCopy.doOneStep(
                    mCoffeeMachine, mCoffeeQueue,
                    coffeeMachineCopy, coffeeQueueCopy);
        }
        coffeeMachineCopy.doOneStep();

        mCoffeeMachine = coffeeMachineCopy;
        mCoffeeQueue = coffeeQueueCopy;
        mEngineers = mEngineersCopy;

        sleepForAWhile();
    }

    private void sleepForAWhile() {
        try {
            double milliSeconds = Math.floor(mParameters.realSecondsPerStep * 1000);
            double nanoSeconds =
                    Math.floor((mParameters.realSecondsPerStep * 1000 - milliSeconds) * 1000 * 1000);
            Thread.sleep((long) milliSeconds, (int) nanoSeconds);
        } catch (InterruptedException e) {
            // Do nothing.
        }
    }
}
