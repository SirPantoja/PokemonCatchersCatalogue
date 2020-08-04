package com.example.pokemoncatcherscatalogue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import adapters.ListAdapter;
import models.Card;
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

        Switch switchScroll = findViewById(R.id.switch1);

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
        List<ParseCard> cards2 = new ArrayList<>();
        ListAdapter deckCardsAdapter = new ListAdapter(cards2, this);
        rvDeckCards.setAdapter(deckCardsAdapter);
        rvDeckCards.setOnDragListener(deckCardsAdapter.getDragInstance());

        // Query Parse to getCards and load them into the adapter
        getParseCards();
    }

    @Override
    public void setEmptyListTop(boolean visibility) {

    }

    @Override
    public void setEmptyListBottom(boolean visibility) {

    }

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
                cards.addAll(objects);
                for (ParseCard card : objects) {
                    Log.i(TAG, card.getName() + card.getUrl());
                }
                userCardsAdapter.notifyDataSetChanged();
            }
        });
    }
}