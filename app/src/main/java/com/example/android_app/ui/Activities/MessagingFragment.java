package com.example.android_app.ui.Activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
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
import java.util.ArrayList;
import java.util.List;

public class MessagingFragment extends Fragment {

    private LinearLayout linear;
    private View view;
    private List<View> allCustomViews = new ArrayList<>();
    private JSONArray messagesArray = new JSONArray();
    private static int MESSAGES_COUNT = 50;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final Context context = inflater.getContext();
        view = inflater.inflate(R.layout.fragment_messaging, container, false);
        linear = (LinearLayout) view.findViewById(R.id.layoutMessaging);
        allCustomViews.clear();

        try {
            messagesArray = ClientHTTPRequests.sendGetRequest_GetMessagesList(ChatsFragment.selected_chat_id,0, MESSAGES_COUNT, true, PlayMateCache.getInstance().getToken(context));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
//
//        Runnable task = () ->
//        {
//            for (int counter = 0; counter < messagesArray.length(); counter++)
//            {
//                final View view_custom = getLayoutInflater().inflate(R.layout.my_message_layout, null);
//
//                ConstraintLayout messagingLayout = (ConstraintLayout) view_custom.findViewById(R.id.no_my_messageFrameLayout);
//
//                try {
//                    if(messagesArray.getJSONObject(counter).getInt("user_id") == PlayMateCache.getInstance().getUserID(context))
//                    {
//                        messagingLayout.setBackground(Drawable.createFromPath(is_this_user_message));
//                    }
//                    else
//                    {
//                        messagingLayout.setBackground(Drawable.createFromPath(is_no_this_user_message));
//                    }
//                } catch (JSONException | ClassNotFoundException | ParseException | IOException e) {
//                    e.printStackTrace();
//                }
//
//                TextView message_id_TextView = (TextView) view_custom.findViewById(R.id.message_id_TextView);
//                try {
//                    message_id_TextView.setText(messagesArray.getJSONObject(counter).getInt("message_id"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        Thread thread = new Thread(task);
//        thread.run();

        return view;
    }
}