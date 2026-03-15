package com.ultimateserver.economy;

import com.ultimateserver.UltimateServerPlugin;
import com.ultimateserver.player.PlayerData;

public class EconomyManager {
    private final UltimateServerPlugin plugin;

    public EconomyManager(UltimateServerPlugin plugin) {
        this.plugin = plugin;
    }

    public void depositMoney(String playerId, double amount) {
        plugin.getUltimateServerPlugin().getPlayerManager().addBalance(playerId, amount);
    }

    public void withdrawMoney(String playerId, double amount) {
        plugin.getUltimateServerPlugin().getPlayerManager().removeBalance(playerId, amount);
    }

    public double getBalance(String playerId) {
        return plugin.getUltimateServerPlugin().getPlayerManager().getBalance(playerId);
    }

    public boolean hasEnoughMoney(String playerId, double amount) {
        return getBalance(playerId) >= amount;
    }

    public boolean transferMoney(String fromPlayerId, String toPlayerId, double amount) {
        if (hasEnoughMoney(fromPlayerId, amount)) {
            withdrawMoney(fromPlayerId, amount);
            depositMoney(toPlayerId, amount);
            return true;
        }
        return false;
    }

    public void setBalance(String playerId, double balance) {
        double currentBalance = getBalance(playerId);
        if (balance > currentBalance) {
            depositMoney(playerId, balance - currentBalance);
        } else if (balance < currentBalance) {
            withdrawMoney(playerId, currentBalance - balance);
        }
    }
}
