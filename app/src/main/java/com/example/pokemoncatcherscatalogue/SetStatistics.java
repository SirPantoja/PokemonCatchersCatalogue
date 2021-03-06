package com.example.pokemoncatcherscatalogue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import adapters.MissingCardAdapter;
import models.Card;
import models.ParseCard;
import okhttp3.Headers;

public class SetStatistics extends AppCompatActivity {

    public static final String TAG = "SetStatistics";
    private String setCode;
    private ArrayList<Integer> stats;
    private HashMap<String, Card> cardsHash = new HashMap<>();
    private MissingCardAdapter adapter;
    private ProgressBar pbSetStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_statistics);

        // Get data from intent
        Intent intent = getIntent();
        setCode = intent.getStringExtra("code");
        String logoUrl = intent.getStringExtra("logo");
        String setName = intent.getStringExtra("name");

        // Parse the setName if needed
        assert setName != null;
        if (setName.length() >= 20) {
            setName = setName.substring(0, Math.min(setName.length(), 20)) + "...";
        }

        // Link up views
        ImageView ivSetLogo = findViewById(R.id.ivSymbol);
        TextView tvSetName = findViewById(R.id.tvSetName);
        RecyclerView rlCards = findViewById(R.id.rlCards);
        pbSetStatistics = findViewById(R.id.pbSetStatistics);

        // Set up cards and stats
        ArrayList<Card> cards = new ArrayList<>();
        stats = new ArrayList<>();
        // Create the adapter
        adapter = new MissingCardAdapter(cards, stats);
        // Set the adapter to rlCards
        rlCards.setAdapter(adapter);
        // Set the layout manager to rlCards
        rlCards.setLayoutManager(new LinearLayoutManager(this));

        // get the cards to populate the recycler view and start up the progress bar
        pbSetStatistics.setVisibility(View.VISIBLE);
        getCards();

        // Add content to views
        Glide.with(this).load(logoUrl).into(ivSetLogo);
        tvSetName.setText(setName);
    }

    private void getCards() {
        // Set up for the API request
        AsyncHttpClient client = ((ParseApplication) this.getApplicationContext()).getClient();
        RequestParams params = new RequestParams();
        params.put("setCode", setCode);

        client.get("https://api.pokemontcg.io/v1/cards", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess");

                // Get the cards array to access data
                JSONArray jsonArray;
                try {
                    jsonArray = json.jsonObject.getJSONArray("cards");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
                // Set the values in card through a loop
                for (int i = 0 ; i < jsonArray.length(); i++) {

                    // Create the card using the constructor
                    Card card;
                    try {
                        card = new Card(jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("id"),
                                jsonArray.getJSONObject(i).getString("imageUrl"), jsonArray.getJSONObject(i).getString("setCode"));
                    } catch (JSONException e) {
                        // This means there was a critical error with the API request
                        e.printStackTrace();
                        return;
                    }

                    // Get the optional parameter of type; not all cards have a valid number
                    try {
                        card.number = jsonArray.getJSONObject(i).getInt("number");
                    } catch (JSONException e) {
                        // Do nothing; it is expected that some cards do not have this
                    }

                    // Get the optional parameter of type; not all cards have a type
                    try {
                        card.type = jsonArray.getJSONObject(i).getJSONArray("types").getString(0);
                    } catch (JSONException e) {
                        // Do nothing; it is expected that some cards do not have this
                    }

                    // Get the optional parameter of hp; not all cards have hp
                    try {
                        card.setHp(jsonArray.getJSONObject(i).getString("hp"));
                    } catch (JSONException e) {
                        // Do nothing; it is expected that some cards do not have this
                    }

                    // Get the optional parameter of rarity; not all cards have a rarity
                    try {
                        card.setRarity(jsonArray.getJSONObject(i).getString("rarity"));
                    } catch (JSONException e) {
                        // Do nothing; it is expected that some cards do not have this
                    }

                    // Add the new card to the list of cards for the adapter
                    cardsHash.put(card.id, card);
                }

                // Set up the remaining statistical values after querying Parse
                getParseCards();
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure, Error getting cards", throwable);
            }
        });
    }

    private void getParseCards() {
        // Query Parse and get all the cards and load them into views
        ParseQuery<ParseCard> query = ParseQuery.getQuery(ParseCard.class);

        // Check if we are in privileged mode or not; if not load the other Users information
        ParseUser user = ((ParseApplication) getApplicationContext()).userPerm();

        // Set up params
        query.whereEqualTo("owner", user);
        query.whereEqualTo("setName", setCode);
        query.findInBackground(new FindCallback<ParseCard>() {
            @SuppressLint("SetTextI18n")
            public void done(List<ParseCard> cards, ParseException e) {
                if (e == null) {

                    // Declare fields to populate views with
                    int total = 0;
                    int uniqueCards = 0;
                    int rares = 0;
                    int uncommons = 0;
                    int commons = 0;

                    // Access the array of results here
                    for (ParseCard card : cards) {

                        Log.i(TAG, card.getName());

                        int count = card.getCount();
                        if (count > 0) {
                            // Remove the card from cards
                            cardsHash.remove(card.getCardId());
                            Log.i(TAG, "Removing " + card.getName());
                            uniqueCards++;
                            total += count;
                            // Increment by rarity
                            int rarity = card.getRarity();
                            if (rarity >= 3) {
                                rares++;
                            } else if (rarity == 2) {
                                uncommons++;
                            } else if (rarity == 1) {
                                commons++;
                            }
                        }
                    }
                    // Enter data into views
                    stats.add(0, total);            // add the total
                    stats.add(1, uniqueCards);      // add the uniqueCards
                    stats.add(2, rares);            // add the rares
                    stats.add(3, uncommons);        // add the uncommons
                    stats.add(4, commons);          // add the commons

                    // Populate the adapter from the data left in the hash map

                    // Getting an iterator
                    Iterator hmIterator = cardsHash.entrySet().iterator();

                    // Declare new cards
                    ArrayList<Card> newCards = new ArrayList<Card>();

                    while (hmIterator.hasNext()) {
                        Map.Entry mapElement = (Map.Entry)hmIterator.next();
                        newCards.add((Card)mapElement.getValue());
                    }

                    // Add to the adapter
                    adapter.clear();
                    Card.sort = Card.SORT.NUMBER;
                    Collections.sort(newCards);
                    adapter.addAll(newCards);           // Also notifies data set has been changed
                    pbSetStatistics.setVisibility(View.INVISIBLE);

                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }
}