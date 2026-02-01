package com.veylor.smp.commands;

import com.veylor.smp.warp.HomeManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCommand implements CommandExecutor {
    private final HomeManager homeManager;

    public DelHomeCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (homeManager.deleteHome(player.getUniqueId())) {
            player.sendMessage(ChatColor.GREEN + "Home removed.");
        } else {
            player.sendMessage(ChatColor.RED + "You do not have a home set.");
        }
        return true;
    }
}
