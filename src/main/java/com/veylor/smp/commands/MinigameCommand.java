package com.veylor.smp.commands;

import com.veylor.smp.minigame.MinigameManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinigameCommand implements CommandExecutor {
    private final MinigameManager minigameManager;

    public MinigameCommand(MinigameManager minigameManager) {
        this.minigameManager = minigameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /minigame <join|leave|list> [name]");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "list" -> player.sendMessage(ChatColor.GREEN + "Minigames: " + String.join(", ", minigameManager.listMinigames()));
            case "leave" -> {
                minigameManager.leaveMinigame(player);
                player.sendMessage(ChatColor.YELLOW + "You left the minigame.");
            }
            case "join" -> {
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /minigame join <name>");
                    return true;
                }
                minigameManager.joinMinigame(player, args[1]);
            }
            default -> player.sendMessage(ChatColor.RED + "Unknown subcommand.");
        }
        return true;
    }
}
