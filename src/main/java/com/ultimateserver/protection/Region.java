package com.ultimateserver.protection;

import org.bukkit.Location;

import java.util.*;

public class Region {
    private final String regionId;
    private final String ownerId;
    private final Location pos1;
    private final Location pos2;
    private Set<String> members;
    private long createdAt;

    public Region(String regionId, String ownerId, Location pos1, Location pos2) {
        this.regionId = regionId;
        this.ownerId = ownerId;
        this.pos1 = pos1.clone();
        this.pos2 = pos2.clone();
        this.members = new HashSet<>();
        this.members.add(ownerId);
        this.createdAt = System.currentTimeMillis();
    }

    public String getRegionId() {
        return regionId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Location getPos1() {
        return pos1.clone();
    }

    public Location getPos2() {
        return pos2.clone();
    }

    public boolean contains(Location location) {
        if (!location.getWorld().equals(pos1.getWorld())) {
            return false;
        }

        int x = location.getBlockX();
        int z = location.getBlockZ();
        int y = location.getBlockY();

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());

        return x >= minX && x <= maxX && z >= minZ && z <= maxZ && y >= minY && y <= maxY;
    }

    public void addMember(String memberId) {
        members.add(memberId);
    }

    public void removeMember(String memberId) {
        if (!memberId.equals(ownerId)) {
            members.remove(memberId);
        }
    }

    public boolean canAccess(String playerId) {
        return members.contains(playerId);
    }

    public Set<String> getMembers() {
        return new HashSet<>(members);
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getArea() {
        int width = Math.abs(pos1.getBlockX() - pos2.getBlockX()) + 1;
        int depth = Math.abs(pos1.getBlockZ() - pos2.getBlockZ()) + 1;
        return (long) width * depth;
    }
}
