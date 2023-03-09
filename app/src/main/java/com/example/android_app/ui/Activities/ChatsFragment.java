package com.example.android_app.ui.Activities;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android_app.CacheInteraction.PlayMateCache;
import com.example.android_app.HTTPInteraction.ClientHTTPRequests;
import com.example.android_app.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private LinearLayout linear;
    private View view;
    private List<View> allCustomViews = new ArrayList<>();
    private JSONArray chatsArray = new JSONArray();
    private int user_id = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final Context context = inflater.getContext();
        view = inflater.inflate(R.layout.fragment_chats, container, false);
        linear = (LinearLayout) view.findViewById(R.id.layoutChats);
        allCustomViews.clear();

        if (user_id == 0) {
            try {
                user_id = ClientHTTPRequests.sendGetRequest_GetUserInfo(PlayMateCache.getInstance().getToken(context)).getInt("user_id");
                PlayMateCache.getInstance().setUserID(user_id, context);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        try {
            chatsArray = ClientHTTPRequests.sendGetRequest_GetChatsList(PlayMateCache.getInstance().getToken(context));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // тут просто добавляем чаты в том количестве, которое есть на момент загрузки
        for (int counter = 0; counter < chatsArray.length(); counter++) {
            final View view_custom = getLayoutInflater().inflate(R.layout.custom_chat_element_layout, null);

            TextView chatID = (TextView) view_custom.findViewById(R.id.chatID_TextView);
            try {
                chatID.setText(chatsArray.getJSONObject(counter).getInt("chat_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            LinearLayout isThisUser = (LinearLayout) view_custom.findViewById(R.id.isThisUserSendMessage_LinearLayout);
            LinearLayout isNoThisUser = (LinearLayout) view_custom.findViewById(R.id.isNoThisUserSendMessage_LinearLayout);

            try {
                if (user_id == chatsArray.getJSONObject(counter).getInt("user_id")) {
                    isThisUser.setVisibility(View.VISIBLE);
                    isNoThisUser.setVisibility(View.INVISIBLE);
                } else {
                    isThisUser.setVisibility(View.INVISIBLE);
                    isNoThisUser.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            allCustomViews.add(view_custom);
            linear.addView(view_custom);
        }

        Runnable task = () ->
        {
            for (int counter = 0; counter < chatsArray.length(); counter++) {
                final View view_custom = allCustomViews.get(counter);

                ImageView personAvatar_ImageView = (ImageView) view_custom.findViewById(R.id.person_avatar_ImageView);
                TextView nickname_TextView = (TextView) view_custom.findViewById(R.id.nickname_TextView);
                TextView timeLastMessage_TextView = (TextView) view_custom.findViewById(R.id.timeLastMessage_TextView);
                TextView textLastMessage_TextView = (TextView) view_custom.findViewById(R.id.isThisUserLastMessage_TextView);

                if (view_custom.findViewById(R.id.isNoThisUserSendMessage_LinearLayout).getVisibility() == View.VISIBLE) {
                    textLastMessage_TextView = (TextView) view_custom.findViewById(R.id.isNoThisUserLastMessage_TextView);
                }

                try {
                    // Устанавливаем картинку
                    Picasso.get().load(
                            chatsArray.getJSONObject(counter).getString("avatar_url")).into(personAvatar_ImageView);

                    // Устанавливаем ник
                    nickname_TextView.setText(chatsArray.getJSONObject(counter).getString("username"));

                    // Устанавливаем время последнего сообщения
                    timeLastMessage_TextView.setText(LocalTime.parse(
                            chatsArray.getJSONObject(counter).getJSONArray("last_msg").getString(1)).
                            format(DateTimeFormatter.ofPattern("HH:mm")));

                    // Устанавливаем текст последнего сообщения
                    textLastMessage_TextView.setText(chatsArray.getJSONObject(counter).getJSONArray("last_msg").getString(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(task);
        thread.run();

//        ConstraintLayout messageDialog = (ConstraintLayout) view.findViewById(R.id.messageDialog_ConstraintLayout);
//
//        messageDialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return view;
    }
}