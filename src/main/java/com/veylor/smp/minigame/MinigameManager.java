package com.veylor.smp.minigame;

import com.veylor.smp.economy.EconomyManager;
import com.veylor.smp.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class MinigameManager implements Listener {
    private final Plugin plugin;
    private final EconomyManager economyManager;
    private final Map<UUID, MinigameSession> activeSessions = new HashMap<>();
    private final Map<UUID, PlayerState> previousStates = new HashMap<>();
    private final Deque<UUID> sumoQueue = new ArrayDeque<>();

    public MinigameManager(Plugin plugin, EconomyManager economyManager) {
        this.plugin = plugin;
        this.economyManager = economyManager;
    }

    public Set<String> listMinigames() {
        return Set.of("spleef", "sumo", "parkour");
    }

    public boolean joinMinigame(Player player, String name) {
        if (activeSessions.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You are already in a minigame.");
            return false;
        }
        MinigameType type;
        switch (name.toLowerCase()) {
            case "spleef" -> type = MinigameType.SPLEEF;
            case "sumo" -> type = MinigameType.SUMO;
            case "parkour" -> type = MinigameType.PARKOUR;
            default -> {
                player.sendMessage(ChatColor.RED + "Unknown minigame.");
                return false;
            }
        }
        storeState(player);
        if (type == MinigameType.SUMO) {
            queueSumo(player);
            return true;
        }
        activeSessions.put(player.getUniqueId(), new MinigameSession(type, null));
        if (type == MinigameType.SPLEEF) {
            startSpleef(player);
        } else {
            startParkour(player);
        }
        return true;
    }

    public void leaveMinigame(Player player) {
        MinigameSession session = activeSessions.remove(player.getUniqueId());
        sumoQueue.remove(player.getUniqueId());
        if (session != null && session.type() == MinigameType.SUMO && session.opponent() != null) {
            Player opponent = Bukkit.getPlayer(session.opponent());
            if (opponent != null) {
                opponent.sendMessage(ChatColor.YELLOW + player.getName() + " left the sumo match.");
                activeSessions.remove(opponent.getUniqueId());
                restoreState(opponent);
            }
        }
        restoreState(player);
    }

    private void storeState(Player player) {
        previousStates.put(player.getUniqueId(), new PlayerState(
                player.getLocation(),
                player.getInventory().getContents(),
                player.getInventory().getArmorContents(),
                player.getGameMode()
        ));
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setGameMode(GameMode.ADVENTURE);
    }

    private void restoreState(Player player) {
        PlayerState state = previousStates.remove(player.getUniqueId());
        if (state != null) {
            player.getInventory().setContents(state.contents());
            player.getInventory().setArmorContents(state.armor());
            player.teleport(state.location());
            player.setGameMode(state.gameMode());
        }
    }

    private void startSpleef(Player player) {
        Location spawn = getLocation("minigames.spleef.spawn");
        if (spawn == null) {
            player.sendMessage(ChatColor.RED + "Spleef arena is not configured.");
            leaveMinigame(player);
            return;
        }
        player.teleport(spawn);
        ItemStack shovel = new ItemStack(Material.DIAMOND_SHOVEL);
        ItemMeta meta = shovel.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + "Spleef Shovel");
            shovel.setItemMeta(meta);
        }
        player.getInventory().addItem(shovel);
        player.sendMessage(ChatColor.GREEN + "Spleef started! Stay above the arena.");
    }

    private void startParkour(Player player) {
        Location start = getLocation("minigames.parkour.start");
        if (start == null) {
            player.sendMessage(ChatColor.RED + "Parkour arena is not configured.");
            leaveMinigame(player);
            return;
        }
        player.teleport(start);
        player.sendMessage(ChatColor.GREEN + "Parkour started! Reach the finish block.");
    }

    private void queueSumo(Player player) {
        if (sumoQueue.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You are already in the sumo queue.");
            return;
        }
        sumoQueue.add(player.getUniqueId());
        player.sendMessage(ChatColor.YELLOW + "Joined sumo queue. Waiting for an opponent...");
        if (sumoQueue.size() >= 2) {
            UUID first = sumoQueue.poll();
            UUID second = sumoQueue.poll();
            if (first == null || second == null) {
                return;
            }
            Player playerOne = Bukkit.getPlayer(first);
            Player playerTwo = Bukkit.getPlayer(second);
            if (playerOne == null || playerTwo == null) {
                if (playerOne != null) {
                    playerOne.sendMessage(ChatColor.RED + "Opponent left the queue.");
                    leaveMinigame(playerOne);
                }
                if (playerTwo != null) {
                    playerTwo.sendMessage(ChatColor.RED + "Opponent left the queue.");
                    leaveMinigame(playerTwo);
                }
                return;
            }
            startSumoMatch(playerOne, playerTwo);
        }
    }

    private void startSumoMatch(Player playerOne, Player playerTwo) {
        Location spawn1 = getLocation("minigames.sumo.spawn1");
        Location spawn2 = getLocation("minigames.sumo.spawn2");
        if (spawn1 == null || spawn2 == null) {
            playerOne.sendMessage(ChatColor.RED + "Sumo arena is not configured.");
            playerTwo.sendMessage(ChatColor.RED + "Sumo arena is not configured.");
            leaveMinigame(playerOne);
            leaveMinigame(playerTwo);
            return;
        }
        activeSessions.put(playerOne.getUniqueId(), new MinigameSession(MinigameType.SUMO, playerTwo.getUniqueId()));
        activeSessions.put(playerTwo.getUniqueId(), new MinigameSession(MinigameType.SUMO, playerOne.getUniqueId()));
        playerOne.teleport(spawn1);
        playerTwo.teleport(spawn2);
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Sumo Stick");
            stick.setItemMeta(meta);
        }
        playerOne.getInventory().addItem(stick);
        playerTwo.getInventory().addItem(stick);
        playerOne.sendMessage(ChatColor.GREEN + "Sumo match started! Knock your opponent off.");
        playerTwo.sendMessage(ChatColor.GREEN + "Sumo match started! Knock your opponent off.");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        MinigameSession session = activeSessions.get(player.getUniqueId());
        if (session == null) {
            return;
        }
        switch (session.type()) {
            case SPLEEF -> checkSpleef(player);
            case SUMO -> checkSumo(player, session);
            case PARKOUR -> checkParkour(player);
            default -> {
            }
        }
    }

    private void checkSpleef(Player player) {
        double fallY = plugin.getConfig().getDouble("minigames.spleef.fall-y", 0);
        if (player.getLocation().getY() <= fallY) {
            player.sendMessage(ChatColor.RED + "You fell out of the spleef arena!");
            activeSessions.remove(player.getUniqueId());
            restoreState(player);
            checkSpleefWinner();
        }
    }

    private void checkSpleefWinner() {
        Set<UUID> remaining = activeSessions.entrySet().stream()
                .filter(entry -> entry.getValue().type() == MinigameType.SPLEEF)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        if (remaining.size() == 1) {
            UUID winnerId = remaining.iterator().next();
            Player winner = Bukkit.getPlayer(winnerId);
            if (winner != null) {
                int reward = plugin.getConfig().getInt("rewards.spleef-win", 0);
                if (reward > 0) {
                    economyManager.addBalance(winnerId, reward);
                }
                winner.sendMessage(ChatColor.GOLD + "You won spleef! Reward: " + reward + " coins.");
                activeSessions.remove(winnerId);
                restoreState(winner);
            }
        }
    }

    private void checkSumo(Player player, MinigameSession session) {
        double fallY = plugin.getConfig().getDouble("minigames.sumo.fall-y", 0);
        if (player.getLocation().getY() <= fallY) {
            Player opponent = session.opponent() != null ? Bukkit.getPlayer(session.opponent()) : null;
            player.sendMessage(ChatColor.RED + "You were knocked out!");
            activeSessions.remove(player.getUniqueId());
            restoreState(player);
            if (opponent != null) {
                int reward = plugin.getConfig().getInt("rewards.sumo-win", 0);
                if (reward > 0) {
                    economyManager.addBalance(opponent.getUniqueId(), reward);
                }
                opponent.sendMessage(ChatColor.GOLD + "You won the sumo match! Reward: " + reward + " coins.");
                activeSessions.remove(opponent.getUniqueId());
                restoreState(opponent);
            }
        }
    }

    private void checkParkour(Player player) {
        String materialName = plugin.getConfig().getString("minigames.parkour.finish-block", "GOLD_BLOCK");
        Material finishMaterial = Material.matchMaterial(materialName);
        if (finishMaterial == null) {
            return;
        }
        Location location = player.getLocation();
        Material below = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()).getType();
        if (below == finishMaterial) {
            int reward = plugin.getConfig().getInt("rewards.parkour-finish", 0);
            if (reward > 0) {
                economyManager.addBalance(player.getUniqueId(), reward);
            }
            player.sendMessage(ChatColor.GOLD + "Parkour complete! Reward: " + reward + " coins.");
            activeSessions.remove(player.getUniqueId());
            restoreState(player);
        }
    }

    private Location getLocation(String path) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection(path);
        return LocationUtil.loadLocation(section);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        activeSessions.remove(player.getUniqueId());
        sumoQueue.remove(player.getUniqueId());
        previousStates.remove(player.getUniqueId());
    }
}
