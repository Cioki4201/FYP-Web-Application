package com.paul.fyp.repository;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.paul.fyp.models.IGDBRequest;
import com.paul.fyp.models.dto.CoverIDsDTO;
import com.paul.fyp.models.dto.GamesIdsDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface IGDBRepo {
    JSONArray gameSearch(String searchQuery) throws UnirestException;

    ResponseEntity<String> getCoverArt(CoverIDsDTO coverIDsDTO) throws UnirestException;

    JSONObject getGameInfo(String gameID) throws UnirestException;

    ResponseEntity<String> getRawGameData(GamesIdsDTO gameID) throws UnirestException;

    ResponseEntity<String> getGameNameCoverAndId(GamesIdsDTO gameIDs) throws UnirestException;


}
