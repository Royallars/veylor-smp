package com.veylor.smp.warp;

import com.veylor.smp.data.DataStore;
import com.veylor.smp.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.Set;

public class WarpManager {
    private final DataStore dataStore;

    public WarpManager(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Location getWarp(String name) {
        return LocationUtil.loadLocation(dataStore.getConfig().getConfigurationSection("warps." + name.toLowerCase()));
    }

    public void setWarp(String name, Location location) {
        ConfigurationSection section = dataStore.getConfig().createSection("warps." + name.toLowerCase());
        LocationUtil.saveLocation(section, location);
        dataStore.save();
    }

    public boolean deleteWarp(String name) {
        String path = "warps." + name.toLowerCase();
        if (!dataStore.getConfig().contains(path)) {
            return false;
        }
        dataStore.getConfig().set(path, null);
        dataStore.save();
        return true;
    }

    public Set<String> getWarpNames() {
        ConfigurationSection section = dataStore.getConfig().getConfigurationSection("warps");
        if (section == null) {
            return Collections.emptySet();
        }
        return section.getKeys(false);
    }
}
