package com.veylor.smp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SmpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GOLD + "Veylor SMP Commands:");
        sender.sendMessage(ChatColor.YELLOW + "/spawn" + ChatColor.WHITE + " - Return to spawn.");
        sender.sendMessage(ChatColor.YELLOW + "/sethome" + ChatColor.WHITE + " - Set your home.");
        sender.sendMessage(ChatColor.YELLOW + "/home" + ChatColor.WHITE + " - Teleport home.");
        sender.sendMessage(ChatColor.YELLOW + "/tpa <player>" + ChatColor.WHITE + " - Request a teleport.");
        sender.sendMessage(ChatColor.YELLOW + "/warp <name>" + ChatColor.WHITE + " - Warp to a location.");
        sender.sendMessage(ChatColor.YELLOW + "/minigame list" + ChatColor.WHITE + " - See minigames.");
        sender.sendMessage(ChatColor.YELLOW + "/balance" + ChatColor.WHITE + " - Check balance.");
        return true;
    }
}
