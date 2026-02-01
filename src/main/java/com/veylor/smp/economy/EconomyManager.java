package com.veylor.smp.economy;

import com.veylor.smp.data.DataStore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class EconomyManager {
    private final Plugin plugin;
    private final DataStore dataStore;

    public EconomyManager(Plugin plugin, DataStore dataStore) {
        this.plugin = plugin;
        this.dataStore = dataStore;
    }

    public int getBalance(UUID uuid) {
        return dataStore.getConfig().getInt("balances." + uuid, plugin.getConfig().getInt("starting-balance", 0));
    }

    public void setBalance(UUID uuid, int amount) {
        dataStore.getConfig().set("balances." + uuid, amount);
        dataStore.save();
    }

    public void addBalance(UUID uuid, int amount) {
        setBalance(uuid, getBalance(uuid) + amount);
    }

    public boolean withdraw(UUID uuid, int amount) {
        int current = getBalance(uuid);
        if (current < amount) {
            return false;
        }
        setBalance(uuid, current - amount);
        return true;
    }

    public void ensureAccount(UUID uuid) {
        FileConfiguration config = dataStore.getConfig();
        String path = "balances." + uuid;
        if (!config.contains(path)) {
            config.set(path, plugin.getConfig().getInt("starting-balance", 0));
            dataStore.save();
        }
    }
}
