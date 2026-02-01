package com.veylor.smp.claim;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

public class ClaimListener implements Listener {
    private final ClaimManager claimManager;

    public ClaimListener(ClaimManager claimManager) {
        this.claimManager = claimManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isDenied(event.getPlayer(), event.getBlock().getChunk())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cThis chunk is claimed.");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (isDenied(event.getPlayer(), event.getBlock().getChunk())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cThis chunk is claimed.");
        }
    }

    private boolean isDenied(Player player, Chunk chunk) {
        if (!claimManager.isClaimed(chunk)) {
            return false;
        }
        UUID owner = claimManager.getOwner(chunk);
        return owner != null && !owner.equals(player.getUniqueId()) && !player.hasPermission("veylorsmp.admin");
    }
}
