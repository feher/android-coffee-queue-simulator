package com.feheren_fekete.espresso;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
    private boolean mIsDataSetChanged;
    private Drawable mEngineerBusyDrawable;
    private Drawable mEngineerNotBusyDrawable;
    private Drawable mEngineerWorkingDrawable;
    private Drawable mEngineerQueuingDrawable;

    private static class ViewHolder {
        public TextView stateText;
        public TextView workText;
        public TextView busyText;
        public ImageView busyImage;
        public ImageView workingImage;
        public ProgressBar busyProgress;
        public ProgressBar needCoffeeProgress;
    }

    public EngineerListAdapter(Context context, ArrayList<EngineerState> engineerStates) {
        super(context, R.layout.layout_engineer, R.id.text_view__engineer_state, engineerStates);
        mContext = context;
        mEngineerStates = engineerStates;
        mIsDataSetChanged = false;

        Resources resources = context.getResources();
        mEngineerBusyDrawable = resources.getDrawable(R.mipmap.engineer_busy);
        mEngineerNotBusyDrawable = resources.getDrawable(R.mipmap.engineer_not_busy);
        mEngineerWorkingDrawable = resources.getDrawable(R.mipmap.engineer_working);
        mEngineerQueuingDrawable = resources.getDrawable(R.mipmap.engineer_queuing);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater =
                    (LayoutInflater)mContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(R.layout.layout_engineer, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.stateText = (TextView) rowView.findViewById(R.id.text_view__engineer_state);
            viewHolder.workText = (TextView) rowView.findViewById(R.id.text_view__engineer_working);
            viewHolder.busyText = (TextView) rowView.findViewById(R.id.text_view__engineer_busy);
            viewHolder.busyImage = (ImageView) rowView.findViewById(R.id.image_view__engineer_busy);
            viewHolder.workingImage = (ImageView) rowView.findViewById(R.id.image_view__engineer_working);
            viewHolder.busyProgress  = (ProgressBar) rowView.findViewById(R.id.progress_bar__engineer_busy);
            viewHolder.needCoffeeProgress  = (ProgressBar) rowView.findViewById(R.id.progress_bar__engineer_need_cofee);
            rowView.setTag(viewHolder);
        }

        EngineerState engineerState = mEngineerStates.get(position);
        ViewHolder viewHolder = (ViewHolder) rowView.getTag();

        viewHolder.stateText.setText("Id: " + engineerState.getId());
        viewHolder.workText.setText(engineerState.isWorking() ? "Working" : "Queuing");
        viewHolder.busyText.setText(engineerState.isBusy() ? "Busy" : "Not busy");
        viewHolder.busyImage.setImageDrawable(
                engineerState.isBusy() ? mEngineerBusyDrawable : mEngineerNotBusyDrawable);
        viewHolder.workingImage.setImageDrawable(
                engineerState.isWorking() ? mEngineerWorkingDrawable : mEngineerQueuingDrawable);
        viewHolder.busyProgress.setProgress(engineerState.getBusyProgress());
        viewHolder.needCoffeeProgress.setProgress(engineerState.getNeedCoffeeProgress());

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

    public void updateEngineerStates(ArrayList<EngineerState> engineerStates) {
        mEngineerStates = engineerStates;

        clear();
        addAll(mEngineerStates);

        mIsDataSetChanged = true;
    }

    public void updateEngineerState(EngineerState engineerState) {
        for (int i = 0; i < mEngineerStates.size(); ++i) {
            if (mEngineerStates.get(i).getId() == engineerState.getId()) {
                mEngineerStates.set(i, engineerState);
                mIsDataSetChanged = true;
                return;
            }
        }
    }

    public void notifyUpdates() {
        if (mIsDataSetChanged) {
            mIsDataSetChanged = false;
            notifyDataSetChanged();
        }
    }

}
