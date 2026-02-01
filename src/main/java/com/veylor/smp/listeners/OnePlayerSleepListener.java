package com.veylor.smp.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.Plugin;

public class OnePlayerSleepListener implements Listener {
    private final Plugin plugin;

    public OnePlayerSleepListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        if (!plugin.getConfig().getBoolean("one-player-sleep.enabled", true)) {
            return;
        }
        if (event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK) {
            return;
        }
        World world = event.getPlayer().getWorld();
        long time = world.getTime();
        if (time < 12541 || time > 23458) {
            return;
        }
        world.setTime(0);
        if (plugin.getConfig().getBoolean("one-player-sleep.clear-weather", true)) {
            world.setStorm(false);
            world.setThundering(false);
        }
        world.getPlayers().forEach(player -> player.sendMessage("§eOne player slept. Good morning!"));
    }
}
