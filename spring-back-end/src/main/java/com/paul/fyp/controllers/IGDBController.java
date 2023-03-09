package com.paul.fyp.controllers;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.paul.fyp.services.IGDBService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/cover/{cover_id}")
    public String getCover(@PathVariable("cover_id") String coverID) throws UnirestException {
        return igdbService.getCoverArt(coverID);
    }
}
