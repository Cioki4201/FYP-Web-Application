package com.paul.fyp.repository;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.paul.fyp.models.IGDBRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
public interface IGDBRepo {
    JSONArray gameSearch(String searchQuery) throws UnirestException;

    String getCoverArt(String artID) throws UnirestException;

    JSONObject getGameInfo(String gameID) throws UnirestException;

}
