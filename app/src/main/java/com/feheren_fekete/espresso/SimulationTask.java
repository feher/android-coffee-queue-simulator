package com.feheren_fekete.espresso;

import android.os.AsyncTask;

public class SimulationTask
        extends AsyncTask<SimulationParameters, Object, Void>
        implements ProgressReporter {

    private Simulation mSimulation = null;
    private SimulationStateChangeHandler mStateChangeHandler;

    public SimulationTask(SimulationStateChangeHandler stateChangeHandler) {
        mStateChangeHandler = stateChangeHandler;
    }

    @Override
    protected Void doInBackground(SimulationParameters... params) {
        SimulationParameters parameters = params[0];
        mSimulation = new Simulation(parameters, this);
        while (!isCancelled()) {
            mSimulation.doOneStep();
        }
        return null;
    }

    @Override
    public void reportLog(String message, CoffeeMachine coffeeMachine, CoffeeQueue coffeeQueue) {
        String state = "Event: " + message + "\n" + mSimulation.getState(coffeeMachine, coffeeQueue);
        publishProgress(state);
    }

    @Override
    public void reportStateChange(Object message) {
        publishProgress(message);
    }

    @Override
    protected void onProgressUpdate(Object... progress) {
        Object state = progress[0];
        mStateChangeHandler.onSimulationStateChange(state);
    }
}
