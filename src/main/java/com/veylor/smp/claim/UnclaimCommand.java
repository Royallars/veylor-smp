package com.veylor.smp.claim;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnclaimCommand implements CommandExecutor {
    private final ClaimManager claimManager;

    public UnclaimCommand(ClaimManager claimManager) {
        this.claimManager = claimManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can unclaim land.");
            return true;
        }
        if (!claimManager.unclaimChunk(player)) {
            player.sendMessage("§cYou do not own this chunk.");
            return true;
        }
        player.sendMessage("§aChunk unclaimed.");
        return true;
    }
}
