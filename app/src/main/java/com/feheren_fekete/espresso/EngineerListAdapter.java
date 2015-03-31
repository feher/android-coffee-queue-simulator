package com.feheren_fekete.espresso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.NoSuchElementException;

public class EngineerListAdapter extends ArrayAdapter<EngineerState> {
    private final Context mContext;
    private ArrayList<EngineerState> mEngineerStates;

    public EngineerListAdapter(Context context, ArrayList<EngineerState> engineerStates) {
        super(context, R.layout.layout_engineer, R.id.text_view__engineer_state, engineerStates);
        mContext = context;
        mEngineerStates = engineerStates;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EngineerState engineerState = mEngineerStates.get(position);

        LayoutInflater inflater =
                (LayoutInflater)mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.layout_engineer, parent, false);

        TextView stateText = (TextView) rowView.findViewById(R.id.text_view__engineer_state);
        ImageView busyImage = (ImageView) rowView.findViewById(R.id.image_view__engineer_busy);
        ImageView workingImage = (ImageView) rowView.findViewById(R.id.image_view__engineer_working);
        ProgressBar busyProgress  = (ProgressBar) rowView.findViewById(R.id.progress_bar__engineer_busy);
        ProgressBar needCoffeeProgress  = (ProgressBar) rowView.findViewById(R.id.progress_bar__engineer_need_cofee);

        stateText.setText(engineerState.toString());
        busyImage.setImageResource(engineerState.isBusy() ? R.mipmap.engineer_busy : R.mipmap.engineer_not_busy);
        workingImage.setImageResource(engineerState.isWorking() ? R.mipmap.engineer_working : R.mipmap.engineer_queuing);
        busyProgress.setProgress(engineerState.getBusyProgress());
        needCoffeeProgress.setProgress(engineerState.getNeedCoffeeProgress());

        return rowView;
    }

    public EngineerState getEngineerState(int engineerId) {
        for (EngineerState state : mEngineerStates) {
            if (state.getId() == engineerId) {
                return state;
            }
        }
        throw new NoSuchElementException("Id " + engineerId + " not found!");
    }

    public void setEngineerStates(ArrayList<EngineerState> engineerStates) {
        mEngineerStates = engineerStates;

        clear();
        for (EngineerState state : engineerStates) {
            add(state);
        }

        notifyDataSetChanged();
    }

    public void updateEngineerState(EngineerState engineerState) {
        for (int i = 0; i < mEngineerStates.size(); ++i) {
            if (mEngineerStates.get(i).getId() == engineerState.getId()) {
                mEngineerStates.set(i, engineerState);
                notifyDataSetChanged();
                return;
            }
        }
    }

}
