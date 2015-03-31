package com.feheren_fekete.espresso;

import java.util.ArrayList;
import java.util.List;

public class CoffeeQueueState {
    private final List<Integer> mEngineerIds;

    public CoffeeQueueState(List<Integer> engineerIds) {
        mEngineerIds = engineerIds;
    }

    public List<Integer> getEngineerIds() {
        return mEngineerIds;
    }

}
