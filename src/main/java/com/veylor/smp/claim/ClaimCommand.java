package com.veylor.smp.claim;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimCommand implements CommandExecutor {
    private final ClaimManager claimManager;

    public ClaimCommand(ClaimManager claimManager) {
        this.claimManager = claimManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can claim land.");
            return true;
        }
        if (!claimManager.claimChunk(player)) {
            player.sendMessage("§cThis chunk is already claimed.");
            return true;
        }
        player.sendMessage("§aChunk claimed!");
        return true;
    }
}
