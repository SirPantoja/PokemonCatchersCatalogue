package com.example.pokemoncatcherscatalogue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapters.CardAdapter;
import models.Card;
import models.ParseCard;
import okhttp3.Headers;

public class SingleSetActivity extends AppCompatActivity {

    public static final String TAG = "SingleSetActivity";
    private RecyclerView rvCards;
    private ArrayList<Card> cards;
    private CardAdapter adapter;
    private ToggleButton btnEditToggle;
    private Spinner spinnerSort;
    private boolean check = false;          // Because of a spinner patch https://stackoverflow.com/questions/13397933/android-spinner-avoid-onitemselected-calls-during-initialization
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
        spinnerSort = findViewById(R.id.spinnerSort);

        // Set on check listener for the toggle button
        btnEditToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SingleSetActivity.isChecked = isChecked;            // Perhaps not the most idiomatic, TODO change this up to be better
            }
        });

        // Set on item selected listener for the spinner and call the appropriate sort functions
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // See the reference on the boolean for why this if statement is here
                if (check) {
                    switch (i) {
                        case 0:
                            Toast.makeText(SingleSetActivity.this, "Number", Toast.LENGTH_SHORT).show();
                            Card.sort = Card.SORT.NUMBER;
                            Collections.sort(cards);
                            break;
                        case 1:
                            Toast.makeText(SingleSetActivity.this, "Type", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(SingleSetActivity.this, "HP", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(SingleSetActivity.this, "Rarity", Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            Toast.makeText(SingleSetActivity.this, "Alphabetical", Toast.LENGTH_SHORT).show();
                            Card.sort = Card.SORT.ALPHABETICAL;
                            Collections.sort(cards);
                            break;
                        default:
                            // Do nothing; assume the user does not want a sort change
                            break;
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    check = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Nothing should be done; assume the user wants to keep current sort
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
                Log.i(TAG, "onSuccess " + json.toString());
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
                                jsonArray.getJSONObject(i).getString("imageUrl"), jsonArray.getJSONObject(i).getString("setCode"),
                                jsonArray.getJSONObject(i).getInt("number"));
                        getCount(card);
                        Log.i(TAG, card.name + " " + card.count);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                    cards.add(card);
                }
                // We want to sort the cards
                Card.sort = Card.SORT.NUMBER;
                Collections.sort(cards);
                // Notify adapter
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure, Error getting cards", throwable);
            }
        });
    }

    private void getCount(final Card card) {
        Log.i(TAG, "Getting count");
        // First query Parse to see if the card already exists
        ParseQuery<ParseCard> query = ParseQuery.getQuery(ParseCard.class);
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.whereEqualTo("cardId", card.id);
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseCard>() {
            public void done(List<ParseCard> itemList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    if (itemList.isEmpty()) {
                        // This means this card doesn't already exist so we do nothing since count is at 0
                        return;
                    } else {
                        // This means we found the card so we just need to increment it
                        ParseCard newParseCard = itemList.get(0);
                        card.count = newParseCard.getCount();
                    }
                    Log.i(TAG, "Successfully retrieved count from Parse. count: " + card.count);
                    // Since we incremented card.count we have to let the adapter know so the view changes
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }
}