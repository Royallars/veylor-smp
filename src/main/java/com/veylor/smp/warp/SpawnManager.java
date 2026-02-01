package com.veylor.smp.warp;

import com.veylor.smp.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class SpawnManager {
    private final Plugin plugin;

    public SpawnManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public Location getSpawn() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("spawn");
        return LocationUtil.loadLocation(section);
    }

    public void setSpawn(Location location) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("spawn");
        if (section == null) {
            section = plugin.getConfig().createSection("spawn");
        }
        LocationUtil.saveLocation(section, location);
        plugin.saveConfig();
    }
}
