package com.paul.fyp.models;


import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.paul.fyp.repository.IGDBRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.io.*;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;

@Data
@NoArgsConstructor
@Component
public class IGDBRequest {

    private String clientID;
    private String accessToken;

    // CONSTRUCTOR
    public IGDBRequest(String clientID, String accessToken) {
        this.clientID = clientID;
        this.accessToken = accessToken;
    }

    // METHODS
    public JSONArray post(String query, String endpoint) throws UnirestException {

        com.mashape.unirest.http.HttpResponse
                <com.mashape.unirest.http.JsonNode> response = Unirest.post("https://api.igdb.com/v4/" + endpoint)
                .header("Content-Type", "text/plain")
                .header("User-Agent", "Java client")
                .header("Client-ID", this.clientID)
                .header("Authorization", this.accessToken)
                .header("Accept", "application/json")

                .body(query)
                .asJson();

        String jsonString = response.getBody().toString();

        return new JSONArray(jsonString);
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
