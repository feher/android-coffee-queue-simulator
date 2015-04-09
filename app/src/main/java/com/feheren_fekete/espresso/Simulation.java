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

    public List<EngineerState> getEngineerStates() {
        List<EngineerState> states = new ArrayList<>(mEngineers.size());
        for (Engineer engineer : mEngineers) {
            states.add(engineer.getState());
        }
        return states;
    }

    public void doOneStep() {
        boolean isCoffeeReady = mCoffeeMachine.isCoffeeReady();
        int nextIdInQueue = mCoffeeQueue.getNext();
        boolean isQueueEmpty = mCoffeeQueue.isEmpty();
        List<EngineerState> engineerStates = getEngineerStates();

        mCoffeeMachine.doOneStep(isQueueEmpty);
        if (mCoffeeMachine.hasNewState()) {
            mProgressReporter.reportStateChange(mCoffeeMachine.getState());
        }

        mCoffeeQueue.doOneStep(engineerStates);
        if (mCoffeeQueue.hasNewState()) {
            mProgressReporter.reportStateChange(mCoffeeQueue.getState());
        }

        for (Engineer engineer : mEngineers) {
            engineer.doOneStep(isCoffeeReady, nextIdInQueue);
            if (engineer.hasNewState()) {
                mProgressReporter.reportStateChange(engineer.getState());
            }
        }

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
