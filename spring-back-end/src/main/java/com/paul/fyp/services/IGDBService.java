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
}
