package com.paul.fyp.controllers;

import com.paul.fyp.models.User;

import com.paul.fyp.models.dto.GameDTO;
import com.paul.fyp.repository.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    // Game Categories: 0 = Want to Play, 1 = Playing, 2 = Completed, 3 = Dropped, 4 = On Hold
    @PostMapping("/{username}/add_game")
    public String addGameToUser(@PathVariable("username") String username, @RequestBody GameDTO gameDTO) {
        User user = userRepository.findByUsername(username).get();

        //check if game already exists in user's list
        int gameCategory = user.findGame(gameDTO.getGameID());

        if (gameCategory != -1) {
            //if game already exists, move it to new category
            user.moveGame(gameDTO.getGameID(), gameCategory, gameDTO.getCategory());
            userRepository.save(user);
            return "Game moved to new category!";
        } else {
            //if game doesn't exist, add it to new category
            user.addGame(gameDTO.getGameID(), gameDTO.getCategory());
            userRepository.save(user);
            return "Game added!";
        }
    }

    @PostMapping("/{username}/remove_game/{gameID}")
    public String removeGameFromUser(@PathVariable("username") String username, @PathVariable("gameID") String gameID) {
        User user = userRepository.findByUsername(username).get();

        //check if game already exists in user's list
        int gameCategory = user.findGame(gameID);

        if (gameCategory != -1) {
            //if game already exists, remove it
            user.removeGame(gameID);
            userRepository.save(user);
            return "Game removed!";
        } else {
            return "Game not found!";
        }
    }

    // Gets all user details from database and returns them as a json object so that it can be used in a javascript function. Label each element in the json object with the name of the variable you want to use in the javascript function.
    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUser(@PathVariable("username") String username) {
        User user = userRepository.findByUsername(username).get();

        JSONObject userJSON = new JSONObject();
        userJSON.put("username", user.getUsername());
        userJSON.put("email", user.getEmail());
        userJSON.put("firstName", user.getFirstName());
        userJSON.put("lastName", user.getLastName());

        JSONArray gamesJSON = new JSONArray();
        for (int i = 0; i < 5; i++) {
            JSONArray categoryJSON = new JSONArray();
            for (String game : user.getGames().get(i)) {
                categoryJSON.put(game);
            }
            gamesJSON.put(categoryJSON);
        }
        userJSON.put("games", gamesJSON);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(userJSON.toString());
    }

//    Get user's recommended games
    @GetMapping(value = "/{username}/recommended", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRecommendedGames(@PathVariable("username") String username) {
        User user = userRepository.findByUsername(username).get();

        List<Integer> gamesJSON = user.getRecommendedGames();

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(gamesJSON.toString());
    }

    // Get user's count of games
    @GetMapping(value = "/{username}/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public int getUserCount(@PathVariable("username") String username) {
        User user = userRepository.findByUsername(username).get();

        return user.getGameCount();
    }

    // Check if game is in user's list
    @GetMapping(value = "/{username}/has/{gameID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean hasGame(@PathVariable("username") String username, @PathVariable("gameID") String gameID) {
        User user = userRepository.findByUsername(username).get();

        return user.hasGame(gameID);
    }


}
