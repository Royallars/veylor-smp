package com.veylor.smp.commands;

import com.veylor.smp.warp.SpawnManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    private final SpawnManager spawnManager;

    public SpawnCommand(SpawnManager spawnManager) {
        this.spawnManager = spawnManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        Location spawn = spawnManager.getSpawn();
        if (spawn == null) {
            player.sendMessage(ChatColor.RED + "Spawn is not configured yet.");
            return true;
        }
        player.teleport(spawn);
        player.sendMessage(ChatColor.GREEN + "Teleported to spawn.");
        return true;
    }
}
