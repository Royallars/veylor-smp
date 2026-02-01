package com.veylor.smp.commands;

import com.veylor.smp.warp.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {
    private final WarpManager warpManager;

    public WarpCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Available warps: " + String.join(", ", warpManager.getWarpNames()));
            return true;
        }
        Location warp = warpManager.getWarp(args[0]);
        if (warp == null) {
            player.sendMessage(ChatColor.RED + "Warp not found.");
            return true;
        }
        player.teleport(warp);
        player.sendMessage(ChatColor.GREEN + "Warped to " + args[0] + ".");
        return true;
    }
}
