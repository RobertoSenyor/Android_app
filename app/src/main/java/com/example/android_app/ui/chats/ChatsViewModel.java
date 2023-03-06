package com.example.android_app.ui.chats;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_app.R;

import java.util.ArrayList;
import java.util.List;

public class ChatsViewModel extends ViewModel {

    private List<View> allEds;
    private int counter = 0;

    public ChatsViewModel() {
//        allEds = new ArrayList<>();
//
//        final LinearLayout linear = (LinearLayout) findViewById(R.id.layoutChats);
//
//        for (int i=0; i<5; i++, ++counter)
//        {
//            final View view = getLayoutInflater().inflate(R.layout.custom_chat_element_layout, null);
//
//            ImageView imageView = (ImageView) view.findViewById(R.id.person_avatar_imageView);
//
//            // TODO - вытаскивать данные из HTTP-класса
//            Picasso.get().load("https://avatars.akamai.steamstatic.com/62e0688275a39d0c01754e666f191b8b3e0bbe30_full.jpg").into(imageView);
//
//            TextView nickname_TextView = (TextView) view.findViewById(R.id.nickname_textView);
//            nickname_TextView.setText("Barbara");
//
//            TextView lastMessage_TextView = (TextView) view.findViewById(R.id.lastMessage_textView);
//            lastMessage_TextView.setText("Приветики!!!");
//
//            TextView timeLastMessage_TextView = (TextView) view.findViewById(R.id.timeLastMessage_textView);
//            timeLastMessage_TextView.setText((int) System.currentTimeMillis());
//
//            allEds.add(view);
//            linear.addView(view);
    }
}
