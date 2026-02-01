package com.veylor.smp.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WildCommand implements CommandExecutor {
    private final Plugin plugin;
    private final Random random = new Random();

    public WildCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        World world = player.getWorld();
        int radius = plugin.getConfig().getInt("wild.radius", 5000);
        int maxTries = plugin.getConfig().getInt("wild.max-tries", 10);
        Set<Material> avoid = new HashSet<>();
        List<String> avoidList = plugin.getConfig().getStringList("wild.avoid-blocks");
        for (String materialName : avoidList) {
            Material material = Material.matchMaterial(materialName);
            if (material != null) {
                avoid.add(material);
            }
        }
        WorldBorder border = world.getWorldBorder();
        double borderRadius = border.getSize() / 2.0;
        for (int attempt = 0; attempt < maxTries; attempt++) {
            double x = random.nextDouble() * radius * 2 - radius;
            double z = random.nextDouble() * radius * 2 - radius;
            double targetX = border.getCenter().getX() + x;
            double targetZ = border.getCenter().getZ() + z;
            if (Math.abs(targetX - border.getCenter().getX()) > borderRadius || Math.abs(targetZ - border.getCenter().getZ()) > borderRadius) {
                continue;
            }
            int highestY = world.getHighestBlockYAt((int) targetX, (int) targetZ);
            Location location = new Location(world, targetX + 0.5, highestY + 1, targetZ + 0.5);
            Material block = world.getBlockAt((int) targetX, highestY, (int) targetZ).getType();
            if (avoid.contains(block)) {
                continue;
            }
            player.teleport(location);
            player.sendMessage(ChatColor.GREEN + "You have been teleported into the wild!");
            return true;
        }
        player.sendMessage(ChatColor.RED + "Failed to find a safe location. Try again.");
        return true;
    }
}
