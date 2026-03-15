package com.ultimateserver.moderation;

public class MuteData {
    private final String playerId;
    private final String reason;
    private final long mutedAt;
    private final long durationSeconds;

    public MuteData(String playerId, String reason, long durationSeconds) {
        this.playerId = playerId;
        this.reason = reason;
        this.mutedAt = System.currentTimeMillis();
        this.durationSeconds = durationSeconds;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getReason() {
        return reason;
    }

    public long getMutedAt() {
        return mutedAt;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public long getRemainingSeconds() {
        long elapsed = (System.currentTimeMillis() - mutedAt) / 1000;
        return Math.max(0, durationSeconds - elapsed);
    }

    public boolean isExpired() {
        return getRemainingSeconds() <= 0;
    }
}
