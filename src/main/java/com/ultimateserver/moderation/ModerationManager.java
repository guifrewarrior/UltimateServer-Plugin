package com.ultimateserver.moderation;

import com.ultimateserver.UltimateServerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ModerationManager {
    private final UltimateServerPlugin plugin;
    private Map<String, MuteData> mutedPlayers;
    private Map<String, BukkitTask> muteTimers;
    private List<String> bannedWords;

    public ModerationManager(UltimateServerPlugin plugin) {
        this.plugin = plugin;
        this.mutedPlayers = new HashMap<>();
        this.muteTimers = new HashMap<>();
        this.bannedWords = new ArrayList<>();
        loadBannedWords();
    }

    private void loadBannedWords() {
        bannedWords.add("badword1");
        bannedWords.add("badword2");
        bannedWords.add("badword3");
    }

    public void mutePlayer(String playerId, long durationSeconds, String reason) {
        MuteData muteData = new MuteData(playerId, reason, durationSeconds);
        mutedPlayers.put(playerId, muteData);

        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            unmutePlayer(playerId);
        }, durationSeconds * 20);

        muteTimers.put(playerId, task);

        try {
            plugin.getDatabaseManager().insertWarning(playerId, "MUTED: " + reason);
        } catch (Exception e) {
            plugin.getLogger().warning("Could not log mute to database: " + e.getMessage());
        }
    }

    public void unmutePlayer(String playerId) {
        mutedPlayers.remove(playerId);
        BukkitTask task = muteTimers.remove(playerId);
        if (task != null) {
            task.cancel();
        }
    }

    public boolean isMuted(String playerId) {
        return mutedPlayers.containsKey(playerId);
    }

    public MuteData getMuteData(String playerId) {
        return mutedPlayers.get(playerId);
    }

    public String filterMessage(String message) {
        String filtered = message;
        for (String word : bannedWords) {
            filtered = filtered.replaceAll("(?i)" + word, "*".repeat(word.length()));
        }
        return filtered;
    }

    public boolean containsBannedWords(String message) {
        for (String word : bannedWords) {
            if (message.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void warnPlayer(String playerId, String reason) {
        try {
            plugin.getDatabaseManager().insertWarning(playerId, reason);
            plugin.getPlayer(playerId).ifPresent(data ->
                data.addWarning()
            );
        } catch (Exception e) {
            plugin.getLogger().warning("Could not warn player: " + e.getMessage());
        }
    }

    public void addBannedWord(String word) {
        if (!bannedWords.contains(word.toLowerCase())) {
            bannedWords.add(word.toLowerCase());
        }
    }

    public void removeBannedWord(String word) {
        bannedWords.remove(word.toLowerCase());
    }

    public List<String> getBannedWords() {
        return new ArrayList<>(bannedWords);
    }
}
