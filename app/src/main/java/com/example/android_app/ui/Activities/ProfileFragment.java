package com.example.android_app.ui.Activities;

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

import com.example.android_app.CacheInteraction.PlayMateCache;
import com.example.android_app.HTTPInteraction.ClientHTTPRequests;
import com.example.android_app.R;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final Context context = inflater.getContext();

        Button logoutBtn = (Button) view.findViewById(R.id.logoutBtn);

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