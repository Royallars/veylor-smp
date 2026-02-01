package com.veylor.smp.claim;

import com.veylor.smp.data.DataStore;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClaimManager {
    private final DataStore dataStore;
    private final Map<String, UUID> claims = new HashMap<>();

    public ClaimManager(DataStore dataStore) {
        this.dataStore = dataStore;
        loadClaims();
    }

    public boolean isClaimed(Chunk chunk) {
        return claims.containsKey(key(chunk.getWorld(), chunk.getX(), chunk.getZ()));
    }

    public UUID getOwner(Chunk chunk) {
        return claims.get(key(chunk.getWorld(), chunk.getX(), chunk.getZ()));
    }

    public boolean claimChunk(Player player) {
        Chunk chunk = player.getLocation().getChunk();
        String key = key(chunk.getWorld(), chunk.getX(), chunk.getZ());
        if (claims.containsKey(key)) {
            return false;
        }
        claims.put(key, player.getUniqueId());
        saveClaim(key, player.getUniqueId());
        return true;
    }

    public boolean unclaimChunk(Player player) {
        Chunk chunk = player.getLocation().getChunk();
        String key = key(chunk.getWorld(), chunk.getX(), chunk.getZ());
        UUID owner = claims.get(key);
        if (owner == null || !owner.equals(player.getUniqueId())) {
            return false;
        }
        claims.remove(key);
        dataStore.getConfig().set("claims." + key, null);
        dataStore.save();
        return true;
    }

    private void loadClaims() {
        ConfigurationSection section = dataStore.getConfig().getConfigurationSection("claims");
        if (section == null) {
            return;
        }
        for (String key : section.getKeys(false)) {
            String uuid = section.getString(key);
            if (uuid != null) {
                claims.put(key, UUID.fromString(uuid));
            }
        }
    }

    private void saveClaim(String key, UUID owner) {
        dataStore.getConfig().set("claims." + key, owner.toString());
        dataStore.save();
    }

    private String key(World world, int x, int z) {
        return world.getName() + ":" + x + ":" + z;
    }
}
