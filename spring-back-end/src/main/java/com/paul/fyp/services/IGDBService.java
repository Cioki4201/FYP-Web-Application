package com.paul.fyp.services;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.paul.fyp.models.IGDBRequest;
import com.paul.fyp.repository.IGDBRepo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

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

        // PLATFORMS

        JSONArray platforms = gameInfo.getJSONArray("platforms");
        JSONArray platformNames = new JSONArray();
        for (Object platform : platforms) {
            String platformQuery = "where id = " + platform.toString() + "; fields name;";
            JSONArray platformInfo = request.post(platformQuery, "platforms");
            JSONObject platformObj = new JSONObject(platformInfo.get(0).toString());
            platformNames.put(platformObj.get("name"));
        }
        res.put("platforms", platformNames);

        // RELEASE DATE

        long unixTime = Long.parseLong(gameInfo.get("first_release_date").toString());
        java.util.Date time = new java.util.Date(unixTime * 1000);
        String releaseDate = new java.text.SimpleDateFormat("dd/MM/yyyy").format(time);
        res.put("releaseDate", releaseDate);

        // SIMILAR GAMES

        JSONArray similarGames = gameInfo.getJSONArray("similar_games");
        JSONArray similarGameNames = new JSONArray();
        for (Object similarGame : similarGames) {
            String similarGameQuery = "where id = " + similarGame.toString() + "; fields name;";
            JSONArray similarGameInfo = request.post(similarGameQuery, "games");
            JSONObject similarGameObj = new JSONObject(similarGameInfo.get(0).toString());
            similarGameNames.put(similarGameObj);
        }
        res.put("similarGames", similarGameNames);

        // WEBSITES

        JSONArray websites = gameInfo.getJSONArray("websites");
        JSONArray websiteUrls = new JSONArray();
        for (Object website : websites) {
            String websiteQuery = "where id = " + website.toString() + "; fields url;";
            JSONArray websiteInfo = request.post(websiteQuery, "websites");
            JSONObject websiteObj = new JSONObject(websiteInfo.get(0).toString());
            websiteUrls.put(websiteObj.get("url"));
        }
        res.put("websites", websiteUrls);

        return res;
    }
}
