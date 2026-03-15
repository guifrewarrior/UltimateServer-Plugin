package com.ultimateserver.protection;

import com.ultimateserver.UltimateServerPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class RegionManager {
    private final UltimateServerPlugin plugin;
    private Map<String, Region> regions;
    private int regionCounter = 0;

    public RegionManager(UltimateServerPlugin plugin) {
        this.plugin = plugin;
        this.regions = new HashMap<>();
    }

    public Region createRegion(String ownerId, Location pos1, Location pos2) {
        String regionId = "region_" + (regionCounter++);
        Region region = new Region(regionId, ownerId, pos1, pos2);
        regions.put(regionId, region);

        try {
            int x1 = Math.min(pos1.getBlockX(), pos2.getBlockX());
            int z1 = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
            int x2 = Math.max(pos1.getBlockX(), pos2.getBlockX());
            int z2 = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

            plugin.getDatabaseManager().insertClaimData(regionId, ownerId, x1, z1, x2, z2);
        } catch (Exception e) {
            plugin.getLogger().warning("Could not save region to database: " + e.getMessage());
        }

        return region;
    }

    public Region getRegion(String regionId) {
        return regions.get(regionId);
    }

    public Region getRegionAtLocation(Location location) {
        for (Region region : regions.values()) {
            if (region.contains(location)) {
                return region;
            }
        }
        return null;
    }

    public List<Region> getRegionsOwnedBy(String playerId) {
        List<Region> ownedRegions = new ArrayList<>();
        for (Region region : regions.values()) {
            if (region.getOwnerId().equals(playerId)) {
                ownedRegions.add(region);
            }
        }
        return ownedRegions;
    }

    public void deleteRegion(String regionId) {
        regions.remove(regionId);
    }

    public boolean canBuildAt(Location location, Player player) {
        Region region = getRegionAtLocation(location);
        if (region == null) {
            return true;
        }
        return region.canAccess(player.getUniqueId().toString());
    }

    public void addMember(String regionId, String memberId) {
        Region region = regions.get(regionId);
        if (region != null) {
            region.addMember(memberId);
        }
    }

    public void removeMember(String regionId, String memberId) {
        Region region = regions.get(regionId);
        if (region != null) {
            region.removeMember(memberId);
        }
    }

    public Collection<Region> getAllRegions() {
        return regions.values();
    }
}
