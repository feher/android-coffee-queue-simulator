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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClickedStartSimulation(View view) {
        EditText numberOfEngineersView = (EditText)findViewById(R.id.edit_text_number_of_engineers);
        int numberOfEngineers = Integer.parseInt(numberOfEngineersView.getText().toString());

        EditText superBusyProbabilityView = (EditText)findViewById(R.id.edit_text_super_busy_probability);
        int superBusyProbability = Integer.parseInt(superBusyProbabilityView.getText().toString());

        EditText superBusyTimeView = (EditText)findViewById(R.id.edit_text_super_busy_time);
        int superBusyTime = Integer.parseInt(superBusyTimeView.getText().toString());

        Intent simulationActivityIntent = new Intent(this, SimulationActivity.class);
        simulationActivityIntent.putExtra(Common.SIMULATION_REAL_SECONDS_PER_STEP, (float)0.01);
        simulationActivityIntent.putExtra(Common.SIMULATION_ENGINEER_SECONDS_PER_STEP, 1);
        simulationActivityIntent.putExtra(Common.SIMULATION_ENGINEER_COUNT, 30);
        simulationActivityIntent.putExtra(Common.SIMULATION_BUSY_PROBABILITY, 30);
        simulationActivityIntent.putExtra(Common.SIMULATION_BUSY_CHECK_SECONDS, 60 * 10);
        simulationActivityIntent.putExtra(Common.SIMULATION_BUSY_SECONDS, 60 * 10);
        simulationActivityIntent.putExtra(Common.SIMULATION_SECONDS_UNTIL_NEED_COFFEE, 60 * 60);
        simulationActivityIntent.putExtra(Common.SIMULATION_SECONDS_UNTIL_COFFEE_READY, 30);
        simulationActivityIntent.putExtra(Common.SIMULATION_MAX_QUEUE_LENGTH_WHEN_BUSY, 30);
        startActivity(simulationActivityIntent);
    }
}
