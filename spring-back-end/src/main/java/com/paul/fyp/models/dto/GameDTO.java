package com.paul.fyp.models.dto;

public class GameDTO {
    private String gameID;
    private int category;

    // getters and setters

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}