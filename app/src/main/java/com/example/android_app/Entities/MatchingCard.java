package com.example.android_app.Entities;

import org.json.JSONException;
import org.json.JSONObject;

public class MatchingCard {
    public static class GameCard {
        String coverURL;
        Integer minutes;
        String gameName;

        public String getCoverURL() {
            return coverURL;
        }

        public Integer getMinutes() {
            return minutes;
        }

        public String getGameName() {
            return gameName;
        }

        public GameCard(String coverURL, Integer minutes, String gameName) {
            this.coverURL = coverURL;
            this.minutes = minutes;
            this.gameName = gameName;
        }

        public GameCard(JSONObject gameCard) throws JSONException {
            this.coverURL = gameCard.getString("cover_url");
            this.minutes = gameCard.getInt("minutes");
            this.gameName = gameCard.getString("name");
        }


    }

    Integer userID;
    String username;
    String avatarURL;
    String aboutUser;
    GameCard[] gameCards;

    public MatchingCard(Integer userID, String username, String avatarURL, String aboutUser, GameCard[] gameCards) {
        this.userID = userID;
        this.username = username;
        this.avatarURL = avatarURL;
        this.aboutUser = aboutUser;
        this.gameCards = gameCards;
    }

    public MatchingCard(JSONObject matchingCard) throws JSONException {
        gameCards = new GameCard[3];
        this.userID = matchingCard.getInt("user_id");
        this.username = matchingCard.getString("username");
        this.avatarURL = matchingCard.getString("avatar_url");
        this.aboutUser = matchingCard.getString("about");
        for (int i = 0; i < 3; i++) {
            gameCards[i] = new GameCard(matchingCard.getJSONArray("top_games").getJSONObject(i));
        }
    }

    public Integer getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public String getAboutUser() {
        return aboutUser;
    }

    public GameCard[] getGameCards() {
        return gameCards;
    }
}
