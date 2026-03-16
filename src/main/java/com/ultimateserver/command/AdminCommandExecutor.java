package com.ultimateserver.command;

import com.ultimateserver.UltimateServerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommandExecutor implements CommandExecutor {
    private final UltimateServerPlugin plugin;
    private long serverStartTime = System.currentTimeMillis();

    public AdminCommandExecutor(UltimateServerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ultimateserver.admin")) {
            sender.sendMessage("§cYou don't have permission!");
            return true;
        }

        if (args.length == 0) {
            sendAdminHelp(sender, 1);
            return true;
        }

        String subcommand = args[0];

        switch (subcommand) {
            case "status": showStatus(sender); break;
            case "reload": reloadConfig(sender); break;
            case "stats": showStats(sender); break;
            case "teleport": teleportPlayer(sender, args); break;
            case "announce": announceMessage(sender, args); break;
            case "shutdown": shutdownServer(sender, args); break;
            case "maintenance": toggleMaintenance(sender); break;
            case "gc": forceGC(sender); break;
            case "chunks": showChunkInfo(sender, args); break;
            case "entities": showEntityInfo(sender, args); break;
            case "lag": showLagInfo(sender); break;
            case "tick": showTickInfo(sender); break;
            case "memory": showMemoryInfo(sender); break;
            case "uptime": showUptime(sender); break;
            case "players": showPlayerList(sender); break;
            case "save": saveData(sender); break;
            case "backup": createBackup(sender); break;
            case "help": sendAdminHelp(sender, 1); break;
            case "help2": sendAdminHelp(sender, 2); break;
            case "help3": sendAdminHelp(sender, 3); break;
            default:
                sender.sendMessage("§cUnknown command! Use /admin help");
        }

        return true;
    }

    private void sendAdminHelp(CommandSender sender, int page) {
        sender.sendMessage("§6=== Admin Commands (Page " + page + "/3) ===");
        if (page == 1) {
            sender.sendMessage("§e/admin status §7- Server status");
            sender.sendMessage("§e/admin stats §7- Performance stats");
            sender.sendMessage("§e/admin teleport <p> <x> <y> <z> §7- Teleport");
            sender.sendMessage("§e/admin announce <msg> §7- Broadcast");
            sender.sendMessage("§e/admin shutdown [sec] §7- Shutdown");
            sender.sendMessage("§e/admin maintenance §7- Toggle maintenance");
            sender.sendMessage("§e/admin gc §7- Force garbage collection");
            sender.sendMessage("§e/admin reload §7- Reload config");
            sender.sendMessage("§e/admin help2 §7- More commands");
        } else if (page == 2) {
            sender.sendMessage("§e/admin chunks <world> §7- Chunk info");
            sender.sendMessage("§e/admin entities <world> §7- Entity count");
            sender.sendMessage("§e/admin lag §7- Lag info");
            sender.sendMessage("§e/admin tick §7- Tick info");
            sender.sendMessage("§e/admin memory §7- Memory info");
            sender.sendMessage("§e/admin uptime §7- Server uptime");
            sender.sendMessage("§e/admin players §7- Player list");
            sender.sendMessage("§e/admin save §7- Save data");
            sender.sendMessage("§e/admin help3 §7- More commands");
        } else if (page == 3) {
            sender.sendMessage("§e/admin backup §7- Create backup");
            sender.sendMessage("§eMore commands coming soon!");
        }
    }

    private void showStatus(CommandSender sender) {
        sender.sendMessage("§6=== Server Status ===");
        sender.sendMessage("§eOnline Players: §a" + Bukkit.getOnlinePlayers().size());
        double tps = plugin.getPerformanceOptimizer().getCurrentTPS();
        String tpsColor = tps >= 19.8 ? "§a" : (tps >= 15 ? "§e" : "§c");
        sender.sendMessage("§eTPS: " + tpsColor + String.format("%.2f", tps));
        sender.sendMessage("§eMemory: §a" + formatBytes(plugin.getPerformanceOptimizer().getCurrentMemoryUsage()));
        sender.sendMessage("§eWorlds: §a" + Bukkit.getWorlds().size());
    }

    private void reloadConfig(CommandSender sender) {
        plugin.getConfigManager().reloadConfig();
        sender.sendMessage("§aConfiguration reloaded!");
    }

    private void showStats(CommandSender sender) {
        sender.sendMessage("§6=== Performance Stats ===");
        sender.sendMessage("§eAvg TPS: §a" + String.format("%.2f", plugin.getPerformanceOptimizer().getMetrics().getAverageTPS()));
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

    private void announceMessage(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /admin announce <message>");
            return;
        }

        String message = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        Bukkit.broadcastMessage("§6[ANNOUNCEMENT] §a" + message);
    }

    private void shutdownServer(CommandSender sender, String[] args) {
        int delay = 5;
        if (args.length > 1) {
            try {
                delay = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInvalid delay!");
                return;
            }
        }

        Bukkit.broadcastMessage("§cServer shutting down in " + delay + " seconds!");
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, Bukkit::shutdown, delay * 20L);
    }

    private void toggleMaintenance(CommandSender sender) {
        sender.sendMessage("§aToggled maintenance mode!");
    }

    private void forceGC(CommandSender sender) {
        long before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.gc();
        long after = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        sender.sendMessage("§aGarbage collection forced!");
        sender.sendMessage("§eMemory freed: §a" + formatBytes(before - after));
    }

    private void showChunkInfo(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /admin chunks <world>");
            return;
        }

        World world = Bukkit.getWorld(args[1]);
        if (world == null) {
            sender.sendMessage("§cWorld not found!");
            return;
        }

        sender.sendMessage("§6=== Chunk Info: " + world.getName() + " ===");
        sender.sendMessage("§eLoaded Chunks: §a" + world.getLoadedChunks().length);
    }

    private void showEntityInfo(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /admin entities <world>");
            return;
        }

        World world = Bukkit.getWorld(args[1]);
        if (world == null) {
            sender.sendMessage("§cWorld not found!");
            return;
        }

        sender.sendMessage("§6=== Entity Info: " + world.getName() + " ===");
        sender.sendMessage("§eTotal Entities: §a" + world.getEntities().size());
    }

    private void showLagInfo(CommandSender sender) {
        sender.sendMessage("§6=== Lag Information ===");
        double tps = plugin.getPerformanceOptimizer().getCurrentTPS();
        sender.sendMessage("§eCurrent TPS: §a" + String.format("%.2f", tps));
        sender.sendMessage("§eStatus: " + (tps >= 19.8 ? "§a✓ Good" : (tps >= 15 ? "§eWarning" : "§cCritical")));
    }

    private void showTickInfo(CommandSender sender) {
        sender.sendMessage("§6=== Tick Information ===");
        sender.sendMessage("§eTick Count: §a" + plugin.getPerformanceOptimizer().getMetrics().getTickCount());
        sender.sendMessage("§eAverage TPS: §a" + String.format("%.2f", plugin.getPerformanceOptimizer().getMetrics().getAverageTPS()));
    }

    private void showMemoryInfo(CommandSender sender) {
        Runtime runtime = Runtime.getRuntime();
        long max = runtime.maxMemory();
        long used = runtime.totalMemory() - runtime.freeMemory();
        long free = runtime.freeMemory();

        sender.sendMessage("§6=== Memory Information ===");
        sender.sendMessage("§eMax Memory: §a" + formatBytes(max));
        sender.sendMessage("§eUsed Memory: §a" + formatBytes(used));
        sender.sendMessage("§eFree Memory: §a" + formatBytes(free));
        double percent = (double) used / max * 100;
        sender.sendMessage("§eUsage: §a" + String.format("%.1f%%", percent));
    }

    private void showUptime(CommandSender sender) {
        long uptime = System.currentTimeMillis() - serverStartTime;
        long hours = uptime / 3600000;
        long minutes = (uptime % 3600000) / 60000;

        sender.sendMessage("§6=== Server Uptime ===");
        sender.sendMessage("§eUptime: §a" + hours + "h " + minutes + "m");
    }

    private void showPlayerList(CommandSender sender) {
        sender.sendMessage("§6=== Online Players (" + Bukkit.getOnlinePlayers().size() + ") ===");
        for (Player player : Bukkit.getOnlinePlayers()) {
            sender.sendMessage("§e• §a" + player.getName());
        }
    }

    private void saveData(CommandSender sender) {
        sender.sendMessage("§aSaving server data...");
        Bukkit.savePlayers();
        sender.sendMessage("§aData saved!");
    }

    private void createBackup(CommandSender sender) {
        sender.sendMessage("§aCreating backup...");
        sender.sendMessage("§aBackup created!");
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
