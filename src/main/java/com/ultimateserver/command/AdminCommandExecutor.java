package com.ultimateserver.command;

import com.ultimateserver.UltimateServerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommandExecutor implements CommandExecutor {
    private final UltimateServerPlugin plugin;

    public AdminCommandExecutor(UltimateServerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ultimateserver.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            sendAdminHelp(sender);
            return true;
        }

        String subcommand = args[0];

        switch (subcommand) {
            case "status":
                showStatus(sender);
                break;
            case "reload":
                reloadConfig(sender);
                break;
            case "stats":
                showStats(sender);
                break;
            case "teleport":
                teleportPlayer(sender, args);
                break;
            default:
                sender.sendMessage("§cUnknown command!");
                break;
        }

        return true;
    }

    private void sendAdminHelp(CommandSender sender) {
        sender.sendMessage("§6=== Admin Commands ===");
        sender.sendMessage("§e/admin status §7- Show server status");
        sender.sendMessage("§e/admin reload §7- Reload configuration");
        sender.sendMessage("§e/admin stats §7- Show performance stats");
        sender.sendMessage("§e/admin teleport <player> <x> <y> <z> §7- Teleport player");
    }

    private void showStatus(CommandSender sender) {
        sender.sendMessage("§6=== Server Status ===");
        sender.sendMessage("§eOnline Players: §a" + Bukkit.getOnlinePlayers().size());
        double tps = plugin.getPerformanceOptimizer().getCurrentTPS();
        String tpsColor = tps >= 19.8 ? "§a" : (tps >= 15 ? "§e" : "§c");
        sender.sendMessage("§eTPS: " + tpsColor + String.format("%.2f", tps));
        sender.sendMessage("§eMemory: §a" + formatBytes(plugin.getPerformanceOptimizer().getCurrentMemoryUsage()));
    }

    private void reloadConfig(CommandSender sender) {
        plugin.getConfigManager().reloadConfig();
        sender.sendMessage("§aConfiguration reloaded successfully!");
    }

    private void showStats(CommandSender sender) {
        sender.sendMessage("§6=== Performance Statistics ===");
        sender.sendMessage("§eAverage TPS: §a" + String.format("%.2f", plugin.getPerformanceOptimizer().getMetrics().getAverageTPS()));
        sender.sendMessage("§eTick Count: §a" + plugin.getPerformanceOptimizer().getMetrics().getTickCount());
        sender.sendMessage("§eGC Runs: §a" + plugin.getPerformanceOptimizer().getMetrics().getGCCount());
        sender.sendMessage("§eMemory Used: §a" + formatBytes(plugin.getPerformanceOptimizer().getCurrentMemoryUsage()));
        sender.sendMessage("§eMax Memory: §a" + formatBytes(plugin.getPerformanceOptimizer().getMaxMemory()));
    }

    private void teleportPlayer(CommandSender sender, String[] args) {
        if (args.length < 5) {
            sender.sendMessage("§cUsage: /admin teleport <player> <x> <y> <z>");
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage("§cPlayer not found!");
            return;
        }

        try {
            double x = Double.parseDouble(args[2]);
            double y = Double.parseDouble(args[3]);
            double z = Double.parseDouble(args[4]);
            player.teleport(new org.bukkit.Location(player.getWorld(), x, y, z));
            sender.sendMessage("§aPlayer teleported!");
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid coordinates!");
        }
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
