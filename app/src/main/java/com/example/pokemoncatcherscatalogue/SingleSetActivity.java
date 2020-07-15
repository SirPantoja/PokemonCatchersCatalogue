package com.example.pokemoncatcherscatalogue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import adapters.CardAdapter;
import models.Card;
import okhttp3.Headers;

public class SingleSetActivity extends AppCompatActivity {

    public static final String TAG = "SingleSetActivity";
    private RecyclerView rvCards;
    private ArrayList<Card> cards;
    private CardAdapter adapter;
    private ToggleButton btnEditToggle;
    public static boolean isChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_set);

        // Get the data stored from the intent
        Intent intent = getIntent();
        String setCode = intent.getStringExtra("code");

        // Link up views
        rvCards = findViewById(R.id.rvCards);
        btnEditToggle = findViewById(R.id.btnEditToggle);

        // Set on check listener for the toggle button
        btnEditToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SingleSetActivity.isChecked = isChecked;            // Perhaps not the most idiomatic, TODO change this up to be better
            }
        });

        // Initialize cards
        cards = new ArrayList<>();
        // Create new adapter for recycler view
        adapter = new CardAdapter(cards);
        // Set the adapter on rvCards
        rvCards.setAdapter(adapter);
        // Set the layout manager
        rvCards.setLayoutManager(new GridLayoutManager(this, 3));

        // Make the API call for the cards in this set
        getCards(setCode, cards);
    }

    private void getCards(String setCode, final ArrayList<Card> cards) {
        Log.i(TAG, "Getting cards");

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("setCode", setCode);

        client.get("https://api.pokemontcg.io/v1/cards", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess");
                JSONArray jsonArray = null;
                try {
                    jsonArray = json.jsonObject.getJSONArray("cards");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
                // Set the values in card through a loop
                for (int i = 0 ; i < jsonArray.length(); i++) {
                    Card card;
                    try {
                        card = new Card(jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("id"),
                                jsonArray.getJSONObject(i).getString("imageUrl"), jsonArray.getJSONObject(i).getInt("number"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                    cards.add(card);
                }
                // We want to sort the cards
                Collections.sort(cards);
                // Notify adapter
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure, Error getting cards");
                // TODO throw throwable lol
            }
        });
    }
}