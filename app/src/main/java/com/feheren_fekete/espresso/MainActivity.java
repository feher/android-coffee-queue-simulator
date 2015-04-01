package com.feheren_fekete.espresso;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {

    private EditText mEngineerCountText;
    private EditText mBusyProbabilityText;
    private EditText mBusyCheckSecondsText;
    private EditText mBusySecondsText;
    private EditText mSecondsUntilNeedCoffeeText;
    private EditText mSecondsUntilCoffeeReadyText;
    
    private static class InputValues {
        public boolean isValid;
        public int engineerCount;
        public int busyProbability;
        public int busyCheckSeconds;
        public int busySeconds;
        public int secondsUntilNeedCoffee;
        public int secondsUntilCoffeeReady;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEngineerCountText = (EditText)findViewById(R.id.edit_text__engineer_count);
        mBusyProbabilityText = (EditText)findViewById(R.id.edit_text__busy_probability);
        mBusyCheckSecondsText = (EditText)findViewById(R.id.edit_text__busy_check_seconds);
        mBusySecondsText = (EditText)findViewById(R.id.edit_text__busy_seconds);
        mSecondsUntilNeedCoffeeText = (EditText)findViewById(R.id.edit_text__seconds_until_need_coffee);
        mSecondsUntilCoffeeReadyText = (EditText)findViewById(R.id.edit_text__seconds_until_coffee_ready);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_help) {
            Intent helpActivityIntent = new Intent(this, HelpActivity.class);
            startActivity(helpActivityIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClickedStartSimulation(View view) {
        try {
            InputValues inputValues = getInputValues();
            startSimulation(inputValues);
        } catch (NumberFormatException e) {
            // Do nothing.
        }
    }
    
    private void startSimulation(InputValues inputValues) {
        // 1 simulation step _takes_ realSecondsPerStep real seconds.
        // 1 simulation step _means_ engineerSecondsPerStep engineer-seconds.
        // These values are measured in units of engineer-seconds:
        // busyCheckSeconds
        // busySeconds
        // secondsUntilNeedCoffee
        // secondsUntilCoffeeReady

        // Make sure that the longest period will take max 5 real seconds.
        double realSecondsPerStep = 0.1;
        int longestPeriod = Math.max(inputValues.busySeconds, inputValues.secondsUntilNeedCoffee);
        longestPeriod = Math.max(longestPeriod, inputValues.secondsUntilCoffeeReady);
        double engineerSecondsPerStep = Math.round(longestPeriod * realSecondsPerStep / 5);

        Intent simulationActivityIntent = new Intent(this, SimulationActivity.class);
        simulationActivityIntent.putExtra(Common.SIMULATION_REAL_SECONDS_PER_STEP, inputValues.realSecondsPerStep);
        simulationActivityIntent.putExtra(Common.SIMULATION_ENGINEER_SECONDS_PER_STEP, inputValues.engineerSecondsPerStep);
        simulationActivityIntent.putExtra(Common.SIMULATION_ENGINEER_COUNT, inputValues.engineerCount);
        simulationActivityIntent.putExtra(Common.SIMULATION_BUSY_PROBABILITY, inputValues.busyProbability);
        simulationActivityIntent.putExtra(Common.SIMULATION_BUSY_CHECK_SECONDS, inputValues.busyCheckSeconds);
        simulationActivityIntent.putExtra(Common.SIMULATION_BUSY_SECONDS, inputValues.busySeconds);
        simulationActivityIntent.putExtra(Common.SIMULATION_SECONDS_UNTIL_NEED_COFFEE, inputValues.secondsUntilNeedCoffee);
        simulationActivityIntent.putExtra(Common.SIMULATION_SECONDS_UNTIL_COFFEE_READY, inputValues.secondsUntilCoffeeReady);
        simulationActivityIntent.putExtra(Common.SIMULATION_MAX_QUEUE_LENGTH_WHEN_BUSY, 30);
        startActivity(simulationActivityIntent);
    }
    
    private InputValues getInputValues() {
        int maxSeconds = 100000;
        InputValues inputValues = new InputValues();
        
        inputValues.engineerCount = getInputValue(mEngineerCountText, 1, 50, "Must be between 1 and 50");
        inputValues.busyProbability = getInputValue(mBusyProbabilityText, 0, 100, "Must be between 0 and 100");
        inputValues.busyCheckSeconds = getInputValue(mBusyCheckSecondsText, 1, maxSeconds, "Must be between 1 and " + maxSeconds);
        inputValues.busySeconds = getInputValue(mBusySecondsText, 1, maxSeconds, "Must be between 1 and " + maxSeconds);
        inputValues.secondsUntilNeedCoffee = getInputValue(mSecondsUntilNeedCoffeeText,  1, maxSeconds, "Must be between 1 and " + maxSeconds);
        inputValues.secondsUntilCoffeeReady = getInputValue(mSecondsUntilCoffeeReadyText,  1, maxSeconds, "Must be between 1 and " + maxSeconds);
        
        return inputValues;
    }
    
    private int getInputValue(TextView textView,
                              int minValue, int maxValue,
                              int String errorMessage) {
        int value = 0;
        try {
            value = Integer.parseInt(textView.getText().toString());
            if (!(minValue <= value && value <= maxValue)) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            textView.setError(errorMessage);
            throw e;
        }
        return value;
    }
}
