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
        // 1 simulation step _takes_ realSecondsPerStep real seconds.
        // 1 simulation step _means_ engineerSecondsPerStep engineer-seconds.
        // These values are measured in units of engineer-seconds:
        // busyCheckSeconds
        // busySeconds
        // secondsUntilNeedCoffee
        // secondsUntilCoffeeReady
        int engineerCount = Integer.parseInt(((EditText)findViewById(R.id.edit_text__engineer_count)).getText().toString());
        int busyProbability = Integer.parseInt(((EditText)findViewById(R.id.edit_text__busy_probability)).getText().toString());
        int busyCheckSeconds = Integer.parseInt(((EditText)findViewById(R.id.edit_text__busy_check_seconds)).getText().toString());
        int busySeconds = Integer.parseInt(((EditText)findViewById(R.id.edit_text__busy_seconds)).getText().toString());
        int secondsUntilNeedCoffee = Integer.parseInt(((EditText)findViewById(R.id.edit_text__seconds_until_need_coffee)).getText().toString());
        int secondsUntilCoffeeReady = Integer.parseInt(((EditText)findViewById(R.id.edit_text__seconds_until_coffee_ready)).getText().toString());

        // Make sure that the longest period will take max 5 real seconds.
        double realSecondsPerStep = 0.1;
        int longestPeriod = Math.max(busySeconds, secondsUntilNeedCoffee);
        longestPeriod = Math.max(longestPeriod, secondsUntilCoffeeReady);
        double engineerSecondsPerStep = Math.round(longestPeriod * realSecondsPerStep / 5);

        Intent simulationActivityIntent = new Intent(this, SimulationActivity.class);
        simulationActivityIntent.putExtra(Common.SIMULATION_REAL_SECONDS_PER_STEP, realSecondsPerStep);
        simulationActivityIntent.putExtra(Common.SIMULATION_ENGINEER_SECONDS_PER_STEP, engineerSecondsPerStep);
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
