package com.veylor.smp.listeners;

import com.veylor.smp.economy.EconomyManager;
import com.veylor.smp.warp.SpawnManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConnectionListener implements Listener {
    private final EconomyManager economyManager;
    private final SpawnManager spawnManager;

    public PlayerConnectionListener(EconomyManager economyManager, SpawnManager spawnManager) {
        this.economyManager = economyManager;
        this.spawnManager = spawnManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        economyManager.ensureAccount(player.getUniqueId());
        if (!player.hasPlayedBefore()) {
            Location spawn = spawnManager.getSpawn();
            if (spawn != null) {
                player.teleport(spawn);
            }
            player.sendMessage(ChatColor.AQUA + "Welcome to Veylor SMP! Use /smp for help.");
        }
    }
}
