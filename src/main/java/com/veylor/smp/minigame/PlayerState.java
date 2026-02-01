package com.veylor.smp.minigame;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public record PlayerState(Location location, ItemStack[] contents, ItemStack[] armor, GameMode gameMode) {
}
