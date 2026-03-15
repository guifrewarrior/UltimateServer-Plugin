package com.ultimateserver.optimization;

import java.util.LinkedList;
import java.util.Queue;

public class PerformanceMetrics {
    private long tickCount = 0;
    private long gcCount = 0;
    private Queue<Double> tpsHistory;
    private final int maxHistorySize = 100;

    public PerformanceMetrics() {
        this.tpsHistory = new LinkedList<>();
    }

    public void recordTick() {
        tickCount++;
    }

    public void recordGarbageCollection() {
        gcCount++;
    }

    public void recordTPS(double tps) {
        if (tpsHistory.size() >= maxHistorySize) {
            tpsHistory.poll();
        }
        tpsHistory.offer(tps);
    }

    public long getTickCount() {
        return tickCount;
    }

    public long getGCCount() {
        return gcCount;
    }

    public double getCurrentTPS() {
        if (tpsHistory.isEmpty()) {
            return 20.0;
        }
        return tpsHistory.peek();
    }

    public double getAverageTPS() {
        if (tpsHistory.isEmpty()) {
            return 20.0;
        }
        return tpsHistory.stream().mapToDouble(Double::doubleValue).average().orElse(20.0);
    }

    public void reset() {
        tickCount = 0;
        gcCount = 0;
        tpsHistory.clear();
    }
}
