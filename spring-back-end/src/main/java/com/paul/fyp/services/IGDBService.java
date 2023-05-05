package com.paul.fyp.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.paul.fyp.models.AccessTokenRetriever;
import com.paul.fyp.models.IGDBRequest;
import com.paul.fyp.models.dto.CoverIDsDTO;
import com.paul.fyp.models.dto.GamesIdsDTO;
import com.paul.fyp.repository.IGDBRepo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class IGDBService implements IGDBRepo {

    @Autowired
    AccessTokenRetriever accessTokenRetriever;

    IGDBRequest request;

    @PostConstruct
    public void init() {
        accessTokenRetriever.addObserver(accessToken -> {
            request = new IGDBRequest("h8e4zy4n8gdygc69d4kfz4qsn9ymn3", accessToken);
        });

        CompletableFuture<String> accessTokenFuture = accessTokenRetriever.getAccessToken();
        accessTokenFuture.thenAccept(accessToken -> {
            if (request == null) {
                request = new IGDBRequest("h8e4zy4n8gdygc69d4kfz4qsn9ymn3", accessToken);
            }
        }).join();
    }
    @Override
    public JSONArray gameSearch(String searchQuery) throws UnirestException {
        searchQuery = "search \"" + searchQuery + "\"; fields name, cover; limit 70;";
        JSONArray returnBody = request.post(searchQuery, "games");

        return returnBody;
    }

    @Override
    public ResponseEntity<String> getCoverArt(CoverIDsDTO coverIDsDTO) throws UnirestException {
        // get all game ids and store them in a string, divided by commas
        String coverIDsString = String.join(",", coverIDsDTO.getCoverIDs());

        try {
            String query = "where id = (" + coverIDsString + "); fields *; limit 70;";
            JSONArray returnBody = request.post(query, "covers");

            // create a new JSONArray to store the processed cover URLs
            JSONArray processedCovers = new JSONArray();

            // iterate through the returnBody and construct the URLs
            for (int i = 0; i < returnBody.length(); i++) {
                JSONObject artObject = returnBody.getJSONObject(i);
                String image_id = artObject.getString("image_id");
                int game_id = artObject.getInt("game");

                // construct the URL
                String url = "https://images.igdb.com/igdb/image/upload/t_cover_big/" + image_id + ".jpg";

                // create a new JSONObject to store the URL and game id
                JSONObject coverInfo = new JSONObject();
                coverInfo.put("game", game_id);
                coverInfo.put("url", url);

                // add the coverInfo JSONObject to the processedCovers JSONArray
                processedCovers.put(coverInfo);
            }

            return new ResponseEntity<>(processedCovers.toString().replace("\\", ""), HttpStatus.OK);
        } catch (Exception e) {
            // if no cover art is found return default image
            return new ResponseEntity<>("https://publications.iarc.fr/uploads/media/default/0001/02/thumb_1205_default_publication.jpeg", HttpStatus.OK);
        }
    }

    @Override
    public JSONObject getGameInfo(String gameID) throws UnirestException {
        String query = "where id = " + gameID + "; fields id, name, storyline, summary, total_rating, total_rating_count, cover, platforms.name, first_release_date, " +
                "websites.url, similar_games.name, similar_games.cover.image_id, genres.name, age_ratings.rating, artworks.image_id, category, screenshots.image_id, videos.video_id;";

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

        String[] coverID = {gameInfo.get("cover").toString()};
        CoverIDsDTO coverIDsDTO = new CoverIDsDTO(coverID);
        res.put("coverUrl", getCoverArt(coverIDsDTO).getBody());

        // =========================== PLATFORMS ===========================
        try {
            JSONArray platformNames = gameInfo.getJSONArray("platforms");
            res.put("platforms", platformNames);
        } catch (Exception e) {
            res.put("platforms", new JSONArray());
        }

        // =============================== RELEASE DATE ===============================
        try {
            long unixTime = Long.parseLong(gameInfo.get("first_release_date").toString());
            java.util.Date time = new java.util.Date(unixTime * 1000);
            String releaseDate = new java.text.SimpleDateFormat("dd/MM/yyyy").format(time);
            res.put("releaseDate", releaseDate);
        } catch (Exception e) {
            res.put("releaseDate", "No release date available");
        }

        // =============================== SIMILAR GAMES ===============================
        try {
            JSONArray similarGameNames = gameInfo.getJSONArray("similar_games");
            res.put("similarGames", similarGameNames);
        } catch (Exception e) {
            res.put("similarGames", new JSONArray());
        }

        // =============================== WEBSITES ===============================
        try {
            JSONArray websiteUrls = gameInfo.getJSONArray("websites");
            res.put("websites", websiteUrls);
        } catch (Exception e) {
            res.put("websites", new JSONArray());
        }

        // =============================== GENRES ===============================
        try {
            JSONArray genres = gameInfo.getJSONArray("genres");
            res.put("genres", genres);
        } catch (Exception e) {
            res.put("genres", new JSONArray());
        }

        // =============================== AGE RATINGS ===============================
        try {
            JSONArray ageRatings = gameInfo.getJSONArray("age_ratings");
            res.put("ageRatings", ageRatings);
        } catch (Exception e) {
            res.put("ageRatings", new JSONArray());
        }

        // =============================== ARTWORKS ===============================
        try {
            JSONArray artworks = gameInfo.getJSONArray("artworks");
            res.put("artworks", artworks);
        } catch (Exception e) {
            res.put("artworks", new JSONArray());
        }

        // =============================== CATEGORY ===============================
        try {
            int category = gameInfo.getInt("category");
            res.put("category", category);
        } catch (Exception e) {
            res.put("category", "No category available");
        }

        // =============================== SCREENSHOTS ===============================
        try {
            JSONArray screenshots = gameInfo.getJSONArray("screenshots");
            res.put("screenshots", screenshots);
        } catch (Exception e) {
            res.put("screenshots", new JSONArray());
        }


        // =============================== VIDEOS ===============================
        try {
            JSONArray videos = gameInfo.getJSONArray("videos");
            res.put("videos", videos);
        } catch (Exception e) {
            res.put("videos", new JSONArray());
        }

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

    @Override
    public ResponseEntity<String> getGameNameCoverAndId(GamesIdsDTO gameIDs) throws UnirestException {
        // get all game ids and store them in a string, divided by commas
        String gameIDsString = String.join(",", gameIDs.getGameIDs());

        String query = "where id = (" + gameIDsString + "); fields name, cover.image_id, id;";

        JSONArray returnBody = request.post(query, "games");

        // Process the returnBody to convert image_id to the required format
        for (int i = 0; i < returnBody.length(); i++) {
            JSONObject gameObject = returnBody.getJSONObject(i);
            try {
                String image_id = gameObject.getJSONObject("cover").getString("image_id");
                String imageUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/" + image_id + ".jpg";
                gameObject.put("coverImageUrl", imageUrl);
                gameObject.remove("cover");
            } catch (Exception e) {
                gameObject.put("coverImageUrl", "https://publications.iarc.fr/uploads/media/default/0001/02/thumb_1205_default_publication.jpeg");
            }
        }

        return new ResponseEntity<>(returnBody.toString(), HttpStatus.OK);
    }

    public ResponseEntity<String> searchByGenre(String genre) throws UnirestException {
        // replace spaces with underscores to match IGDB genre format
        genre = genre.replace(" ", "_");

        // Create the query to search games by genre
        String query = "fields name, cover, first_release_date, total_rating_count; where genres.name ~ \"" + genre + "\"* & first_release_date != n & total_rating_count > 10; sort first_release_date desc; limit 50;";

        // Make a request to the IGDB API
        JSONArray returnBody = request.post(query, "games");

        // Convert the JSONArray to a String and wrap it in a ResponseEntity with HttpStatus.OK
        return new ResponseEntity<>(returnBody.toString(), HttpStatus.OK);
    }


}
