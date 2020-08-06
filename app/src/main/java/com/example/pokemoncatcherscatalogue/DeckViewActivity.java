package com.example.pokemoncatcherscatalogue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import adapters.ListAdapter;
import models.Deck;
import models.ParseCard;

public class DeckViewActivity extends AppCompatActivity {

    public static final String TAG = "DeckViewActivity";
    private List<ParseCard> cards;
    private ListAdapter adapter;
    private Deck deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_view);

        // Link up views
        Button btnDeleteDeck = findViewById(R.id.btnDeleteDeck);

        // Set up the RecyclerView
        RecyclerView rvDeckCards = findViewById(R.id.rvDeckCards);
        rvDeckCards.setLayoutManager(new GridLayoutManager(this, 3));
        cards = new ArrayList<>();
        adapter = new ListAdapter(cards);
        rvDeckCards.setAdapter(adapter);

        // Get intent data
        Intent intent = getIntent();
        String deckName = intent.getStringExtra("deckName");
        Log.i(TAG, "Got deck name: " + deckName);

        // Set scroll to false
        NewDeckActivity.scroll = false;
        getDeck(deckName);

        // Set on click listener for btnDeleteDeck
        btnDeleteDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete the deck and return by intent
                deck.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(DeckViewActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void getDeck(String deckName) {
        // Make the parse query and then update the adapter's data set
        ParseQuery<Deck> query = ParseQuery.getQuery(Deck.class);
        ParseUser user = ParseUser.getCurrentUser();
        // Set the query parameters
        query.setLimit(1);
        query.whereEqualTo("deckName", deckName);
        query.whereEqualTo("owner", user);
        query.findInBackground(new FindCallback<Deck>() {
            @Override
            public void done(List<Deck> objects, ParseException e) {
                deck = objects.get(0);
                List<ParseCard> cards2 = deck.getCards();
                for (ParseCard card : cards2) {
                    try {
                        Log.i(TAG, ((ParseCard) card.fetchIfNeeded()).getName());
                        cards.add(card);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}