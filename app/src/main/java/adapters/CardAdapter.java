package adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokemoncatcherscatalogue.CardDetailsActivity;
import com.example.pokemoncatcherscatalogue.R;
import com.example.pokemoncatcherscatalogue.SingleSetActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

import models.Card;
import models.ParseCard;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    public static final String TAG = "CardAdapter";
    private List<Card> cards;
    private Context context;

    public CardAdapter(List<Card> cards) {
        this.cards = cards;
    }

    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View cardView = inflater.inflate(R.layout.card_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(cardView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {
        // Get the right card based off position
        final Card card = cards.get(position);

        // Set the item views based off card
        TextView tvCardName = holder.tvCardName;
        tvCardName.setText(card.name);
        TextView tvCount = holder.tvCount;
        tvCount.setText("Count: " + card.count);
        ImageView ivCard = holder.ivCard;
        Glide.with(context).load(card.url).into(ivCard);
        Button btnAdd = holder.btnAdd;
        Button btnSub = holder.btnSub;

        // Set the buttons to GONE; this will change based on which edit mode we are in
        if (SingleSetActivity.isChecked) {
            btnAdd.setVisibility(View.VISIBLE);
            btnSub.setVisibility(View.VISIBLE);
        } else {
            btnAdd.setVisibility(View.GONE);
            btnSub.setVisibility(View.GONE);
        }

        // Set on click listener for buttons and have them add and subtract cards
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCard(card);
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCard(card);
            }
        });

        // Set on click listener for ivCard
        ivCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(SingleSetActivity.isChecked)) {
                    // Use an intent to go the CardDetailsActivity since we are not in edit mode
                    Intent intent = new Intent(context, CardDetailsActivity.class);
                    intent.putExtra("card", Parcels.wrap(card));
                    context.startActivity(intent);
                }
            }
        });
    }

    private void addCard(final Card card) {
        // Increment the card count field and notify the adapter
        card.count++;
        notifyDataSetChanged();
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
                        // This means this card doesn't already exist so we need to create it
                        ParseCard parseCard = new ParseCard(card.setCode, card.number, ParseUser.getCurrentUser(), 1, card.name, card.id);
                        parseCard.saveInBackground();
                    } else {
                        // This means we found the card so we just need to increment it
                        ParseCard newParseCard = itemList.get(0);
                        newParseCard.incrementCount();
                        newParseCard.saveInBackground();
                    }
                    Log.i(TAG, "Successfully added a card to Parse");
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    private void removeCard(final Card card) {
        // Decrement the count field and notify the adapter
        card.count--;
        notifyDataSetChanged();
        // First query Parse to see if the card already exists
        ParseQuery<ParseCard> query = ParseQuery.getQuery(ParseCard.class);
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.whereEqualTo("cardId", card.id);
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseCard>() {
            public void done(List<ParseCard> itemList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    if (!(itemList.isEmpty())) {
                        // This means we found the card so we just need to decrement it
                        ParseCard newParseCard = itemList.get(0);
                        newParseCard.decrementCount();
                        newParseCard.saveInBackground();
                    }
                    Log.i(TAG, "Successfully removed a card from Parse");
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCardName;
        public TextView tvCount;
        public ImageView ivCard;
        public Button btnAdd;
        public Button btnSub;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardName = itemView.findViewById(R.id.tvCardName);
            tvCount = itemView.findViewById(R.id.tvCount);
            ivCard = itemView.findViewById(R.id.ivCard);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnSub = itemView.findViewById(R.id.btnSub);
        }
    }
}
