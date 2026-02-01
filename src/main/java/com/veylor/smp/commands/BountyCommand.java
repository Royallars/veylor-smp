package com.veylor.smp.commands;

import com.veylor.smp.economy.BountyManager;
import com.veylor.smp.economy.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BountyCommand implements CommandExecutor {
    private final BountyManager bountyManager;
    private final EconomyManager economyManager;

    public BountyCommand(BountyManager bountyManager, EconomyManager economyManager) {
        this.bountyManager = bountyManager;
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /bounty <player> [amount]");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }
        if (args.length == 1) {
            int bounty = bountyManager.getBounty(target.getUniqueId());
            sender.sendMessage("§e" + target.getName() + " has a bounty of §6" + bounty);
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            sender.sendMessage("Amount must be a number.");
            return true;
        }
        if (amount <= 0) {
            sender.sendMessage("Amount must be positive.");
            return true;
        }
        if (sender instanceof Player player) {
            if (!economyManager.withdraw(player.getUniqueId(), amount)) {
                player.sendMessage("§cYou do not have enough money.");
                return true;
            }
        }
        bountyManager.addBounty(target.getUniqueId(), amount);
        Bukkit.broadcastMessage("§cA bounty of §6" + amount + " §chas been placed on " + target.getName() + "!");
        return true;
    }
}
