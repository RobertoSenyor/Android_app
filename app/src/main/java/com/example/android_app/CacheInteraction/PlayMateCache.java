package com.example.android_app.CacheInteraction;

import android.content.Context;

import com.example.android_app.Entities.Chats;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;


public class PlayMateCache
{
    private static String key_isFirstBoot = "isFirstBoot";
    private static boolean isFirstBoot;

    private static String key_Token = "Token";
    private static String Token;

    private static PlayMateCache INSTANCE;

    private PlayMateCache()
    {
        PlayMateCache.isFirstBoot = true;
        PlayMateCache.Token = null;
    }

    public static PlayMateCache getInstance()
    {
        if(INSTANCE == null)
        {
            synchronized (PlayMateCache.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new PlayMateCache();
                }
            }
        }
        return INSTANCE;
    }

    private void saveCache(Context context) throws JSONException, IOException
    {
        System.out.println("AAAAAAAAAAAAAAAAAAA\n"+context.getCacheDir()+"\nAAAAAAAAAAAAAAAAAAA");

        JSONObject jsonCache = new JSONObject();
        jsonCache.put(key_isFirstBoot, isFirstBoot);
        jsonCache.put(key_Token, Token);

        // FileOutputStream:
        // false - перезапись
        // true - добавление записей
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(new File(context.getCacheDir(),"")+"cacheFile.srl", false));
        out.writeObject(jsonCache);
        out.close();
    }

    private JSONObject retrieveCache(Context context) throws IOException, ClassNotFoundException, JSONException
    {
        System.out.println("AAAAAAAAAAAAAAAAAAA\n"+context.getCacheDir()+"\nAAAAAAAAAAAAAAAAAAA");

        JSONObject jsonCache = new JSONObject();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(new File(context.getCacheDir(),"")+"cacheFile.srl")));
        jsonCache = (JSONObject) in.readObject();
        in.close();

        PlayMateCache.isFirstBoot = jsonCache.getBoolean(key_isFirstBoot);
        PlayMateCache.Token = jsonCache.getString(key_Token);

        return jsonCache;
    }

    public boolean IsFirstBoot(Context context) throws IOException, ClassNotFoundException, JSONException {
        retrieveCache(context);
        return isFirstBoot;
    }

    public void setIsFirstBoot(boolean isFirstBoot, Context context) throws JSONException, IOException {
        PlayMateCache.isFirstBoot = isFirstBoot;
        saveCache(context);
    }

    public String getToken(Context context) throws JSONException, IOException, ClassNotFoundException {
        retrieveCache(context);
        return Token;
    }

    public void setToken(String token, Context context) throws JSONException, IOException {
        PlayMateCache.Token = token;
        saveCache(context);
    }

    public void setTokenNull(Context context) throws JSONException, IOException {
        PlayMateCache.Token=null;
        saveCache(context);
    }
}
