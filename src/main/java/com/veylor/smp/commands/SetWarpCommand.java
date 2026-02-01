package com.veylor.smp.commands;

import com.veylor.smp.warp.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {
    private final WarpManager warpManager;

    public SetWarpCommand(WarpManager warpManager) {
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
            player.sendMessage(ChatColor.RED + "Usage: /setwarp <name>");
            return true;
        }
        warpManager.setWarp(args[0], player.getLocation());
        player.sendMessage(ChatColor.GREEN + "Warp set: " + args[0]);
        return true;
    }
}
