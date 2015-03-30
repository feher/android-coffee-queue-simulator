package com.feheren_fekete.espresso;

public class EngineerState {
    public final int id;
    public final boolean isBusy;
    public final boolean isWorking;
    public final int busyProgress;
    public final int needCoffeeProgress;

    public EngineerState(int id, boolean isBusy, boolean isWorking, int busyProgress, int needCoffeeProgress) {
        this.id = id;
        this.isBusy = isBusy;
        this.isWorking = isWorking;
        this.busyProgress = busyProgress;
        this.needCoffeeProgress = needCoffeeProgress;
    }

    @Override
    public String toString() {
        String place = isWorking ? "Working" : "Queuing for coffee";
        String business = isBusy ? "Busy" : "Not busy";

        return "Id: " + id + "\n" + place + "\n" + business;
    }
}
