package com.veylor.smp.economy;

import com.veylor.smp.data.DataStore;

import java.util.UUID;

public class BountyManager {
    private final DataStore dataStore;

    public BountyManager(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public int getBounty(UUID target) {
        return dataStore.getConfig().getInt("bounties." + target, 0);
    }

    public void addBounty(UUID target, int amount) {
        int current = getBounty(target);
        dataStore.getConfig().set("bounties." + target, current + amount);
        dataStore.save();
    }

    public void clearBounty(UUID target) {
        dataStore.getConfig().set("bounties." + target, null);
        dataStore.save();
    }
}
