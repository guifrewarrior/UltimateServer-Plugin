package com.ultimateserver.database;

import com.ultimateserver.config.ConfigManager;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class DatabaseManager {
    private final ConfigManager configManager;
    private final HttpClient httpClient;
    private final String supabaseUrl;
    private final String anonKey;

    public DatabaseManager(ConfigManager configManager) throws Exception {
        this.configManager = configManager;
        this.supabaseUrl = configManager.getDatabase();
        this.anonKey = configManager.getDatabaseAnonKey();
        this.httpClient = HttpClient.newHttpClient();

        if (supabaseUrl.isEmpty() || anonKey.isEmpty()) {
            throw new RuntimeException("Database credentials not configured in config.yml");
        }
    }

    public void insertPlayerData(String playerId, String playerName, double balance) throws Exception {
        String url = supabaseUrl + "/rest/v1/players";

        JsonObject body = new JsonObject();
        body.addProperty("player_id", playerId);
        body.addProperty("player_name", playerName);
        body.addProperty("balance", balance);
        body.addProperty("created_at", System.currentTimeMillis());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Bearer " + anonKey)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=minimal")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.discarding());
    }

    public double getPlayerBalance(String playerId) throws Exception {
        String url = supabaseUrl + "/rest/v1/players?player_id=eq." + playerId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Bearer " + anonKey)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.body().startsWith("[") && response.body().length() > 2) {
            return 1000.0;
        }
        return 0.0;
    }

    public void updatePlayerBalance(String playerId, double balance) throws Exception {
        String url = supabaseUrl + "/rest/v1/players?player_id=eq." + playerId;

        JsonObject body = new JsonObject();
        body.addProperty("balance", balance);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Bearer " + anonKey)
                .header("Content-Type", "application/json")
                .PATCH(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.discarding());
    }

    public void insertWarning(String playerId, String reason) throws Exception {
        String url = supabaseUrl + "/rest/v1/warnings";

        JsonObject body = new JsonObject();
        body.addProperty("player_id", playerId);
        body.addProperty("reason", reason);
        body.addProperty("created_at", System.currentTimeMillis());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Bearer " + anonKey)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=minimal")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.discarding());
    }

    public void insertClaimData(String claimId, String playerId, int x1, int z1, int x2, int z2) throws Exception {
        String url = supabaseUrl + "/rest/v1/claims";

        JsonObject body = new JsonObject();
        body.addProperty("claim_id", claimId);
        body.addProperty("player_id", playerId);
        body.addProperty("x1", x1);
        body.addProperty("z1", z1);
        body.addProperty("x2", x2);
        body.addProperty("z2", z2);
        body.addProperty("created_at", System.currentTimeMillis());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Bearer " + anonKey)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=minimal")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.discarding());
    }

    public void close() {
    }
}
