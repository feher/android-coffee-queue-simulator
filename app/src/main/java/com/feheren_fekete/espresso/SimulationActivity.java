package com.feheren_fekete.espresso;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


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
    private TextView mEngineerListTitle;
    private TextView mCoffeeQueueTitle;
    private Drawable mCoffeeMachineBrewingDrawable;
    private Drawable mCoffeeMachineReadyDrawable;
    private Drawable mCoffeeMachineIdleDrawable;
    private SimulationTask mSimulationTask;
    private EngineerListAdapter mEngineerListAdapter;
    private EngineerListAdapter mCoffeeQueueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Common.LOG_TAG, "xxxxxxxxxxxxxxxxxxxxx Creating SimulationActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        mSimulationStateText = (TextView) findViewById(R.id.textView_simulationState);
        mCoffeeMachineImage = (ImageView) findViewById(R.id.imageView_coffeeMachine);
        mCoffeeMachineStateText = (TextView) findViewById(R.id.textView_coffeeMachine);
        mCoffeeMachineProgressBar = (ProgressBar) findViewById(R.id.progressBar_coffeeMachine);

        Resources resources = getResources();
        mCoffeeMachineBrewingDrawable = resources.getDrawable(R.mipmap.coffee_machine_brewing);
        mCoffeeMachineReadyDrawable = resources.getDrawable(R.mipmap.coffee_machine_ready);
        mCoffeeMachineIdleDrawable = resources.getDrawable(R.mipmap.coffee_machine_idle);

        mEngineersListView = (ListView) findViewById(R.id.listView_engineers);
        mCoffeeQueueListView = (ListView) findViewById(R.id.listView_coffeeQueue);

        mEngineerListTitle = (TextView) findViewById(R.id.textView_engineerListTitle);
        mCoffeeQueueTitle = (TextView) findViewById(R.id.textView_coffeeQueueTitle);

        mPauseButton = (Button) findViewById(R.id.button_pause);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPauseButtonClick();
            }
        });

        SimulationParameters parameters = extras.getParcelable(Common.SIMULATION_PARAMETERS);
        createEngineerList(parameters.engineerCount);
        createCoffeeQueueList();

        mSimulationTask = new SimulationTask(this);
        mSimulationTask.execute(parameters);

        mEngineerListTitle.setText("Engineers (" + parameters.engineerCount + ")");
    }

    @Override
    protected void onPause() {
        if (mSimulationTask != null) {
            if (!mSimulationTask.isTaskPaused()) {
                pauseSimulation();
            }
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSimulationTask != null) {
            if (mSimulationTask.isTaskPaused()) {
                resumeSimulation();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(Common.LOG_TAG, "xxxxxxxxxxxxxxxxxxxxx Destroying SimulationActivity");
        if (mSimulationTask != null) {
            mSimulationTask.cancel(true);
        }
        super.onDestroy();
    }

    private void createEngineerList(int engineerCount) {
        ArrayList<EngineerState> engineerList = new ArrayList<EngineerState>();
        for (int i = 0; i < engineerCount; ++i) {
            engineerList.add(new EngineerState(i, false, true, 0, 0));
        }
        mEngineerListAdapter = new EngineerListAdapter(this, engineerList);
        mEngineersListView.setAdapter(mEngineerListAdapter);
    }

    private void createCoffeeQueueList() {
        mCoffeeQueueAdapter = new EngineerListAdapter(this, new ArrayList<EngineerState>());
        mCoffeeQueueListView.setAdapter(mCoffeeQueueAdapter);
    }

    @Override
    public void onSimulationStateChange(List<Object> states) {
        for (Object state : states) {
            handleStateChange(state);
        }
        mEngineerListAdapter.notifyUpdates();
        mCoffeeQueueAdapter.notifyUpdates();
    }

    private void handleStateChange(Object state) {
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
        mSimulationStateText.setText("RUNNING");
    }

    private void updateCoffeeMachineState(CoffeeMachineState coffeeMachineState) {
        if (coffeeMachineState.isBrewing()) {
            mCoffeeMachineImage.setImageDrawable(mCoffeeMachineBrewingDrawable);
            mCoffeeMachineStateText.setText("Brewing");
        } else if (coffeeMachineState.isCoffeeReady()) {
            mCoffeeMachineImage.setImageDrawable(mCoffeeMachineReadyDrawable);
            mCoffeeMachineStateText.setText("Ready");
        } else {
            mCoffeeMachineImage.setImageDrawable(mCoffeeMachineIdleDrawable);
            mCoffeeMachineStateText.setText("Idle");
        }
        mCoffeeMachineProgressBar.setProgress(coffeeMachineState.getBrewingProgress());
    }

    private void updateEngineerState(EngineerState engineerState) {
        mEngineerListAdapter.updateEngineerState(engineerState);
        mCoffeeQueueAdapter.updateEngineerState(engineerState);
    }

    private void updateCoffeeQueueState(CoffeeQueueState coffeeQueueState) {
        ArrayList<EngineerState> queuingEngineerStates = new ArrayList<EngineerState>();
        for (Integer engineerId : coffeeQueueState.getEngineerIds()) {
            queuingEngineerStates.add(mEngineerListAdapter.getEngineerState(engineerId));
        }

        mCoffeeQueueAdapter.updateEngineerStates(queuingEngineerStates);
        mCoffeeQueueTitle.setText("Coffee Queue (" + queuingEngineerStates.size() + ")");
    }
}
