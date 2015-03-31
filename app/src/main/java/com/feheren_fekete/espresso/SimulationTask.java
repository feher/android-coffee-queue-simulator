package com.feheren_fekete.espresso;

import android.os.AsyncTask;

public class SimulationTask
        extends AsyncTask<SimulationParameters, Object, Void>
        implements ProgressReporter {

    private Simulation mSimulation;
    private SimulationStateChangeHandler mStateChangeHandler;
    private boolean mIsPaused;
    private String mResumeFlag;

    public SimulationTask(SimulationStateChangeHandler stateChangeHandler) {
        mSimulation = null;
        mStateChangeHandler = stateChangeHandler;
        mIsPaused = false;
        mResumeFlag = "pause-flag";
    }

    @Override
    protected Void doInBackground(SimulationParameters... params) {
        SimulationParameters parameters = params[0];
        mSimulation = new Simulation(parameters, this);
        while (!isCancelled()) {
            mSimulation.doOneStep();
            if (mIsPaused) {
                waitForResume();
            }
        }
        return null;
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

    public void waitForResume() {
        synchronized (mResumeFlag) {
            try {
                mResumeFlag.wait();
            } catch (InterruptedException e) {
                // Nothing.
            }
            mIsPaused = false;
        }
    }

    public void pauseTask() {
        mIsPaused = true;
    }

    public void resumeTask() {
        synchronized (mResumeFlag) {
            mResumeFlag.notify();
        }
    }

    public boolean isTaskPaused() {
        return (getStatus() == Status.RUNNING) && mIsPaused;
    }
}
