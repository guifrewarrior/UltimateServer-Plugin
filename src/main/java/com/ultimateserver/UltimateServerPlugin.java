package com.ultimateserver;

import com.ultimateserver.config.ConfigManager;
import com.ultimateserver.database.DatabaseManager;
import com.ultimateserver.listener.ServerListener;
import com.ultimateserver.optimization.PerformanceOptimizer;
import com.ultimateserver.player.PlayerDataManager;
import com.ultimateserver.protection.RegionManager;
import com.ultimateserver.moderation.ModerationManager;
import com.ultimateserver.economy.EconomyManager;
import com.ultimateserver.command.AdminCommandExecutor;
import com.ultimateserver.command.EconomyCommandExecutor;
import com.ultimateserver.command.ProtectionCommandExecutor;
import com.ultimateserver.command.ModerationCommandExecutor;
import com.ultimateserver.command.PlayerCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class UltimateServerPlugin extends JavaPlugin {
    private static UltimateServerPlugin instance;
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private PerformanceOptimizer performanceOptimizer;
    private PlayerDataManager playerManager;
    private RegionManager protectionManager;
    private ModerationManager moderationManager;
    private EconomyManager economyManager;
    private ServerListener serverListener;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("=".repeat(50));
        getLogger().info("UltimateServer Plugin v1.0.0 - Initializing...");
        getLogger().info("=".repeat(50));

        try {
            configManager = new ConfigManager(this);
            getLogger().info("Config loaded successfully!");

            databaseManager = new DatabaseManager(configManager);
            getLogger().info("Database connection established!");

            performanceOptimizer = new PerformanceOptimizer(this);
            getLogger().info("Performance optimization enabled!");

            playerManager = new PlayerDataManager(this);
            getLogger().info("Player manager initialized!");

            protectionManager = new RegionManager(this);
            getLogger().info("Protection manager initialized!");

            moderationManager = new ModerationManager(this);
            getLogger().info("Moderation manager initialized!");

            economyManager = new EconomyManager(this);
            getLogger().info("Economy manager initialized!");

            serverListener = new ServerListener(this);
            Bukkit.getPluginManager().registerEvents(serverListener, this);
            getLogger().info("Event listeners registered!");

            registerCommands();
            getLogger().info("Commands registered!");

            performanceOptimizer.startOptimizationTasks();
            getLogger().info("Optimization tasks started!");

            getLogger().info("=".repeat(50));
            getLogger().info("UltimateServer Plugin - ENABLED SUCCESSFULLY!");
            getLogger().info("=".repeat(50));
        } catch (Exception e) {
            getLogger().severe("Failed to enable plugin: " + e.getMessage());
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (performanceOptimizer != null) {
            performanceOptimizer.stopOptimizationTasks();
        }
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("UltimateServer Plugin - DISABLED!");
    }

    private void registerCommands() {
        getCommand("admin").setExecutor(new AdminCommandExecutor(this));
        getCommand("economy").setExecutor(new EconomyCommandExecutor(this));
        getCommand("protect").setExecutor(new ProtectionCommandExecutor(this));
        getCommand("moderation").setExecutor(new ModerationCommandExecutor(this));
        getCommand("player").setExecutor(new PlayerCommandExecutor(this));
    }

    public static UltimateServerPlugin getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public PerformanceOptimizer getPerformanceOptimizer() {
        return performanceOptimizer;
    }

    public PlayerDataManager getPlayerManager() {
        return playerManager;
    }

    public RegionManager getProtectionManager() {
        return protectionManager;
    }

    public ModerationManager getModerationManager() {
        return moderationManager;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public UltimateServerPlugin getUltimateServerPlugin() {
        return this;
    }
}
