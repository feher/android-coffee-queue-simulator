package com.feheren_fekete.espresso;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class SimulationActivity
        extends ActionBarActivity {

    private static final String MESSAGE_LOG = "Log:";
    private static final String MESSAGE_STATE_CHANGE = "State Change:";

    private ImageView mCoffeeMachineImage;
    private TextView mCoffeeMachineState;
    private ProgressBar mCoffeeMachineProgressBar;
    private ListView mEngineersListView;
    private ListView mCoffeeQueueListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);

        mCoffeeMachineImage = (ImageView) findViewById(R.id.image_view__coffee_machine_state);
        mCoffeeMachineState = (TextView) findViewById(R.id.text_view__coffee_machine);
        mCoffeeMachineProgressBar = (ProgressBar) findViewById(R.id.progress_bar__coffee_machine);

        mEngineersListView = (ListView) findViewById(R.id.list_view__engineers);
        mCoffeeQueueListView = (ListView) findViewById(R.id.list_view__coffee_queue);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            float realSecondsPerStep = extras.getFloat(Common.SIMULATION_REAL_SECONDS_PER_STEP);
            int engineerSecondsPerStep = extras.getInt(Common.SIMULATION_ENGINEER_SECONDS_PER_STEP);
            int engineerCount = extras.getInt(Common.SIMULATION_ENGINEER_COUNT);
            int busyProb = extras.getInt(Common.SIMULATION_BUSY_PROBABILITY);
            int busySeconds = extras.getInt(Common.SIMULATION_BUSY_SECONDS);
            int secondsUntilNeedCoffee = extras.getInt(Common.SIMULATION_SECONDS_UNTIL_NEED_COFFEE);
            int secondsUntilCoffeeReady = extras.getInt(Common.SIMULATION_SECONDS_UNTIL_COFFEE_READY);
            int maxQueueLengthWhenBusy = extras.getInt(Common.SIMULATION_MAX_QUEUE_LENGTH_WHEN_BUSY);
            int busyCheckSeconds = extras.getInt(Common.SIMULATION_BUSY_CHECK_SECONDS);

            createEngineerList(engineerCount);
            createCoffeeQueueList();

            SimulationParameters parameters =
                    new SimulationParameters(
                            realSecondsPerStep,
                            engineerSecondsPerStep,
                            engineerCount,
                            busyProb,
                            busyCheckSeconds,
                            busySeconds,
                            secondsUntilNeedCoffee,
                            secondsUntilCoffeeReady,
                            maxQueueLengthWhenBusy);
            SimulationAsyncTask simulationAsyncTask = new SimulationAsyncTask();
            simulationAsyncTask.execute(parameters);
        }
    }

    private void createEngineerList(int engineerCount) {
        ArrayList<EngineerState> engineerList = new ArrayList<EngineerState>();
        for (int i = 0; i < engineerCount; ++i) {
            engineerList.add(new EngineerState(i, false, true, 0, 0));
        }
        EngineerListAdapter engineerListAdapter = new EngineerListAdapter(this, engineerList);
        mEngineersListView.setAdapter(engineerListAdapter);
    }

    private void createCoffeeQueueList() {
        EngineerListAdapter engineerListAdapter =
                new EngineerListAdapter(this, new ArrayList<EngineerState>());
        mCoffeeQueueListView.setAdapter(engineerListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simulation, menu);
        return true;
    }

    private class SimulationAsyncTask
            extends AsyncTask<SimulationParameters, Object, Void>
            implements ProgressReporter {

        private Simulation mSimulation = null;

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
            publishProgress(MESSAGE_LOG + state);
        }

        @Override
        public void reportStateChange(Object message) {
            publishProgress(message);
        }

        @Override
        protected void onProgressUpdate(Object... progress) {
            Object message = progress[0];
            if (message instanceof String) {
                updateLog(((String) message).substring(MESSAGE_LOG.length()));
            } else if (message instanceof CoffeeMachineState) {
                updateCoffeeMachineState((CoffeeMachineState) message);
            } else if (message instanceof EngineerState) {
                updateEngineerState((EngineerState)message);
            }
        }

        private void updateLog(String logMessage) {
//            String state = progress[0];
//            CharSequence log = mStateLog.getText();
//            if (log.length() > 1024*1024*512) {
//                log = log.subSequence(0, log.length() - state.length());
//            }
//            log = state + log;
//            mStateLog.setText(log);
        }

        private void updateCoffeeMachineState(CoffeeMachineState coffeeMachineState) {
            if (coffeeMachineState.isBrewing) {
                mCoffeeMachineImage.setImageResource(R.mipmap.coffee_machine_brewing);
                mCoffeeMachineState.setText("Brewing coffee...");
            } else if (coffeeMachineState.isCoffeeReady) {
                mCoffeeMachineImage.setImageResource(R.mipmap.coffee_machine_ready);
                mCoffeeMachineState.setText("Coffee is ready");
            } else {
                mCoffeeMachineImage.setImageResource(R.mipmap.coffee_machine_idle);
                mCoffeeMachineState.setText("Coffee machine is idle");
            }
            mCoffeeMachineProgressBar.setProgress(coffeeMachineState.brewingProgress);
        }

        private void updateEngineerState(EngineerState engineerState) {
            EngineerListAdapter adapter = (EngineerListAdapter)mEngineersListView.getAdapter();
            EngineerListAdapter queueAdapter = (EngineerListAdapter)mCoffeeQueueListView.getAdapter();

            EngineerState oldState = adapter.getItem(engineerState.id);
            boolean isGoingForCoffee = oldState.isWorking && !engineerState.isWorking;
            boolean isGoingToWork = !oldState.isWorking && engineerState.isWorking;
            if (isGoingForCoffee) {
                queueAdapter.addToQueue(engineerState);
            } else if (isGoingToWork) {
                queueAdapter.removeFromQueue(oldState);
            } else if (!oldState.isWorking) {
                queueAdapter.updateState(engineerState);
            }

            // Do this last!
            adapter.updateState(engineerState);
        }

    }

}
