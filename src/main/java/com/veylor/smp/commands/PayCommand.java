package com.veylor.smp.commands;

import com.veylor.smp.economy.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {
    private final EconomyManager economyManager;

    public PayCommand(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /pay <player> <amount>");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }
        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You cannot pay yourself.");
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            player.sendMessage(ChatColor.RED + "Amount must be a number.");
            return true;
        }
        if (amount <= 0) {
            player.sendMessage(ChatColor.RED + "Amount must be positive.");
            return true;
        }
        if (!economyManager.withdraw(player.getUniqueId(), amount)) {
            player.sendMessage(ChatColor.RED + "You do not have enough coins.");
            return true;
        }
        economyManager.addBalance(target.getUniqueId(), amount);
        player.sendMessage(ChatColor.GREEN + "Paid " + target.getName() + " " + amount + " coins.");
        target.sendMessage(ChatColor.GOLD + player.getName() + " sent you " + amount + " coins.");
        return true;
    }
}
