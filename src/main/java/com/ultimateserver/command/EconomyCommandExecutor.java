package com.ultimateserver.command;

import com.ultimateserver.UltimateServerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyCommandExecutor implements CommandExecutor {
    private final UltimateServerPlugin plugin;

    public EconomyCommandExecutor(UltimateServerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendEconomyHelp(sender);
            return true;
        }

        String subcommand = args[0];

        switch (subcommand) {
            case "balance":
                checkBalance(sender, args);
                break;
            case "pay":
                payPlayer(sender, args);
                break;
            case "give":
                if (sender.hasPermission("ultimateserver.admin")) {
                    giveBalance(sender, args);
                } else {
                    sender.sendMessage("§cYou don't have permission!");
                }
                break;
            case "top":
                showTop(sender);
                break;
            default:
                sender.sendMessage("§cUnknown command!");
                break;
        }

        return true;
    }

    private void sendEconomyHelp(CommandSender sender) {
        sender.sendMessage("§6=== Economy Commands ===");
        sender.sendMessage("§e/economy balance [player] §7- Check balance");
        sender.sendMessage("§e/economy pay <player> <amount> §7- Transfer money");
        sender.sendMessage("§e/economy top §7- Show richest players");
        if (sender.hasPermission("ultimateserver.admin")) {
            sender.sendMessage("§e/economy give <player> <amount> §7- Give money");
        }
    }

    private void checkBalance(CommandSender sender, String[] args) {
        Player target = null;

        if (args.length > 1) {
            target = Bukkit.getPlayer(args[1]);
            if (!sender.hasPermission("ultimateserver.admin") && sender != target) {
                sender.sendMessage("§cYou can only check your own balance!");
                return;
            }
        } else if (sender instanceof Player) {
            target = (Player) sender;
        }

        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return;
        }

        String playerId = target.getUniqueId().toString();
        double balance = plugin.getEconomyManager().getBalance(playerId);
        sender.sendMessage("§6" + target.getName() + "'s Balance: §a$" + String.format("%.2f", balance));
    }

    private void payPlayer(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return;
        }

        if (args.length < 3) {
            sender.sendMessage("§cUsage: /economy pay <player> <amount>");
            return;
        }

        Player payer = (Player) sender;
        Player payee = Bukkit.getPlayer(args[1]);

        if (payee == null) {
            sender.sendMessage("§cPlayer not found!");
            return;
        }

        try {
            double amount = Double.parseDouble(args[2]);
            if (amount <= 0) {
                sender.sendMessage("§cAmount must be positive!");
                return;
            }

            String payerId = payer.getUniqueId().toString();
            String payeeId = payee.getUniqueId().toString();

            if (plugin.getEconomyManager().transferMoney(payerId, payeeId, amount)) {
                sender.sendMessage("§aTransferred §e$" + String.format("%.2f", amount) + "§a to §e" + payee.getName());
                payee.sendMessage("§aReceived §e$" + String.format("%.2f", amount) + "§a from §e" + payer.getName());
            } else {
                sender.sendMessage("§cInsufficient balance!");
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid amount!");
        }
    }

    private void giveBalance(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /economy give <player> <amount>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return;
        }

        try {
            double amount = Double.parseDouble(args[2]);
            String playerId = target.getUniqueId().toString();
            plugin.getEconomyManager().depositMoney(playerId, amount);
            sender.sendMessage("§aGave §e$" + String.format("%.2f", amount) + "§a to §e" + target.getName());
            target.sendMessage("§aYou received §e$" + String.format("%.2f", amount));
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid amount!");
        }
    }

    private void showTop(CommandSender sender) {
        sender.sendMessage("§6=== Top 10 Richest Players ===");
        sender.sendMessage("§eRanking coming soon!");
    }
}
