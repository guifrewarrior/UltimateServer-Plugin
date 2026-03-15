package com.ultimateserver.listener;

import com.ultimateserver.UltimateServerPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class ServerListener implements Listener {
    private final UltimateServerPlugin plugin;

    public ServerListener(UltimateServerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerId = event.getPlayer().getUniqueId().toString();
        String playerName = event.getPlayer().getName();

        try {
            plugin.getDatabaseManager().insertPlayerData(playerId, playerName, 1000.0);
            event.setJoinMessage("§a" + playerName + " joined the server!");
        } catch (Exception e) {
            plugin.getLogger().warning("Could not insert player data: " + e.getMessage());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerId = event.getPlayer().getUniqueId().toString();
        event.setQuitMessage("§c" + event.getPlayer().getName() + " left the server!");
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        String message = event.getMessage();

        if (message.contains("bad")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYour message was blocked!");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        String playerId = event.getPlayer().getUniqueId().toString();
        if (!plugin.getProtectionManager().canBuildAt(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou cannot break blocks in this region!");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        String playerId = event.getPlayer().getUniqueId().toString();
        if (!plugin.getProtectionManager().canBuildAt(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou cannot place blocks in this region!");
        }
    }
}
