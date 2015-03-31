package com.feheren_fekete.espresso;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class SimulationActivity
        extends ActionBarActivity
        implements SimulationStateChangeHandler {

    private TextView mSimulationStateText;
    private ImageView mCoffeeMachineImage;
    private TextView mCoffeeMachineStateText;
    private ProgressBar mCoffeeMachineProgressBar;
    private ListView mEngineersListView;
    private ListView mCoffeeQueueListView;
    private Button mPauseButton;
    private SimulationTask mSimulationTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);

        mSimulationStateText = (TextView) findViewById(R.id.text_view__simulation_state);
        mCoffeeMachineImage = (ImageView) findViewById(R.id.image_view__coffee_machine);
        mCoffeeMachineStateText = (TextView) findViewById(R.id.text_view__coffee_machine);
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
            mSimulationTask = new SimulationTask(this);
            mSimulationTask.execute(parameters);

            mPauseButton = (Button) findViewById(R.id.button__pause);
            mPauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPauseButtonClick();
                }
            });
        }
    }

    @Override
    protected void onPause() {
        if (!mSimulationTask.isTaskPaused()) {
            pauseSimulation();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSimulationTask.isTaskPaused()) {
            resumeSimulation();
        }
    }

    @Override
    protected void onDestroy() {
        mSimulationTask.cancel(true);
        super.onDestroy();
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

    @Override
    public void onSimulationStateChange(Object state) {
        if (state instanceof CoffeeMachineState) {
            updateCoffeeMachineState((CoffeeMachineState) state);
        } else if (state instanceof EngineerState) {
            updateEngineerState((EngineerState) state);
        } else if (state instanceof CoffeeQueueState) {
            updateCoffeeQueueState((CoffeeQueueState) state);
        } else {
            throw new ClassCastException("Unknown simulation state!");
        }
    }

    private void onPauseButtonClick() {
        if (mSimulationTask.getStatus() == AsyncTask.Status.RUNNING) {
            if (mSimulationTask.isTaskPaused()) {
                resumeSimulation();
            } else {
                pauseSimulation();
            }
        }
    }

    private void pauseSimulation() {
        mSimulationTask.pauseTask();
        mPauseButton.setText("Resume");
        mSimulationStateText.setText("PAUSED");
    }

    private void resumeSimulation() {
        mSimulationTask.resumeTask();
        mPauseButton.setText("Pause");
        mSimulationStateText.setText("Running...");
    }

    private void updateCoffeeMachineState(CoffeeMachineState coffeeMachineState) {
        if (coffeeMachineState.isBrewing()) {
            mCoffeeMachineImage.setImageResource(R.mipmap.coffee_machine_brewing);
            mCoffeeMachineStateText.setText("Brewing coffee...");
        } else if (coffeeMachineState.isCoffeeReady()) {
            mCoffeeMachineImage.setImageResource(R.mipmap.coffee_machine_ready);
            mCoffeeMachineStateText.setText("Coffee is ready");
        } else {
            mCoffeeMachineImage.setImageResource(R.mipmap.coffee_machine_idle);
            mCoffeeMachineStateText.setText("Coffee machine is idle");
        }
        mCoffeeMachineProgressBar.setProgress(coffeeMachineState.getBrewingProgress());
    }

    private void updateEngineerState(EngineerState engineerState) {
        EngineerListAdapter engineersAdapter = (EngineerListAdapter)mEngineersListView.getAdapter();
        engineersAdapter.updateEngineerState(engineerState);

        EngineerListAdapter queueAdapter = (EngineerListAdapter)mCoffeeQueueListView.getAdapter();
        queueAdapter.updateEngineerState(engineerState);
    }

    private void updateCoffeeQueueState(CoffeeQueueState coffeeQueueState) {
        EngineerListAdapter engineersAdapter = (EngineerListAdapter)mEngineersListView.getAdapter();
        EngineerListAdapter queueAdapter = (EngineerListAdapter)mCoffeeQueueListView.getAdapter();

        ArrayList<EngineerState> queuingEngineerStates = new ArrayList<EngineerState>();
        for (Integer engineerId : coffeeQueueState.getEngineerIds()) {
            queuingEngineerStates.add(engineersAdapter.getEngineerState(engineerId));
        }

        queueAdapter.setEngineerStates(queuingEngineerStates);
    }
}
