package com.veylor.smp.warp;

import com.veylor.smp.data.DataStore;
import com.veylor.smp.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public class HomeManager {
    private final DataStore dataStore;

    public HomeManager(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Location getHome(UUID uuid) {
        return LocationUtil.loadLocation(dataStore.getConfig().getConfigurationSection("homes." + uuid));
    }

    public void setHome(UUID uuid, Location location) {
        ConfigurationSection section = dataStore.getConfig().createSection("homes." + uuid);
        LocationUtil.saveLocation(section, location);
        dataStore.save();
    }

    public boolean deleteHome(UUID uuid) {
        if (!dataStore.getConfig().contains("homes." + uuid)) {
            return false;
        }
        dataStore.getConfig().set("homes." + uuid, null);
        dataStore.save();
        return true;
    }
}
