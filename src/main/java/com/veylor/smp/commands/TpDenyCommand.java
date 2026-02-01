package com.veylor.smp.commands;

import com.veylor.smp.teleport.TeleportRequestManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TpDenyCommand implements CommandExecutor {
    private final TeleportRequestManager teleportRequestManager;

    public TpDenyCommand(TeleportRequestManager teleportRequestManager) {
        this.teleportRequestManager = teleportRequestManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        UUID requesterId = teleportRequestManager.getRequester(player.getUniqueId());
        if (requesterId == null) {
            player.sendMessage(ChatColor.RED + "No pending teleport requests.");
            return true;
        }
        Player requester = Bukkit.getPlayer(requesterId);
        if (requester != null) {
            requester.sendMessage(ChatColor.RED + "Teleport request denied.");
        }
        player.sendMessage(ChatColor.YELLOW + "Teleport request denied.");
        teleportRequestManager.clear(player.getUniqueId());
        return true;
    }
}
