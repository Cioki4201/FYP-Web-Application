package com.paul.fyp.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
  @Id
  private String id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @NotBlank
  @Size(max = 50)
  private String firstName;

  @NotBlank
  @Size(max = 50)
  private String lastName;

  @DBRef
  private Set<Role> roles = new HashSet<>();

  // Game Categories: 0 = Want to Play, 1 = Playing, 2 = Completed, 3 = Dropped, 4 = On Hold
  private List<List<String>> games = new ArrayList<List<String>>();

  private List<Integer> recommended_games = new ArrayList<Integer>();


  public User() {
  }

  public User(String username, String email, String password, String firstName, String lastName) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;

    for (int i = 0; i < 5; i++) {
      games.add(new ArrayList<String>());
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

    public List<Integer> getRecommendedGames() {
        return recommended_games;
    }

    public void setRecommendedGames(List<Integer> recommended_games) {
        this.recommended_games = recommended_games;
    }


  // ==================== Game Methods ====================

  private GameRecommender gameRecommender = new GameRecommender();

  public void addGame(String gameID, int category) {
    // Add game to specified category
    games.get(category).add(gameID);

    // Update Recommendations
    this.UpdateRecommendations();
  }

  // Find if game is in any category and return the category
  public int findGame(String gameID) {
      for (int i = 0; i < 5; i++) {
      if (games.get(i).contains(gameID)) {
          return i;
      }
    }
    return -1;
  }

  // Move game from one category to another
  public void moveGame(String gameID, int from, int to) {
      games.get(from).remove(gameID);
      games.get(to).add(gameID);

      // Update Recommendations
      this.UpdateRecommendations();
  }

  // Get Games
    public List<List<String>> getGames() {
        return games;
    }

  // Remove Game
    public void removeGame(String gameID) {
        for (int i = 0; i < 5; i++) {
        if (games.get(i).contains(gameID)) {
            games.get(i).remove(gameID);
        }
      }

      // Update Recommendations
      this.UpdateRecommendations();
    }

    public void UpdateRecommendations() {
        // If user has no games, return
        if (games.get(0).isEmpty() && games.get(1).isEmpty() && games.get(2).isEmpty()) {
          return;
        }

        // Get User Games from "games" list from the following categories only 0 = Want to Play, 1 = Playing, 2 = Completed and store them all in a list
        List<Integer> userGames = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
          for (String game : games.get(i)) {
            userGames.add(Integer.parseInt(game));
          }
        }

        try {
          this.setRecommendedGames(gameRecommender.recommendGames(userGames));
        } catch (IOException | InterruptedException e) {
          e.printStackTrace();
        }
    }

    // Get total count of games
    public int getGameCount() {
        int total = 0;
        for (int i = 0; i < 5; i++) {
            total += games.get(i).size();
        }
        return total;
    }

    public boolean hasGame(String gameID) {
        for (int i = 0; i < 5; i++) {
            if (games.get(i).contains(gameID)) {
                return true;
            }
        }
        return false;
    }

}