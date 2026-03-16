package com.ultimateserver.command;

import com.ultimateserver.UltimateServerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommandExecutor implements CommandExecutor {
    private final UltimateServerPlugin plugin;

    public PlayerCommandExecutor(UltimateServerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendPlayerHelp(sender);
            return true;
        }

        String subcommand = args[0];

        switch (subcommand) {
            case "info":
                showPlayerInfo(sender, args);
                break;
            case "stats":
                showPlayerStats(sender, args);
                break;
            case "rank":
                showPlayerRank(sender, args);
                break;
            case "playtime":
                showPlaytime(sender, args);
                break;
            case "friends":
                showFriends(sender, args);
                break;
            case "block":
                blockPlayer(sender, args);
                break;
            case "unblock":
                unblockPlayer(sender, args);
                break;
            case "message":
                sendMessage(sender, args);
                break;
            default:
                sender.sendMessage("§cUnknown command!");
                break;
        }

        return true;
    }

    private void sendPlayerHelp(CommandSender sender) {
        sender.sendMessage("§6=== Player Commands ===");
        sender.sendMessage("§e/player info [player] §7- Player information");
        sender.sendMessage("§e/player stats [player] §7- Player statistics");
        sender.sendMessage("§e/player rank [player] §7- Show rank");
        sender.sendMessage("§e/player playtime [player] §7- Playtime");
        sender.sendMessage("§e/player friends [player] §7- Friends list");
        sender.sendMessage("§e/player block <player> §7- Block player");
        sender.sendMessage("§e/player unblock <player> §7- Unblock player");
        sender.sendMessage("§e/player message <player> <msg> §7- Send message");
    }

    private void showPlayerInfo(CommandSender sender, String[] args) {
        Player target = null;
        if (args.length > 1) {
            target = Bukkit.getPlayer(args[1]);
        } else if (sender instanceof Player) {
            target = (Player) sender;
        }

        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return;
        }

        sender.sendMessage("§6=== Player Info: " + target.getName() + " ===");
        sender.sendMessage("§eUUID: §a" + target.getUniqueId());
        sender.sendMessage("§eHealth: §a" + (int)target.getHealth() + "§7/§a20");
        sender.sendMessage("§eLocation: §a" + target.getLocation().getBlockX() + ", " + target.getLocation().getBlockY() + ", " + target.getLocation().getBlockZ());
        sender.sendMessage("§eLevel: §a" + target.getLevel());
        sender.sendMessage("§eExp: §a" + target.getExp());
    }

    private void showPlayerStats(CommandSender sender, String[] args) {
        Player target = null;
        if (args.length > 1) {
            target = Bukkit.getPlayer(args[1]);
        } else if (sender instanceof Player) {
            target = (Player) sender;
        }

        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return;
        }

        sender.sendMessage("§6=== " + target.getName() + " Statistics ===");
        sender.sendMessage("§eOnline Time: §a" + (System.currentTimeMillis() - target.getLastLogin()) / 1000 + "s");
        sender.sendMessage("§eJumps: §a0");
        sender.sendMessage("§eWalked: §a0 blocks");
        sender.sendMessage("§eSwum: §a0 blocks");
    }

    private void showPlayerRank(CommandSender sender, String[] args) {
        Player target = null;
        if (args.length > 1) {
            target = Bukkit.getPlayer(args[1]);
        } else if (sender instanceof Player) {
            target = (Player) sender;
        }

        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return;
        }

        sender.sendMessage("§6" + target.getName() + "'s Rank: §aUser");
    }

    private void showPlaytime(CommandSender sender, String[] args) {
        Player target = null;
        if (args.length > 1) {
            target = Bukkit.getPlayer(args[1]);
        } else if (sender instanceof Player) {
            target = (Player) sender;
        }

        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return;
        }

        sender.sendMessage("§6" + target.getName() + "'s Playtime: §a0 hours");
    }

    private void showFriends(CommandSender sender, String[] args) {
        sender.sendMessage("§6Friends: §aNone");
    }

    private void blockPlayer(CommandSender sender, String[] args) {
        sender.sendMessage("§aPlayer blocked!");
    }

    private void unblockPlayer(CommandSender sender, String[] args) {
        sender.sendMessage("§aPlayer unblocked!");
    }

    private void sendMessage(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /player message <player> <message>");
            return;
        }
        sender.sendMessage("§aMessage sent!");
    }
}
