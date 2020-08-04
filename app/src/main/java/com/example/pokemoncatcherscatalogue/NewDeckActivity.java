package com.example.pokemoncatcherscatalogue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapters.ListAdapter;
import models.Deck;
import models.Listener;
import models.ParseCard;

// Note: Used this github: https://github.com/jkozh/DragDropTwoRecyclerViews/blob/master/app/src/main/java/com/example/julia/dragdroptworecyclerviews/MainActivity.java
public class NewDeckActivity extends AppCompatActivity implements Listener {

    public static final String TAG = "NewDeckActivity";
    private List<ParseCard> cards;
    private ListAdapter userCardsAdapter;
    public static boolean scroll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deck);

        // Link up views
        Switch switchScroll = findViewById(R.id.switch1);
        ImageView ivDeckLogo = findViewById(R.id.ivDeckLogo);
        ImageView ivProfileDeck = findViewById(R.id.ivProfileDeck);
        Button btnSaveDeck = findViewById(R.id.btnSaveDeck);
        final EditText etDeckName = findViewById(R.id.etDeckName);

        // Load the image with Glide
        Glide.with(this).load("https://cdn1.dotesports.com/wp-content/uploads/2020/02/22021537/494815920e06ed9b01f27f4a03da4033.jpg").into(ivDeckLogo);
        Glide.with(this).load(Objects.requireNonNull(ParseUser.getCurrentUser().getParseFile("profilePic")).getUrl()).into(ivProfileDeck);

        // Set up switch listener
        switchScroll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                scroll = !(b);
            }
        });

        // Set up the first RecyclerView
        RecyclerView rvUserCards = findViewById(R.id.rvUserCards);
        rvUserCards.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        cards = new ArrayList<>();
        userCardsAdapter = new ListAdapter(cards, this);
        rvUserCards.setAdapter(userCardsAdapter);
        rvUserCards.setOnDragListener(userCardsAdapter.getDragInstance());

        // Set up the second RecyclerView
        RecyclerView rvDeckCards = findViewById(R.id.rvDeckCards);
        rvDeckCards.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        final List<ParseCard> cards2 = new ArrayList<>();
        ListAdapter deckCardsAdapter = new ListAdapter(cards2, this);
        rvDeckCards.setAdapter(deckCardsAdapter);
        rvDeckCards.setOnDragListener(deckCardsAdapter.getDragInstance());

        // Query Parse to getCards and load them into the adapter
        getParseCards();

        // Set on click listener for the save deck button
        btnSaveDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Save deck clicked");
                // Error checking
                if (cards2.size() > 60) {
                    Toast.makeText(NewDeckActivity.this, "More than 60 cards added to the deck", Toast.LENGTH_SHORT).show();
                    return;
                } else if (etDeckName.getText().toString().isEmpty()) {
                    Toast.makeText(NewDeckActivity.this, "Must provide a deck name", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveDeck(cards2, etDeckName.getText().toString());
                Intent intent = new Intent(NewDeckActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveDeck(List<ParseCard> cards2, String deckName) {
        Deck deck = new Deck(ParseUser.getCurrentUser(), cards2, deckName);
        deck.saveInBackground();
    }

    @Override
    public void setEmptyListTop(boolean visibility) { }

    @Override
    public void setEmptyListBottom(boolean visibility) { }

    private void getParseCards() {
        // Query Parse and get all the cards and put them into the list
        ParseQuery<ParseCard> query = ParseQuery.getQuery(ParseCard.class);

        // Check if we are in privileged mode or not; if not load the other Users information
        ParseUser user = ParseUser.getCurrentUser();

        // Set up params
        query.whereEqualTo("owner", user);
        query.findInBackground(new FindCallback<ParseCard>() {
            @Override
            public void done(List<ParseCard> objects, ParseException e) {
                for (ParseCard card : objects) {
                    Log.i(TAG, card.getName() + card.getUrl());
                    if (card.getCount() >= 1) {
                        for (int i = 0; i < card.getCount(); i++) {
                            cards.add(new ParseCard(card));
                        }
                    }
                }
                userCardsAdapter.notifyDataSetChanged();
            }
        });
    }
}