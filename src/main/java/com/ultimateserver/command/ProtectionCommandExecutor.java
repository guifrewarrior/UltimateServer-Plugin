package com.ultimateserver.command;

import com.ultimateserver.UltimateServerPlugin;
import com.ultimateserver.protection.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class ProtectionCommandExecutor implements CommandExecutor {
    private final UltimateServerPlugin plugin;
    private Map<String, Location[]> selectionPositions = new HashMap<>();

    public ProtectionCommandExecutor(UltimateServerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendProtectionHelp(sender);
            return true;
        }

        String subcommand = args[0];

        switch (subcommand) {
            case "pos1":
                setPos1(player);
                break;
            case "pos2":
                setPos2(player);
                break;
            case "create":
                createRegion(player, args);
                break;
            case "delete":
                deleteRegion(player, args);
                break;
            case "list":
                listRegions(player);
                break;
            case "info":
                showInfo(player);
                break;
            case "member":
                manageMember(player, args);
                break;
            default:
                sender.sendMessage("§cUnknown command!");
                break;
        }

        return true;
    }

    private void sendProtectionHelp(CommandSender sender) {
        sender.sendMessage("§6=== Protection Commands ===");
        sender.sendMessage("§e/protect pos1 §7- Set first position");
        sender.sendMessage("§e/protect pos2 §7- Set second position");
        sender.sendMessage("§e/protect create <name> §7- Create region");
        sender.sendMessage("§e/protect list §7- List your regions");
        sender.sendMessage("§e/protect info §7- Get region info");
        sender.sendMessage("§e/protect member add <player> §7- Add member");
        sender.sendMessage("§e/protect member remove <player> §7- Remove member");
    }

    private void setPos1(Player player) {
        Location location = player.getLocation();
        Location[] positions = selectionPositions.getOrDefault(player.getUniqueId().toString(), new Location[2]);
        positions[0] = location;
        selectionPositions.put(player.getUniqueId().toString(), positions);
        player.sendMessage("§aPosition 1 set to " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
    }

    private void setPos2(Player player) {
        Location location = player.getLocation();
        Location[] positions = selectionPositions.getOrDefault(player.getUniqueId().toString(), new Location[2]);
        positions[1] = location;
        selectionPositions.put(player.getUniqueId().toString(), positions);
        player.sendMessage("§aPosition 2 set to " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
    }

    private void createRegion(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /protect create <name>");
            return;
        }

        Location[] positions = selectionPositions.get(player.getUniqueId().toString());
        if (positions == null || positions[0] == null || positions[1] == null) {
            player.sendMessage("§cYou must select two positions first!");
            return;
        }

        String playerId = player.getUniqueId().toString();
        Region region = plugin.getProtectionManager().createRegion(playerId, positions[0], positions[1]);

        player.sendMessage("§aRegion created successfully!");
        player.sendMessage("§eRegion ID: §a" + region.getRegionId());
        player.sendMessage("§eArea: §a" + region.getArea() + " blocks");
    }

    private void deleteRegion(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /protect delete <region-id>");
            return;
        }

        String regionId = args[1];
        Region region = plugin.getProtectionManager().getRegion(regionId);

        if (region == null) {
            player.sendMessage("§cRegion not found!");
            return;
        }

        if (!region.getOwnerId().equals(player.getUniqueId().toString()) && !player.hasPermission("ultimateserver.admin")) {
            player.sendMessage("§cYou don't own this region!");
            return;
        }

        plugin.getProtectionManager().deleteRegion(regionId);
        player.sendMessage("§aRegion deleted!");
    }

    private void listRegions(Player player) {
        String playerId = player.getUniqueId().toString();
        var regions = plugin.getProtectionManager().getRegionsOwnedBy(playerId);

        if (regions.isEmpty()) {
            player.sendMessage("§eYou don't own any regions!");
            return;
        }

        player.sendMessage("§6=== Your Regions ===");
        for (Region region : regions) {
            player.sendMessage("§e" + region.getRegionId() + " §7- §a" + region.getArea() + " blocks");
        }
    }

    private void showInfo(Player player) {
        Region region = plugin.getProtectionManager().getRegionAtLocation(player.getLocation());

        if (region == null) {
            player.sendMessage("§cYou are not in a protected region!");
            return;
        }

        player.sendMessage("§6=== Region Info ===");
        player.sendMessage("§eID: §a" + region.getRegionId());
        player.sendMessage("§eOwner: §a" + Bukkit.getOfflinePlayer(UUID.fromString(region.getOwnerId())).getName());
        player.sendMessage("§eArea: §a" + region.getArea() + " blocks");
        player.sendMessage("§eMembers: §a" + region.getMembers().size());
    }

    private void manageMember(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage("§cUsage: /protect member <add|remove> <player>");
            return;
        }

        Region region = plugin.getProtectionManager().getRegionAtLocation(player.getLocation());
        if (region == null || !region.getOwnerId().equals(player.getUniqueId().toString())) {
            player.sendMessage("§cYou must be in your own region!");
            return;
        }

        String action = args[1];
        Player targetPlayer = Bukkit.getPlayer(args[2]);

        if (targetPlayer == null) {
            player.sendMessage("§cPlayer not found!");
            return;
        }

        String memberId = targetPlayer.getUniqueId().toString();

        if (action.equals("add")) {
            region.addMember(memberId);
            player.sendMessage("§aAdded " + targetPlayer.getName() + " to region!");
        } else if (action.equals("remove")) {
            region.removeMember(memberId);
            player.sendMessage("§aRemoved " + targetPlayer.getName() + " from region!");
        }
    }
}
