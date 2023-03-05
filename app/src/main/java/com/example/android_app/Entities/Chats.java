package com.example.android_app.Entities;

import com.example.android_app.HTTPInteraction.ClientHTTPRequests;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.TreeMap;

public class Chats {

    private TreeMap<Integer, Chat> ChatsTree;
    private static Chats INSTANCE;

    private class Chat {

        private int chat_id; // идентификатор чата
        private int user_id; // идентификатор собеседника

        public Chat(int chat_id, int user_id)
        {
            this.chat_id = chat_id;
            this.user_id = user_id;
        }

        public int getChat_id() {
            return chat_id;
        }

        public void setChat_id(int chat_id) {
            this.chat_id = chat_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }

    private Chats()
    {
        ChatsTree = new TreeMap<>();
    }

    public static Chats getInstance()
    {
        if(INSTANCE == null)
        {
            synchronized (Chats.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new Chats();
                }
            }
        }
        return INSTANCE;
    }

    public void setChatsTreeUsingJSONArray(JSONArray _Array) throws JSONException
    {
        ChatsTree.clear();

        for (int i=0; i < _Array.length(); i++)
        {
            ChatsTree.put(_Array.getJSONObject(i).getInt("chat_id"),
                    new Chat(_Array.getJSONObject(i).getInt("chat_id"),_Array.getJSONObject(i).getInt("user_id"))
                    );
        }
    }

    public TreeMap getChatsTree()
    {
        return ChatsTree;
    }
}
