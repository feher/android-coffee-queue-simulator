package com.feheren_fekete.espresso;

import java.util.ArrayList;
import java.util.List;

public class CoffeeQueueState {
    private final int[] mEngineerIds;

    public CoffeeQueueState(int[] engineerIds) {
        mEngineerIds = engineerIds;
    }

    public int[] getEngineerIds() {
        return mEngineerIds;
    }

}
