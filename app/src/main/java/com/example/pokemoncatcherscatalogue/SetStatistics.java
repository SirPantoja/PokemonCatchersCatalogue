package com.example.pokemoncatcherscatalogue;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import models.ParseCard;

public class SetStatistics extends AppCompatActivity {

    public static final String TAG = "SetStatistics";
    private String setCode;
    private TextView tvTotalCards2;
    private TextView tvRepeats2;
    private TextView tvUniqueCards2;
    private TextView tvRares2;
    private TextView tvUncommons2;
    private TextView tvCommons2;
    private TextView tvOther2;
    private int totalCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_statistics);

        // Get data from intent
        Intent intent = getIntent();
        setCode = intent.getStringExtra("code");
        String logoUrl = intent.getStringExtra("logo");
        String symbolUrl = intent.getStringExtra("symbol");
        String setName = intent.getStringExtra("name");
        totalCards = intent.getIntExtra("total", 0);

        // Parse the setName if needed
        if (setName.length() >= 20) {
            setName = setName.substring(0, Math.min(setName.length(), 20)) + "...";
        }

        // Link up views
        ImageView ivSetLogo = findViewById(R.id.ivSetLogo);
        ImageView ivSymbol = findViewById(R.id.ivSymbol);
        TextView tvSetName = findViewById(R.id.tvSetName);
        tvTotalCards2 = findViewById(R.id.tvTotalCards2);
        tvRepeats2 = findViewById(R.id.tvRepeats2);
        tvUniqueCards2 = findViewById(R.id.tvUniqueCards2);
        tvRares2 = findViewById(R.id.tvRares2);
        tvUncommons2 = findViewById(R.id.tvUncommons2);
        tvCommons2 = findViewById(R.id.tvCommons2);
        tvOther2 = findViewById(R.id.tvOther2);

        // Add content to views
        Glide.with(this).load(logoUrl).into(ivSetLogo);
        Glide.with(this).load(symbolUrl).into(ivSymbol);
        tvSetName.setText(setName);

        // Set up the remaining statistical values after querying Parse
        getCards();
    }

    private void getCards() {
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
                    tvTotalCards2.setText(String.valueOf(total));
                    tvRepeats2.setText(String.valueOf(total - uniqueCards));
                    tvUniqueCards2.setText(uniqueCards + "/" + totalCards);
                    tvRares2.setText(String.valueOf(rares));
                    tvUncommons2.setText(String.valueOf(uncommons));
                    tvCommons2.setText(String.valueOf(commons));
                    tvOther2.setText(String.valueOf(uniqueCards - rares - uncommons - commons));
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }
}