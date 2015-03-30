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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);

        mCoffeeMachineImage = (ImageView) findViewById(R.id.image_view__coffee_machine_state);
        mCoffeeMachineState = (TextView) findViewById(R.id.text_view__coffee_machine);
        mCoffeeMachineProgressBar = (ProgressBar) findViewById(R.id.progress_bar__coffee_machine);

        mEngineersListView = (ListView) findViewById(R.id.list_view__engineers);

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

            ArrayList<String> engineerList = new ArrayList<String>();
            String engineerStateText = "N0|W0";
            for (int i = 0; i < engineerCount; ++i) {
                engineerList.add(engineerStateText);
            }
            EngineerListAdapter engineerListAdapter = new EngineerListAdapter(this, engineerList);
            mEngineersListView.setAdapter(engineerListAdapter);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simulation, menu);
        return true;
    }

    private class SimulationAsyncTask
            extends AsyncTask<SimulationParameters, String, Void>
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
        public void reportStateChange(String message) {
            publishProgress(MESSAGE_STATE_CHANGE + message);
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            String message = progress[0];
            if (message.startsWith(MESSAGE_LOG)) {
                updateLog(message.substring(MESSAGE_LOG.length()));
            } else if (message.startsWith(MESSAGE_STATE_CHANGE)) {
                updateState(message.substring(MESSAGE_STATE_CHANGE.length()));
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

        private void updateState(String stateChangeMessage) {
            if (stateChangeMessage.startsWith(Common.STATE_CHANGE_COFFEE_MACHINE)) {
                updateCoffeeMachineState(stateChangeMessage.substring(Common.STATE_CHANGE_COFFEE_MACHINE.length()));
            } else if (stateChangeMessage.startsWith(Common.STATE_CHANGE_ENGINEER)) {
                updateEngineerState(stateChangeMessage.substring(Common.STATE_CHANGE_ENGINEER.length()));
            }
        }

        private void updateCoffeeMachineState(String coffeeMachineState) {
            char state = coffeeMachineState.charAt(0);
            int progress = Integer.parseInt(coffeeMachineState.substring(1));
            mCoffeeMachineProgressBar.setProgress(progress);
            switch (state) {
                case 'I':
                    mCoffeeMachineImage.setImageResource(R.mipmap.coffee_machine_idle);
                    mCoffeeMachineState.setText("Coffee machine is idle");
                    break;
                case 'B':
                    mCoffeeMachineImage.setImageResource(R.mipmap.coffee_machine_brewing);
                    mCoffeeMachineState.setText("Brewing coffee...");
                    break;
                case 'R':
                    mCoffeeMachineImage.setImageResource(R.mipmap.coffee_machine_ready);
                    mCoffeeMachineState.setText("Coffee is ready");
                    break;
                default:
                    assert false;
                    break;
            }
        }

        private void updateEngineerState(String engineerIdAndState) {
            EngineerListAdapter adapter = (EngineerListAdapter)mEngineersListView.getAdapter();
            int separatorPos = engineerIdAndState.indexOf('|');
            String engineerIdText = engineerIdAndState.substring(0, separatorPos);
            String engineerStateText = engineerIdAndState.substring(separatorPos + 1);
            int engineerId = Integer.parseInt(engineerIdText);
            adapter.updateState(engineerId, engineerStateText);
        }

    }

}
