package com.example.android_app.ui.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android_app.R;
import com.google.android.material.timepicker.TimeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatsFragment extends Fragment {

    private List<View> allEds = new ArrayList<>();
    private int counter = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_chats, container,false);
        final LinearLayout linear = (LinearLayout) view.findViewById(R.id.layoutChats);

        for(;counter<50;counter++)
        {
            final View view_custom = getLayoutInflater().inflate(R.layout.custom_chat_element_layout, null);
            TextView timeLastMessage = (TextView) view_custom.findViewById(R.id.timeLastMessage_textView);
            timeLastMessage.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

            allEds.add(view_custom);
            linear.addView(view_custom);
        }

        return view;
    }
}