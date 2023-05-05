package com.paul.fyp.models;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Service
public class AccessTokenRetriever {

    private static final String URL = "https://id.twitch.tv/oauth2/token?client_id=h8e4zy4n8gdygc69d4kfz4qsn9ymn3&client_secret=8lcsfnr06erilsvkwmh76tn4ncjcyo&grant_type=client_credentials";
    private String accessToken;
    private final List<Consumer<String>> accessTokenObservers = new ArrayList<>();

    public void addObserver(Consumer<String> observer) {
        accessTokenObservers.add(observer);
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void retrieveAccessToken() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String jsonResponse = restTemplate.postForObject(URL, null, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            accessToken = "Bearer " + rootNode.get("access_token").asText();
            System.out.println("Access token retrieved: " + accessToken);

            // Notify observers
            for (Consumer<String> observer : accessTokenObservers) {
                observer.accept(accessToken);
            }
        } catch (HttpClientErrorException | IOException ex) {
            System.out.println("Error while retrieving access token: " + ex.getMessage());
        }
    }

    @Async
    public CompletableFuture<String> getAccessToken() {
        if (accessToken == null) {
            retrieveAccessToken();
        }
        return CompletableFuture.completedFuture(accessToken);
    }
}
