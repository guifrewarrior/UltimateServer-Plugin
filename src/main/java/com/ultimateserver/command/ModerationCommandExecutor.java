package com.ultimateserver.command;

import com.ultimateserver.UltimateServerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModerationCommandExecutor implements CommandExecutor {
    private final UltimateServerPlugin plugin;

    public ModerationCommandExecutor(UltimateServerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ultimateserver.moderation")) {
            sender.sendMessage("§cYou don't have permission!");
            return true;
        }

        if (args.length == 0) {
            sendModerationHelp(sender);
            return true;
        }

        String subcommand = args[0];

        switch (subcommand) {
            case "mute":
                mutePlayer(sender, args);
                break;
            case "unmute":
                unmutePlayer(sender, args);
                break;
            case "warn":
                warnPlayer(sender, args);
                break;
            case "kick":
                kickPlayer(sender, args);
                break;
            case "filter":
                manageFilter(sender, args);
                break;
            default:
                sender.sendMessage("§cUnknown command!");
                break;
        }

        return true;
    }

    private void sendModerationHelp(CommandSender sender) {
        sender.sendMessage("§6=== Moderation Commands ===");
        sender.sendMessage("§e/moderation mute <player> [duration] [reason] §7- Mute player");
        sender.sendMessage("§e/moderation unmute <player> §7- Unmute player");
        sender.sendMessage("§e/moderation warn <player> <reason> §7- Warn player");
        sender.sendMessage("§e/moderation kick <player> [reason] §7- Kick player");
        sender.sendMessage("§e/moderation filter <add|remove|list> [word] §7- Manage bad words");
    }

    private void mutePlayer(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /moderation mute <player> [duration] [reason]");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return;
        }

        long duration = 3600;
        if (args.length > 2) {
            try {
                duration = Long.parseLong(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInvalid duration!");
                return;
            }
        }

        String reason = args.length > 3 ? args[3] : "No reason provided";

        String playerId = target.getUniqueId().toString();
        plugin.getModerationManager().mutePlayer(playerId, duration, reason);

        target.sendMessage("§cYou have been muted for " + duration + " seconds!");
        sender.sendMessage("§a" + target.getName() + " has been muted!");
    }

    private void unmutePlayer(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /moderation unmute <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return;
        }

        String playerId = target.getUniqueId().toString();
        plugin.getModerationManager().unmutePlayer(playerId);

        target.sendMessage("§aYou have been unmuted!");
        sender.sendMessage("§a" + target.getName() + " has been unmuted!");
    }

    private void warnPlayer(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /moderation warn <player> <reason>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return;
        }

        String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));
        String playerId = target.getUniqueId().toString();

        plugin.getModerationManager().warnPlayer(playerId, reason);
        target.sendMessage("§cYou have been warned: " + reason);
        sender.sendMessage("§a" + target.getName() + " has been warned!");
    }

    private void kickPlayer(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /moderation kick <player> [reason]");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return;
        }

        String reason = args.length > 2 ? String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length)) : "No reason provided";
        target.kickPlayer("§cYou have been kicked!\n§eReason: §f" + reason);
        sender.sendMessage("§a" + target.getName() + " has been kicked!");
    }

    private void manageFilter(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /moderation filter <add|remove|list> [word]");
            return;
        }

        String action = args[1];

        if (action.equals("list")) {
            var words = plugin.getModerationManager().getBannedWords();
            if (words.isEmpty()) {
                sender.sendMessage("§eNo banned words!");
            } else {
                sender.sendMessage("§6=== Banned Words ===");
                for (String word : words) {
                    sender.sendMessage("§e" + word);
                }
            }
        } else if (action.equals("add")) {
            if (args.length < 3) {
                sender.sendMessage("§cUsage: /moderation filter add <word>");
                return;
            }
            plugin.getModerationManager().addBannedWord(args[2]);
            sender.sendMessage("§aAdded word to filter!");
        } else if (action.equals("remove")) {
            if (args.length < 3) {
                sender.sendMessage("§cUsage: /moderation filter remove <word>");
                return;
            }
            plugin.getModerationManager().removeBannedWord(args[2]);
            sender.sendMessage("§aRemoved word from filter!");
        }
    }
}
