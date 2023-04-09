package com.paul.fyp.controllers;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.paul.fyp.models.dto.CoverIDsDTO;
import com.paul.fyp.models.dto.GamesIdsDTO;
import com.paul.fyp.services.IGDBService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/igdb")
public class IGDBController {

    @Autowired
    IGDBService igdbService;

    // SEARCH DATABASE FOR GAME BY STRING
    @GetMapping("/search/{searchQuery}")
    public String gameSearch(@PathVariable("searchQuery") String searchQuery) throws UnirestException {
        JSONArray searchResult = igdbService.gameSearch(searchQuery);

        return searchResult.toString();
    }

    // RETURNS THE GAME COVER ART FOR A GIVEN COVER ID
    @PostMapping("/cover")
    public ResponseEntity<String> getCover(@RequestBody CoverIDsDTO coverIDsDTO) throws UnirestException {
        return igdbService.getCoverArt(coverIDsDTO);
    }

    // RETURNS ALL INFORMATION FOR A GIVEN GAME ID
    @GetMapping("/game/{game_id}")
    public String getGameInfo(@PathVariable("game_id") String gameID) throws UnirestException {
        JSONObject gameInfo = igdbService.getGameInfo(gameID);

        return gameInfo.toString();
    }

    // RETURNS RAW GAME DATA FOR A LIST OF GAME IDS
    @PostMapping("/rawGameData")
    public ResponseEntity<String> getRawGameData(@RequestBody GamesIdsDTO gamesIdsDTO) throws UnirestException {
        return igdbService.getRawGameData(gamesIdsDTO);
    }

    // RETURNS RAW GAME DATA FOR A LIST OF GAME IDS
    @PostMapping("/coverAndNames")
    public ResponseEntity<String> getCoverAndNames(@RequestBody GamesIdsDTO gamesIdsDTO) throws UnirestException {
        return igdbService.getGameNameCoverAndId(gamesIdsDTO);
    }
}
