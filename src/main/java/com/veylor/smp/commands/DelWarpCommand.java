package com.veylor.smp.commands;

import com.veylor.smp.warp.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelWarpCommand implements CommandExecutor {
    private final WarpManager warpManager;

    public DelWarpCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (!sender.hasPermission("veylorsmp.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /delwarp <name>");
            return true;
        }
        if (warpManager.deleteWarp(args[0])) {
            player.sendMessage(ChatColor.GREEN + "Warp removed: " + args[0]);
        } else {
            player.sendMessage(ChatColor.RED + "Warp not found.");
        }
        return true;
    }
}
