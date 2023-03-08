package com.example.android_app.CacheInteraction;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
        PlayMateCache.Token = "";
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
        JSONObject jsonCache = new JSONObject();
        jsonCache.put(key_isFirstBoot, isFirstBoot);
        jsonCache.put(key_Token, Token);

        // FileOutputStream:
        // false - перезапись
        // true - добавление записей
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(new File(context.getCacheDir(),"")+"cacheFile.srl", false));
        out.writeObject(jsonCache.toString(1));
        out.close();
    }

    private JSONObject retrieveCache(Context context) throws IOException, ClassNotFoundException, JSONException, ParseException
    {
        JSONObject jsonCache;

        if(new File(new File(context.getCacheDir(),"")+"cacheFile.srl").exists())
        {
            BufferedReader br = new BufferedReader(new FileReader(new File(context.getCacheDir(),"")+"cacheFile.srl"));
            StringBuilder sb = new StringBuilder();

            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();

            int istart = sb.toString().indexOf("{");
            int iend = sb.toString().length();

//            System.out.println("\nJSONJSONJSON\n"+sb.toString().substring(istart,iend)+"\nJSONJSONJSON\n");

            JSONParser jsonParser = new JSONParser();
            jsonCache = new JSONObject((Map) jsonParser.parse(sb.toString().substring(istart,iend)));

            PlayMateCache.isFirstBoot = jsonCache.getBoolean(key_isFirstBoot);
            PlayMateCache.Token = jsonCache.getString(key_Token);

            return jsonCache;
        }

        return null;
    }

    public void clearCache(Context context) throws JSONException, IOException {
        JSONObject jsonCache = new JSONObject();
        jsonCache.put(key_isFirstBoot, true);
        jsonCache.put(key_Token, "");

        // FileOutputStream:
        // false - перезапись
        // true - добавление записей
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(new File(context.getCacheDir(),"")+"cacheFile.srl", false));
        out.writeObject(jsonCache.toString(1));
        out.close();
    }

    public boolean IsFirstBoot(Context context) throws IOException, ClassNotFoundException, JSONException, ParseException {
        retrieveCache(context);
        return isFirstBoot;
    }

    public void setIsFirstBoot(boolean isFirstBoot, Context context) throws JSONException, IOException {
        PlayMateCache.isFirstBoot = isFirstBoot;
        saveCache(context);
    }

    public String getToken(Context context) throws JSONException, IOException, ClassNotFoundException, ParseException {
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
