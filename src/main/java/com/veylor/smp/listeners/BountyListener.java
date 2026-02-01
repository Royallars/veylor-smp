package com.veylor.smp.listeners;

import com.veylor.smp.economy.BountyManager;
import com.veylor.smp.economy.EconomyManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BountyListener implements Listener {
    private final BountyManager bountyManager;
    private final EconomyManager economyManager;

    public BountyListener(BountyManager bountyManager, EconomyManager economyManager) {
        this.bountyManager = bountyManager;
        this.economyManager = economyManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        if (killer == null) {
            return;
        }
        int bounty = bountyManager.getBounty(victim.getUniqueId());
        if (bounty <= 0) {
            return;
        }
        bountyManager.clearBounty(victim.getUniqueId());
        economyManager.addBalance(killer.getUniqueId(), bounty);
        killer.sendMessage("§aYou claimed a bounty of §6" + bounty + "§a!");
        victim.sendMessage("§cYour bounty was claimed.");
    }
}
