package com.example.android_app.Entities;

import com.example.android_app.CacheInteraction.PlayMateCache;

import java.util.ArrayList;
import java.util.List;

public class MatchingData {
    private List<MatchingCard> matchingCards;
    private static MatchingData INSTANCE;

    public MatchingData() {
        this.matchingCards = new ArrayList<>(10);
    }

    public static MatchingData getInstance()
    {
        if(INSTANCE == null)
        {
            synchronized (MatchingData.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new MatchingData();
                }
            }
        }
        return INSTANCE;
    }

    public void setMatchingCards(List<MatchingCard> matchingCards) {
        this.matchingCards.addAll(matchingCards);
    }

    public List<MatchingCard> getMatchingCards() {
        return matchingCards;
    }

    public List<String> getAvatarURLs() {
        List<String> avatarURLs = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            avatarURLs.add(matchingCards.get(i).avatarURL);
        }
        return avatarURLs;
    }
}
