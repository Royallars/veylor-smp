package com.veylor.smp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatLogListener implements Listener {
    private final Map<UUID, Long> combatLog = new HashMap<>();
    private final int combatSeconds;
    private final boolean enabled;

    public CombatLogListener(Plugin plugin) {
        this.enabled = plugin.getConfig().getBoolean("combat-logging.enabled", true);
        this.combatSeconds = plugin.getConfig().getInt("combat-logging.timer-seconds", 15);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!enabled) {
            return;
        }
        Player attacker = getPlayer(event.getDamager());
        Player victim = getPlayer(event.getEntity());
        if (attacker == null || victim == null || attacker.equals(victim)) {
            return;
        }
        markInCombat(attacker);
        markInCombat(victim);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!enabled) {
            return;
        }
        Player player = event.getPlayer();
        if (!isInCombat(player.getUniqueId())) {
            return;
        }
        combatLog.remove(player.getUniqueId());
        player.setHealth(0.0);
        Bukkit.broadcastMessage("§c" + player.getName() + " logged out during combat!");
    }

    private Player getPlayer(Entity entity) {
        return entity instanceof Player player ? player : null;
    }

    private void markInCombat(Player player) {
        combatLog.put(player.getUniqueId(), System.currentTimeMillis() + (combatSeconds * 1000L));
        player.sendMessage("§cYou are now in combat for " + combatSeconds + "s.");
    }

    private boolean isInCombat(UUID uuid) {
        Long expiry = combatLog.get(uuid);
        if (expiry == null) {
            return false;
        }
        if (expiry < System.currentTimeMillis()) {
            combatLog.remove(uuid);
            return false;
        }
        return true;
    }
}
