package com.feheren_fekete.espresso;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClickedStartSimulation(View view) {
        int engineerCount = Integer.parseInt(((EditText)findViewById(R.id.edit_text__engineer_count)).getText().toString());
        int busyProbability = Integer.parseInt(((EditText)findViewById(R.id.edit_text__busy_probability)).getText().toString());
        int busyCheckSeconds = Integer.parseInt(((EditText)findViewById(R.id.edit_text__busy_check_seconds)).getText().toString());
        int busySeconds = Integer.parseInt(((EditText)findViewById(R.id.edit_text__busy_seconds)).getText().toString());
        int secondsUntilNeedCoffee = Integer.parseInt(((EditText)findViewById(R.id.edit_text__seconds_until_need_coffee)).getText().toString());
        int secondsUntilCoffeeReady = Integer.parseInt(((EditText)findViewById(R.id.edit_text__seconds_until_coffee_ready)).getText().toString());

        Intent simulationActivityIntent = new Intent(this, SimulationActivity.class);
        simulationActivityIntent.putExtra(Common.SIMULATION_REAL_SECONDS_PER_STEP, (float)0.01);
        simulationActivityIntent.putExtra(Common.SIMULATION_ENGINEER_SECONDS_PER_STEP, 1);
        simulationActivityIntent.putExtra(Common.SIMULATION_ENGINEER_COUNT, engineerCount);
        simulationActivityIntent.putExtra(Common.SIMULATION_BUSY_PROBABILITY, busyProbability);
        simulationActivityIntent.putExtra(Common.SIMULATION_BUSY_CHECK_SECONDS, busyCheckSeconds);
        simulationActivityIntent.putExtra(Common.SIMULATION_BUSY_SECONDS, busySeconds);
        simulationActivityIntent.putExtra(Common.SIMULATION_SECONDS_UNTIL_NEED_COFFEE, secondsUntilNeedCoffee);
        simulationActivityIntent.putExtra(Common.SIMULATION_SECONDS_UNTIL_COFFEE_READY, secondsUntilCoffeeReady);
        simulationActivityIntent.putExtra(Common.SIMULATION_MAX_QUEUE_LENGTH_WHEN_BUSY, 30);
        startActivity(simulationActivityIntent);
    }
}
