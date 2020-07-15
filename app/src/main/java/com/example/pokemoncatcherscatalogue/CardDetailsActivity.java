package com.example.pokemoncatcherscatalogue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import models.Card;

public class CardDetailsActivity extends AppCompatActivity {

    public static final String TAG = "CardDetailsActivity";
    private ImageView ivCardDetails;
    private TextView tvCardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);

        // Link up views
        ivCardDetails = findViewById(R.id.ivCardDetails);
        tvCardName = findViewById(R.id.tvCardName);

        // Get the intent
        Intent intent = getIntent();
        Card card = Parcels.unwrap(getIntent().getParcelableExtra("card"));

        // Fill up the views
        tvCardName.setText(card.getName());
        Glide.with(this).load(card.getUrl()).into(ivCardDetails);
    }
}