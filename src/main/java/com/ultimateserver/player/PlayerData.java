package com.ultimateserver.player;

import java.util.*;

public class PlayerData {
    private final String playerId;
    private final String playerName;
    private double balance;
    private Set<String> ranks;
    private long lastLogin;
    private int playtime;
    private int warnings;

    public PlayerData(String playerId, String playerName, double balance) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.balance = balance;
        this.ranks = new HashSet<>();
        this.ranks.add("user");
        this.lastLogin = System.currentTimeMillis();
        this.playtime = 0;
        this.warnings = 0;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addBalance(double amount) {
        this.balance += amount;
    }

    public void removeBalance(double amount) {
        this.balance = Math.max(0, this.balance - amount);
    }

    public Set<String> getRanks() {
        return new HashSet<>(ranks);
    }

    public void addRank(String rank) {
        ranks.add(rank);
    }

    public void removeRank(String rank) {
        ranks.remove(rank);
    }

    public boolean hasRank(String rank) {
        return ranks.contains(rank);
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getPlaytime() {
        return playtime;
    }

    public void addPlaytime(int minutes) {
        this.playtime += minutes;
    }

    public int getWarnings() {
        return warnings;
    }

    public void addWarning() {
        this.warnings++;
    }

    public void clearWarnings() {
        this.warnings = 0;
    }
}
