package com.paul.fyp.services;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.paul.fyp.models.IGDBRequest;
import com.paul.fyp.models.dto.GamesIdsDTO;
import com.paul.fyp.repository.IGDBRepo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class IGDBService implements IGDBRepo {

    IGDBRequest request = new IGDBRequest("h8e4zy4n8gdygc69d4kfz4qsn9ymn3", "Bearer 3x7wa4db826lpwqiithvvvzg77ebkg");

    @Override
    public JSONArray gameSearch(String searchQuery) throws UnirestException {
        searchQuery = "search \"" + searchQuery + "\"; fields name, cover; limit 50;";
        JSONArray returnBody = request.post(searchQuery, "games");

        return returnBody;
    }

    @Override
    public String getCoverArt(String artID) throws UnirestException {
        try {
            String query = "where id = " + artID + "; fields *;";
            JSONArray returnBody = request.post(query, "covers");

            JSONObject artObject = new JSONObject(returnBody.get(0).toString());
            return "https://images.igdb.com/igdb/image/upload/t_cover_big/" + artObject.get("image_id") + ".jpg";
        } catch (Exception e) { // if no cover art is found return default image
            return "https://publications.iarc.fr/uploads/media/default/0001/02/thumb_1205_default_publication.jpeg";
        }

    }

    @Override
    public JSONObject getGameInfo(String gameID) throws UnirestException {
        String query = "where id = " + gameID + "; fields name, cover, storyline, summary, total_rating, total_rating_count, platforms, first_release_date, websites, similar_games;";
        JSONArray returnBody = request.post(query, "games");
        JSONObject gameInfo = new JSONObject(returnBody.get(0).toString());

        JSONObject res = new JSONObject();


        res.put("id", gameInfo.get("id"));
        res.put("name", gameInfo.get("name"));

        try {
            res.put("storyline", gameInfo.get("storyline"));
        } catch (Exception e) {
            res.put("storyline", "No storyline available");
        }

        try {
            res.put("summary", gameInfo.get("summary"));
        } catch (Exception e) {
            res.put("summary", "No summary available");
        }

        try {
            res.put("rating", gameInfo.get("total_rating"));
        } catch (Exception e) {
            res.put("rating", "No rating available");
        }

        try {
            res.put("rating_count", gameInfo.get("total_rating_count"));
        } catch (Exception e) {
            res.put("rating_count", "No rating count available");
        }

        res.put("coverUrl", getCoverArt(gameInfo.get("cover").toString()));

        // =========================== PLATFORMS ===========================

        CompletableFuture<JSONArray> platformNamesFuture = CompletableFuture.supplyAsync(() -> {
            JSONArray platforms = gameInfo.getJSONArray("platforms");
            JSONArray platformNames = new JSONArray();

            // Create a comma-separated list of platform IDs for a single query
            String platformIds = String.join(",", platforms.toList().stream().map(Object::toString).collect(Collectors.toList()));

            // Modify the query to request information for all platforms at once
            String platformQuery = "where id = (" + platformIds + "); fields name;";

            try {
                JSONArray platformInfo = request.post(platformQuery, "platforms");

                for (int i = 0; i < platformInfo.length(); i++) {
                    JSONObject platformObj = platformInfo.getJSONObject(i);
                    platformNames.put(platformObj.get("name"));
                }
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }

            return platformNames;
        });


        // =============================== RELEASE DATE ===============================

        long unixTime = Long.parseLong(gameInfo.get("first_release_date").toString());
        java.util.Date time = new java.util.Date(unixTime * 1000);
        String releaseDate = new java.text.SimpleDateFormat("dd/MM/yyyy").format(time);
        res.put("releaseDate", releaseDate);

        // =============================== SIMILAR GAMES ===============================

        CompletableFuture<JSONArray> similarGameNamesFuture = CompletableFuture.supplyAsync(() -> {
            JSONArray similarGames = gameInfo.getJSONArray("similar_games");
            JSONArray similarGameNames = new JSONArray();

            // Create a comma-separated list of similar game IDs for a single query
            String similarGameIds = String.join(",", similarGames.toList().stream().map(Object::toString).collect(Collectors.toList()));

            // Modify the query to request information for all similar games at once
            String similarGameQuery = "where id = (" + similarGameIds + "); fields name;";

            try {
                JSONArray similarGameInfo = request.post(similarGameQuery, "games");

                for (int i = 0; i < similarGameInfo.length(); i++) {
                    JSONObject similarGameObj = similarGameInfo.getJSONObject(i);
                    similarGameNames.put(similarGameObj);
                }
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }

            return similarGameNames;
        });

        // =============================== WEBSITES ===============================

        CompletableFuture<JSONArray> websiteUrlsFuture = CompletableFuture.supplyAsync(() -> {
            JSONArray websites = gameInfo.getJSONArray("websites");
            JSONArray websiteUrls = new JSONArray();

            // Create a comma-separated list of website IDs for a single query
            String websiteIds = String.join(",", websites.toList().stream().map(Object::toString).collect(Collectors.toList()));

            // Modify the query to request information for all websites at once
            String websiteQuery = "where id = (" + websiteIds + "); fields url;";

            try {
                JSONArray websiteInfo = request.post(websiteQuery, "websites");

                for (int i = 0; i < websiteInfo.length(); i++) {
                    JSONObject websiteObj = websiteInfo.getJSONObject(i);
                    websiteUrls.put(websiteObj.get("url"));
                }
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }

            return websiteUrls;
        });

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(platformNamesFuture, similarGameNamesFuture, websiteUrlsFuture);
        allFutures.join();

        res.put("platforms", platformNamesFuture.join());
        res.put("similarGames", similarGameNamesFuture.join());
        res.put("websites", websiteUrlsFuture.join());

        return res;
    }

    @Override
    public ResponseEntity<String> getRawGameData(GamesIdsDTO gameIDs) throws UnirestException {
        // get all game ids and store them in a string, divided by commas
        String gameIDsString = String.join(",", gameIDs.getGameIDs());

        String query = "where id = (" + gameIDsString + "); fields name;";

        JSONArray returnBody = request.post(query, "games");

        return new ResponseEntity<>(returnBody.toString(), HttpStatus.OK);
    }
}
