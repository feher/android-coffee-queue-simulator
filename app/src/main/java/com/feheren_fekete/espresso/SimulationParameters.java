package com.feheren_fekete.espresso;

import android.os.Parcel;
import android.os.Parcelable;

public class SimulationParameters implements Parcelable {
    public final double realSecondsPerStep;
    public final double engineerSecondsPerStep;
    public final int engineerCount;
    public final int busyProb;
    public final long busyCheckSteps;
    public final long busySteps;
    public final long stepsUntilNeedCoffee;
    public final long stepsUntilCoffeeReady;
    public final int maxQueueLengthWhenBusy;

    public SimulationParameters(InputValues inputValues) {
        // 1 simulation step _takes_ realSecondsPerStep real seconds.
        // 1 simulation step _means_ engineerSecondsPerStep engineer-seconds.
        // These values are measured in units of engineer-seconds:
        // busyCheckSeconds
        // busySeconds
        // secondsUntilNeedCoffee
        // secondsUntilCoffeeReady

        // Make sure that the longest period will take max 5 real seconds.
        realSecondsPerStep = 0.1; // Hard-coded for now.
        int longestPeriod = Math.max(inputValues.busySeconds, inputValues.secondsUntilNeedCoffee);
        longestPeriod = Math.max(longestPeriod, inputValues.secondsUntilCoffeeReady);
        engineerSecondsPerStep = Math.round(longestPeriod * realSecondsPerStep / 5);

        engineerCount = inputValues.engineerCount;
        busyProb = inputValues.busyProbability;
        busyCheckSteps = Math.round(inputValues.busyCheckSeconds / engineerSecondsPerStep);
        busySteps = Math.round(inputValues.busySeconds / engineerSecondsPerStep);
        stepsUntilNeedCoffee = Math.round(inputValues.secondsUntilNeedCoffee / engineerSecondsPerStep);
        stepsUntilCoffeeReady = Math.round(inputValues.secondsUntilCoffeeReady / engineerSecondsPerStep);
        maxQueueLengthWhenBusy = 30; // Hard-coded for now.
    }

    private SimulationParameters(Parcel in) {
        // NOTE: The order must match writeToParcel()!
        realSecondsPerStep = in.readDouble();
        engineerSecondsPerStep = in.readDouble();
        engineerCount = in.readInt();
        busyProb = in.readInt();
        busyCheckSteps = in.readLong();
        busySteps = in.readLong();
        stepsUntilNeedCoffee = in.readLong();
        stepsUntilCoffeeReady = in.readLong();
        maxQueueLengthWhenBusy = in.readInt();
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        // NOTE: The order must match SimulationParameters(Parcel in)!
        out.writeDouble(realSecondsPerStep);
        out.writeDouble(engineerSecondsPerStep);
        out.writeInt(engineerCount);
        out.writeInt(busyProb);
        out.writeLong(busyCheckSteps);
        out.writeLong(busySteps);
        out.writeLong(stepsUntilNeedCoffee);
        out.writeLong(stepsUntilCoffeeReady);
        out.writeInt(maxQueueLengthWhenBusy);
    }
    
    public static final Parcelable.Creator<SimulationParameters> CREATOR =
            new Parcelable.Creator<SimulationParameters>() {
                public SimulationParameters createFromParcel(Parcel in) {
                    return new SimulationParameters(in);
                }
                public SimulationParameters[] newArray(int size) {
                    return new SimulationParameters[size];
                }
            };
}
