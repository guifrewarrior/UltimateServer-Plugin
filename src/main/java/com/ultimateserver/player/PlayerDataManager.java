package com.ultimateserver.player;

import com.ultimateserver.UltimateServerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerDataManager {
    private final UltimateServerPlugin plugin;
    private Map<String, PlayerData> playerDataCache;

    public PlayerDataManager(UltimateServerPlugin plugin) {
        this.plugin = plugin;
        this.playerDataCache = new HashMap<>();
    }

    public void loadPlayerData(Player player) {
        String playerId = player.getUniqueId().toString();
        String playerName = player.getName();

        try {
            double balance = plugin.getDatabaseManager().getPlayerBalance(playerId);
            PlayerData playerData = new PlayerData(playerId, playerName, balance);
            playerDataCache.put(playerId, playerData);
        } catch (Exception e) {
            PlayerData playerData = new PlayerData(playerId, playerName, 1000.0);
            playerDataCache.put(playerId, playerData);
            plugin.getLogger().warning("Could not load player data from database: " + e.getMessage());
        }
    }

    public void savePlayerData(Player player) {
        String playerId = player.getUniqueId().toString();
        PlayerData data = playerDataCache.get(playerId);

        if (data != null) {
            try {
                plugin.getDatabaseManager().updatePlayerBalance(playerId, data.getBalance());
            } catch (Exception e) {
                plugin.getLogger().warning("Could not save player data: " + e.getMessage());
            }
        }
    }

    public PlayerData getPlayerData(String playerId) {
        return playerDataCache.get(playerId);
    }

    public void addBalance(String playerId, double amount) {
        PlayerData data = playerDataCache.get(playerId);
        if (data != null) {
            data.addBalance(amount);
        }
    }

    public void removeBalance(String playerId, double amount) {
        PlayerData data = playerDataCache.get(playerId);
        if (data != null) {
            data.removeBalance(amount);
        }
    }

    public double getBalance(String playerId) {
        PlayerData data = playerDataCache.get(playerId);
        return data != null ? data.getBalance() : 0.0;
    }

    public void addRank(String playerId, String rank) {
        PlayerData data = playerDataCache.get(playerId);
        if (data != null) {
            data.addRank(rank);
        }
    }

    public void removePlayerData(String playerId) {
        playerDataCache.remove(playerId);
    }

    public Collection<PlayerData> getAllPlayerData() {
        return playerDataCache.values();
    }
}
