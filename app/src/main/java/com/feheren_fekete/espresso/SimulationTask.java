package com.feheren_fekete.espresso;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimulationTask
        extends AsyncTask<SimulationParameters, Object, Void>
        implements ProgressReporter {

    private Simulation mSimulation;
    private SimulationStateChangeHandler mStateChangeHandler;
    private boolean mIsPaused;
    private String mResumeFlag;
    private List<Object> mStateChanges;

    public SimulationTask(SimulationStateChangeHandler stateChangeHandler) {
        mSimulation = null;
        mStateChangeHandler = stateChangeHandler;
        mIsPaused = false;
        mResumeFlag = "pause-flag";
        mStateChanges = new ArrayList<Object>();
    }

    @Override
    protected Void doInBackground(SimulationParameters... params) {
        SimulationParameters parameters = params[0];
        mSimulation = new Simulation(parameters, this);
        while (!isCancelled()) {
            mStateChanges = new ArrayList<Object>();
            mSimulation.doOneStep();
            if(!mStateChanges.isEmpty()) {
                publishProgress(mStateChanges);
            }
            if (mIsPaused) {
                waitForResume();
            }
        }
        return null;
    }

    @Override
    public void reportStateChange(Object stateChange) {
        mStateChanges.add(stateChange);
    }

    @Override
    protected void onProgressUpdate(Object... progress) {
        List<Object> state = (List<Object>)progress[0];
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
