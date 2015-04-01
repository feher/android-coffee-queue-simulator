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
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEngineerCountText = (EditText)findViewById(R.id.editText_engineerCount);
        mBusyProbabilityText = (EditText)findViewById(R.id.editText_busyProbability);
        mBusyCheckSecondsText = (EditText)findViewById(R.id.editText_busyCheckSeconds);
        mBusySecondsText = (EditText)findViewById(R.id.editText_busySeconds);
        mSecondsUntilNeedCoffeeText = (EditText)findViewById(R.id.editText_secondsUntilNeedCoffee);
        mSecondsUntilCoffeeReadyText = (EditText)findViewById(R.id.editText_secondsUntiCoffeeReady);
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
        SimulationParameters simulationParameters = new SimulationParameters(InputValues);
        Intent simulationActivityIntent = new Intent(this, SimulationActivity.class);
        simulationActivityIntent.putExtra(Common.SIMULATION_PARAMETERS, simulationParameters);
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
