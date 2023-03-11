package com.example.android_app.ui.Activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.android_app.CacheInteraction.PlayMateCache;
import com.example.android_app.Entities.MatchingCard;
import com.example.android_app.Entities.MatchingData;
import com.example.android_app.HTTPInteraction.ClientHTTPRequests;
import com.example.android_app.adapters.SwipeAdapter;
import com.example.android_app.R;
import com.example.android_app.databinding.FragmentProfileBinding;
import com.example.android_app.ui.Activities.LoginActivity;
import com.example.android_app.ui.Activities.RegistrationActivity;
import com.yalantis.library.Koloda;
import com.yalantis.library.KolodaListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PersonsFragment extends Fragment {

    private FragmentProfileBinding binding;
    String token;
    private List <MatchingCard> matchingCards;
    private JSONArray infoList = new JSONArray();

    private int currentCardPosition = 0;

    private SwipeAdapter swipeAdapter;
    private List<Integer> list;
    Koloda koloda;

    private int dataSize;

    private JSONArray getMatchingResults() {
        AtomicReference<JSONArray> matching_results = new AtomicReference<>(new JSONArray());
        Runnable task = () -> {
            matching_results.set(ClientHTTPRequests.sendGetRequest_GetInfoList(token));
        };
        Thread thread = new Thread(task);
        thread.start();

        while (thread.isAlive())
        {}
        dataSize = matching_results.get().length();
        System.out.println("matching_results: " + matching_results.get().length());
        return matching_results.get();
    }

    private void fillData() {
        infoList = getMatchingResults();
        System.out.println("response: " + infoList.length());
        matchingCards = new ArrayList<>(dataSize);
        for (int j = 0; j < dataSize; j++) {
            matchingCards.add(null);
        }
        for (int j = 0; j < dataSize; j ++) {
            try {
                System.out.println(j + ": " + infoList.getJSONObject(j));
                matchingCards.set(j, new MatchingCard(infoList.getJSONObject(j)));
                //matchingCards[j] = new MatchingCard(ClientHTTPRequests.sendGetRequest_GetUserInfo(infoList.getJSONObject(j).getInt("user_id"), token));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        MatchingData.getInstance().setMatchingCards(matchingCards);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //setContentView(R.layout.matching_page);

        try {
            token = PlayMateCache.getInstance().getToken(inflater.getContext());
        } catch (JSONException | ParseException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

        fillData();

        final View view = inflater.inflate(R.layout.matching_page, container,false);

        koloda = view.findViewById(R.id.koloda);
        list = new ArrayList<>(dataSize);
        swipeAdapter = new SwipeAdapter(view.getContext(), list);
        swipeAdapter.setSize(dataSize);
        koloda.setAdapter(swipeAdapter);
        //koloda.setNeedCircleLoading(true);

        koloda.setKolodaListener(new KolodaListener() {
                                     @Override
                                     public void onNewTopCard(int i) {}
                                     @Override
                                     public void onCardDrag(int i, @NonNull View view, float v) {}
                                     @Override
                                     public void onCardSwipedLeft(int i) {
                                         Runnable task = () -> {
                                             ClientHTTPRequests.sendPostRequest_SetAttitude(-1, matchingCards.get(currentCardPosition).getUserID() ,token);
                                         };
                                         Thread thread = new Thread(task);
                                         thread.start();

                                         while (thread.isAlive())
                                         {}
                                         currentCardPosition++;
                                     }
                                     @Override
                                     public void onCardSwipedRight(int i) {
                                         Runnable task = () -> {
                                             ClientHTTPRequests.sendPostRequest_SetAttitude(1, matchingCards.get(currentCardPosition).getUserID() ,token);
                                         };
                                         Thread thread = new Thread(task);
                                         thread.start();

                                         while (thread.isAlive())
                                         {}
                                         currentCardPosition++;
                                     }
                                     @Override
                                     public void onClickRight(int i) {}
                                     @Override
                                     public void onClickLeft(int i) {}
                                     @Override
                                     public void onCardSingleTap(int i) {}
                                     @Override
                                     public void onCardDoubleTap(int i) {}
                                     @Override
                                     public void onCardLongPress(int i) {}
                                     @Override
                                     public void onEmptyDeck() {
                                         currentCardPosition = 0;
                                         fillData();
                                         //list = new ArrayList<>(dataSize);
                                         //swipeAdapter = new SwipeAdapter(view.getContext(), list);
                                         swipeAdapter.setSize(dataSize);
                                         //koloda.setAdapter(swipeAdapter);
                                         koloda.reloadAdapterData();
                                         /*view.post(new Runnable() {
                                             @Override
                                             public void run() {
                                                 view.invalidate();
                                             }
                                         });*/
                                     }
                                 }
        );

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}