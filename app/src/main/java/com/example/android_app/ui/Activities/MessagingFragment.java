package com.example.android_app.ui.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

public class MessagingFragment extends AppCompatActivity {

    private LinearLayout linear;
    private List<View> allCustomViews = new ArrayList<>();
    private JSONArray messagesArray = new JSONArray();
    private static int MESSAGES_COUNT = 50;

    @Override
    public void onBackPressed() {

        try {
            Intent intent = new Intent(MessagingFragment.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void refreshChat()
    {
        final Context context = getApplicationContext();
        linear = (LinearLayout) findViewById(R.id.layoutMessages);
        allCustomViews.clear();

        try {
            messagesArray = ClientHTTPRequests.sendGetRequest_GetMessagesList(ChatsFragment.selected_chat_id, 0, MESSAGES_COUNT, true, PlayMateCache.getInstance().getToken(context));
        } catch (JSONException | IOException | ClassNotFoundException | ParseException e) {
            e.printStackTrace();
        }

        Runnable task = () ->
        {
            for (int counter = 0; counter < messagesArray.length(); counter++)
            {
                View view_custom = null;

                try {
                    if (messagesArray.getJSONObject(counter).getInt("user_id") == PlayMateCache.getInstance().getUserID(context))
                    {
                        view_custom = findViewById(R.id.my_messageFrameLayout);
                    }
                    else
                    {
                        view_custom = findViewById(R.id.no_my_messageFrameLayout);
                    }
                } catch (JSONException | IOException | ParseException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                TextView messageText = (TextView) view_custom.findViewById(R.id.message_TextView);

                try {
                    messageText.setText(messagesArray.getJSONObject(counter).getString("text"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                allCustomViews.add(view_custom);
                linear.addView(view_custom);
            }
        };

        Thread thread = new Thread(task);
        thread.run();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final Context context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_messaging);
        linear = (LinearLayout) findViewById(R.id.layoutMessages);
        allCustomViews.clear();

        try {
            messagesArray = ClientHTTPRequests.sendGetRequest_GetMessagesList(ChatsFragment.selected_chat_id, 0, MESSAGES_COUNT, true, PlayMateCache.getInstance().getToken(context));
        } catch (JSONException | IOException | ClassNotFoundException | ParseException e) {
            e.printStackTrace();
        }

        Runnable task = () ->
        {
            for (int counter = 0; counter < messagesArray.length(); counter++)
            {
                try {
                    View view_custom = findViewById(R.id.no_my_messageFrameLayout);

                    if (messagesArray.getJSONObject(counter).getInt("user_id") == PlayMateCache.getInstance().getUserID(context))
                    {
                        view_custom = findViewById(R.id.my_messageFrameLayout);
                    }

                    TextView messageText = (TextView) view_custom.findViewById(R.id.message_TextView);
                    messageText.setText(messagesArray.getJSONObject(counter).getString("text"));

                    allCustomViews.add(view_custom);
                    linear.addView(view_custom);
                }
                catch (JSONException | IOException | ParseException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(task);
        thread.run();

        TextView messageTextChat = (TextView) findViewById(R.id.messageTextChat);

        ImageButton sendMessageBtn = (ImageButton) findViewById(R.id.sendMessageBtn);
        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ClientHTTPRequests.sendPostRequest_SendMessage(ChatsFragment.selected_chat_id,
                            messageTextChat.getText().toString(),
                            PlayMateCache.getInstance().getToken(context));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                messageTextChat.setText("");
//                refreshChat();
            }
        });
    }
}