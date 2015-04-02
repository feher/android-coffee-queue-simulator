package com.feheren_fekete.espresso;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private SimulationParameters mParameters;
    private ProgressReporter mProgressReporter;
    private CoffeeMachine mCoffeeMachine;
    private CoffeeQueue mCoffeeQueue;
    private List<Engineer> mEngineers;

    public Simulation(SimulationParameters parameters,
                      ProgressReporter reporter,
                      CoffeeMachine coffeeMachine,
                      CoffeeQueue coffeeQueue,
                      List<Engineer> engineers) {
        mParameters = parameters;
        mProgressReporter = reporter;
        mCoffeeMachine = coffeeMachine;
        mCoffeeQueue = coffeeQueue;
        mEngineers = engineers;
    }

    public void doOneStep() {
        // The engineers depend on the coffee machine.
        // So we must update the coffee machine first.
        mCoffeeMachine.doOneStep();
        for (Engineer engineer : mEngineers) {
            engineer.doOneStep(mCoffeeMachine, mCoffeeQueue);
        }

        for (Engineer engineer : mEngineers) {
            if (engineer.isQueuing()) {
                if (mCoffeeQueue.contains(engineer)) {
                    mCoffeeQueue.update(engineer);
                } else {
                    mCoffeeQueue.add(engineer);
                }
            }
        }

        if (!mCoffeeQueue.isEmpty()) {
            if (mCoffeeMachine.isCoffeeReady()) {
                mCoffeeMachine.takeCoffee();
                mCoffeeQueue.removeNext();
            } else if (mCoffeeMachine.isIdle()) {
                mCoffeeMachine.startBrewing();
            }
        }

        reportStateChanges();

        sleepForAWhile();
    }

    private void reportStateChanges() {
        for (Engineer engineer : mEngineers) {
            if (engineer.hasNewState()) {
                mProgressReporter.reportStateChange(engineer.getState());
            }
        }

        if (mCoffeeMachine.hasNewState()) {
            mProgressReporter.reportStateChange(mCoffeeMachine.getState());
        }

        if (mCoffeeQueue.hasNewState()) {
            mProgressReporter.reportStateChange(mCoffeeQueue.getState());
        }
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
