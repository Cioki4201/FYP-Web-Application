package com.paul.fyp.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameRecommender {

    public List<Integer> recommendGames(List<Integer> userGames) throws IOException, InterruptedException {
        JSONArray userGamesJsonArray = new JSONArray(userGames);
        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.put("user_games", userGamesJsonArray);
        String requestBody = requestBodyJson.toString();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5000/recommend_games"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        String responseBody = httpResponse.body();

        JSONArray jsonArray = new JSONArray(responseBody); // Parse the response body as a JSONArray
        List<Integer> recommendedGameIds = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            recommendedGameIds.add(jsonArray.getInt(i));
        }

        return recommendedGameIds;
    }
}