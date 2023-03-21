package com.paul.fyp.controllers;

import com.paul.fyp.models.User;

import com.paul.fyp.models.dto.GameDTO;
import com.paul.fyp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
