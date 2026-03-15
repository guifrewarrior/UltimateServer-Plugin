package com.ultimateserver.config;

import com.ultimateserver.UltimateServerPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final UltimateServerPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(UltimateServerPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public String getDatabase() {
        return config.getString("database.url", "");
    }

    public String getDatabaseApiKey() {
        return config.getString("database.api-key", "");
    }

    public String getDatabaseAnonKey() {
        return config.getString("database.anon-key", "");
    }

    public boolean isOptimizationEnabled() {
        return config.getBoolean("optimization.enabled", true);
    }

    public int getOptimizationCheckInterval() {
        return config.getInt("optimization.check-interval", 20);
    }

    public boolean isEconomyEnabled() {
        return config.getBoolean("economy.enabled", true);
    }

    public double getStartingBalance() {
        return config.getDouble("economy.starting-balance", 1000.0);
    }

    public boolean isProtectionEnabled() {
        return config.getBoolean("protection.enabled", true);
    }

    public int getMaxClaimSize() {
        return config.getInt("protection.max-claim-size", 10000);
    }

    public boolean isModerationEnabled() {
        return config.getBoolean("moderation.enabled", true);
    }

    public int getMuteDefaultDuration() {
        return config.getInt("moderation.mute-duration", 3600);
    }
}
