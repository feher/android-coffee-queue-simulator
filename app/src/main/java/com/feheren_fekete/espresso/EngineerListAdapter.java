package com.feheren_fekete.espresso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class EngineerListAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final ArrayList<EngineerState> mEngineerStates;

    private static class EngineerState {
        public boolean isBusy;
        public boolean isWorking;
        public int busyProgress;
        public int needCoffeeProgress;
    }

    public EngineerListAdapter(Context context, ArrayList<String> engineerStates) {
        super(context, R.layout.layout_engineer, R.id.text_view__engineer_dummy, engineerStates);
        mContext = context;
        mEngineerStates = parseEngineerStates(engineerStates);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EngineerState engineerState = mEngineerStates.get(position);

        LayoutInflater inflater =
                (LayoutInflater)mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.layout_engineer, parent, false);

        ImageView busyImage = (ImageView) rowView.findViewById(R.id.image_view__engineer_busy);
        ImageView workingImage = (ImageView) rowView.findViewById(R.id.image_view__engineer_working);
        ProgressBar busyProgress  = (ProgressBar) rowView.findViewById(R.id.progress_bar__engineer_busy);
        ProgressBar needCoffeeProgress  = (ProgressBar) rowView.findViewById(R.id.progress_bar__engineer_need_cofee);

        busyImage.setImageResource(engineerState.isBusy ? R.mipmap.engineer_busy : R.mipmap.engineer_not_busy);
        workingImage.setImageResource(engineerState.isWorking ? R.mipmap.engineer_working : R.mipmap.engineer_queuing);
        busyProgress.setProgress(engineerState.busyProgress);
        needCoffeeProgress.setProgress(engineerState.needCoffeeProgress);

        return rowView;
    }

    public ArrayList<EngineerState> parseEngineerStates(ArrayList<String> engineerStateTexts) {
        ArrayList<EngineerState> engineerStates = new ArrayList<EngineerState>();
        for (String engineerStateText : engineerStateTexts) {
            engineerStates.add(parseEngineerState(engineerStateText));
        }
        return engineerStates;
    }

    public EngineerState parseEngineerState(String engineerStateText) {
        int separatorPos = engineerStateText.indexOf('|');

        String busyStateText = engineerStateText.substring(0, separatorPos);
        String coffeeStateText = engineerStateText.substring(separatorPos + 1);

        char busyState = busyStateText.charAt(0);
        int busyProgress = Integer.parseInt(busyStateText.substring(1));

        char needCoffeeState = coffeeStateText.charAt(0);
        int needCoffeeProgress = Integer.parseInt(coffeeStateText.substring(1));

        EngineerState engineerState = new EngineerState();

        switch (busyState) {
            case 'B':
                engineerState.isBusy = true;
                break;
            case 'N':
                engineerState.isBusy = false;
                break;
            default:
                assert false;
                break;
        }

        switch (needCoffeeState) {
            case 'W':
                engineerState.isWorking = true;
                break;
            case 'C':
                engineerState.isWorking = false;
                break;
            default:
                assert false;
                break;
        }

        engineerState.busyProgress = busyProgress;
        engineerState.needCoffeeProgress = needCoffeeProgress;

        return engineerState;
    }

    public void updateState(int engineerId, String engineerStateText) {
        EngineerState engineerState = parseEngineerState(engineerStateText);
        mEngineerStates.set(engineerId, engineerState);
        notifyDataSetChanged();
    }

}
