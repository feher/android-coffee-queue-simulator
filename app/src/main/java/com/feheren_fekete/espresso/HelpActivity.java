package com.feheren_fekete.espresso;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;


public class HelpActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        TextView header = (TextView)findViewById(R.id.text_view__help_header);
        TextView footer = (TextView)findViewById(R.id.text_view__help_footer);
        header.setText(
                "This is a simulation of an office where engineers work.\n\n" +
                "Every engineer needs some coffee from time-to-time to keep up the good work. " +
                "There is only one coffee machine so, the engineers must stand in a queue. " +
                "Engineers are sometimes busy. Busy engineers can jump the queue but they cannot take over other busy guys.\n" +
                "\nThe state of the engineers and the coffee machine are signified using icons.\n");

        footer.setText(
                "\nThe \"seconds\" that are input to the simulation are \"engineer seconds\", not real seconds. " +
                "The simulation is done in discrete steps. " +
                "1 simulation step _takes_ a pre-defined number of real seconds. " +
                "1 simulation step _means_ a calculated number of \"engineer seconds\". " +
                "The simulation is done so that the progress bars in the UI do not take more than 5 seconds.\n" +
                "\n" +
                "Description of inputs:\n\n" +
                "- Number of engineers: The number of engineers in the office.\n" +
                "\n" +
                "- Probability that engineer becomes busy: Percentage of the probability that engineer becomes busy.\n" +
                "\n" +
                "- Re-evaluate busy status every N seconds: An engineer can become busy only at every N seconds.\n" +
                "\n" +
                "- Engineer stays busy for N seconds: An engineer stays busy for the given seconds.\n" +
                "\n" +
                "- Engineer needs coffee every N seconds: An engineer goes for coffee every given N seconds.\n" +
                "\n" +
                "- Coffee machine makes the coffee in N seconds: It takes N seconds for the coffe machine to prepare one cup of coffee.\n" +
                "\n");
    }
}
