package com.example.android_app.ui.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.android_app.CacheInteraction.PlayMateCache;
import com.example.android_app.Entities.MatchingCard;
import com.example.android_app.HTTPInteraction.ClientHTTPRequests;
import com.example.android_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ProfileFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean IsSuccessLogout(Context context)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        AtomicBoolean responseCode = new AtomicBoolean(false);

        Runnable task = () -> {
            try {
                responseCode.set(ClientHTTPRequests.sendPostRequest_LogoutUser(PlayMateCache.getInstance().getToken(context)));
                Thread.currentThread().interrupt();
                return;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        };

        Thread thread = new Thread(task);
        thread.run();

        while (true) {
            if (!thread.isAlive()) {
                break;
            }
        }

        return responseCode.get();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String token;

        try {
            token = PlayMateCache.getInstance().getToken(inflater.getContext());
        } catch (JSONException | ParseException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final Context context = inflater.getContext();

        Button logoutBtn = (Button) view.findViewById(R.id.logoutBtn);

        AtomicReference<JSONObject> profileInfo = new AtomicReference<>(new JSONObject());
        Runnable task = () -> {
            profileInfo.set(ClientHTTPRequests.sendGetRequest_GetUserInfo(token));
        };
        Thread thread = new Thread(task);
        thread.start();

        while (thread.isAlive())
        {}

        MatchingCard profileCard;
        try {
            profileCard = new MatchingCard(profileInfo.get());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        ImageView userAvatar  = view.findViewById(R.id.user_avatar);
        TextView userName = view.findViewById(R.id.username);
        TextView aboutUser = view.findViewById(R.id.about);

        ImageView gameImage1 = view.findViewById(R.id.game_avatar_1);
        TextView gameName1 = view.findViewById(R.id.game_name_1);
        TextView gameHours1 = view.findViewById(R.id.game_hours_1);

        ImageView gameImage2 = view.findViewById(R.id.game_avatar_2);
        TextView gameName2 = view.findViewById(R.id.game_name_2);
        TextView gameHours2 = view.findViewById(R.id.game_hours_2);

        ImageView gameImage3 = view.findViewById(R.id.game_avatar_3);
        TextView gameName3 = view.findViewById(R.id.game_name_3);
        TextView gameHours3 = view.findViewById(R.id.game_hours_3);

        Glide.with(context)
                .load(profileCard.getAvatarURL()/*MatchingData.getInstance().avatarURLs*/)
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().transform(new RoundedCorners(200)))
                .into(userAvatar);
        userName.setText(profileCard.getUsername());
        aboutUser.setText(profileCard.getAboutUser());
        Glide.with(context)
                .load(profileCard.getGameCards()[0].getCoverURL())
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().transform(new RoundedCorners(50)))
                .into(gameImage1);
        gameName1.setText(profileCard.getGameCards()[0].getGameName());
        gameHours1.setText(Integer.toString(profileCard.getGameCards()[0].getMinutes()));
        Glide.with(context)
                .load(profileCard.getGameCards()[1].getCoverURL())
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().transform(new RoundedCorners(50)))
                .into(gameImage2);
        gameName2.setText(profileCard.getGameCards()[1].getGameName());
        gameHours2.setText(Integer.toString(profileCard.getGameCards()[1].getMinutes()));
        Glide.with(context)
                .load(profileCard.getGameCards()[2].getCoverURL())
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().transform(new RoundedCorners(50)))
                .into(gameImage3);
        gameName3.setText(profileCard.getGameCards()[2].getGameName());
        gameHours3.setText(Integer.toString(profileCard.getGameCards()[2].getMinutes()));

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (IsSuccessLogout(context)) {
                        PlayMateCache.getInstance().setTokenNull(context);
                        PlayMateCache.getInstance().setUserIDNull(context);
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        if (context instanceof Activity) {
                            ((Activity) context).finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}