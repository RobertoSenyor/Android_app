package com.example.android_app.ui.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android_app.CacheInteraction.PlayMateCache;
import com.example.android_app.HTTPInteraction.ClientHTTPRequests;
import com.example.android_app.R;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chats, container, false);
//        linear = (LinearLayout) view.findViewById(R.id.layoutChats);
//
//        int user_id;
//        JSONArray chatsArray = new JSONArray();
//
//        try {
//            user_id = ClientHTTPRequests.sendGetRequest_GetUserInfo(PlayMateCache.getInstance().getToken(inflater.getContext())).getInt("user_id");
//            chatsArray = ClientHTTPRequests.sendGetRequest_GetChatsList(PlayMateCache.getInstance().getToken(inflater.getContext()));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        for (int counter = 0; counter < chatsArray.length(); counter++) {
//            final View view_custom = getLayoutInflater().inflate(R.layout.custom_chat_element_layout, null);
//
//            TextView chatID = (TextView) view_custom.findViewById(R.id.chatID_TextView);
//            try {
//                chatID.setText(chatsArray.getJSONObject(counter).getInt("chat_id"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            TextView timeLastMessage = (TextView) view_custom.findViewById(R.id.timeLastMessage_textView);
//            timeLastMessage.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
//
//            TextView nicknameTextView = (TextView) view_custom.findViewById(R.id.nickname_textView);
//            try {
//                nicknameTextView.setText(chatsArray.getJSONObject(counter).getInt("chat_id"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            LinearLayout isThisUser = (LinearLayout) view_custom.findViewById(R.id.isThisUserSendMessage_LinearLayout);
//            LinearLayout isNoThisUser = (LinearLayout) view_custom.findViewById(R.id.isNoThisUserSendMessage_LinearLayout);
//
//            // todo - если user_id совпадает, то thisUser, иначе иначе
//            isThisUser.setVisibility(View.INVISIBLE);
//            isNoThisUser.setVisibility(View.VISIBLE);
//
//            // todo - использовать класс Chats
//            linear.addView(view_custom);
//        }

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