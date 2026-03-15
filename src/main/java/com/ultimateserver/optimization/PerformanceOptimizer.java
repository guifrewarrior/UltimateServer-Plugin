package com.ultimateserver.optimization;

import com.ultimateserver.UltimateServerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

public class PerformanceOptimizer {
    private final UltimateServerPlugin plugin;
    private BukkitTask optimizationTask;
    private PerformanceMetrics metrics;

    public PerformanceOptimizer(UltimateServerPlugin plugin) {
        this.plugin = plugin;
        this.metrics = new PerformanceMetrics();
    }

    public void startOptimizationTasks() {
        optimizationTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            optimizeEntityLoading();
            optimizeMemory();
            optimizeChunkLoading();
            updateMetrics();
        }, 0L, 20L);
    }

    public void stopOptimizationTasks() {
        if (optimizationTask != null) {
            optimizationTask.cancel();
        }
    }

    private void optimizeEntityLoading() {
        for (World world : Bukkit.getWorlds()) {
            int entityCount = 0;
            for (Entity entity : world.getEntities()) {
                entityCount++;
                if (entityCount > 500) {
                    if (entity instanceof LivingEntity && !(entity instanceof org.bukkit.entity.Player)) {
                        entity.remove();
                    }
                }
            }
        }
    }

    private void optimizeMemory() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        double memoryUsagePercent = (double) usedMemory / maxMemory * 100;

        if (memoryUsagePercent > 85) {
            System.gc();
            metrics.recordGarbageCollection();
        }
    }

    private void optimizeChunkLoading() {
        for (World world : Bukkit.getWorlds()) {
            int chunkCount = world.getLoadedChunks().length;
            if (chunkCount > 1000) {
                for (org.bukkit.Chunk chunk : world.getLoadedChunks()) {
                    if (!chunk.isSlimeChunk() && chunk.getEntities().length == 0) {
                        chunk.unload();
                    }
                }
            }
        }
    }

    private void updateMetrics() {
        metrics.recordTick();
        double tps = calculateTPS();
        metrics.recordTPS(tps);
    }

    private double calculateTPS() {
        try {
            Object server = Bukkit.getServer();
            Object recentTPS = server.getClass().getMethod("getRecentTps", double[].class).invoke(server);
            if (recentTPS instanceof double[]) {
                double[] tpsArray = (double[]) recentTPS;
                return tpsArray.length > 0 ? tpsArray[0] : 20.0;
            }
        } catch (Exception e) {
            return 20.0;
        }
        return 20.0;
    }

    public PerformanceMetrics getMetrics() {
        return metrics;
    }

    public double getCurrentTPS() {
        return metrics.getCurrentTPS();
    }

    public long getCurrentMemoryUsage() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }
}
