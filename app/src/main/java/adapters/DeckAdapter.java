package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokemoncatcherscatalogue.R;

import java.util.List;

import models.Deck;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.ViewHolder> {

    private List<Deck> decks;
    private Context context;

    public DeckAdapter(List<Deck> decks) {
        this.decks = decks;
    }

    @NonNull
    @Override
    public DeckAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.deck_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckAdapter.ViewHolder holder, int position) {
        // Get the correct deck based on position
        Deck deck = decks.get(position);

        // Link up views
        ImageView ivDeckCover = holder.ivDeckCover;
        TextView tvDeckName = holder.tvDeckName;
        RelativeLayout rlDeck = holder.rlDeck;

        // Insert data into views
        Glide.with(context).load(deck.getDeckCover().getUrl()).into(ivDeckCover);
        tvDeckName.setText(deck.getDeckName());

        // Set an on click listener for the relative layout
        rlDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Deck Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return decks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivDeckCover;
        public TextView tvDeckName;
        public RelativeLayout rlDeck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Link up views
            ivDeckCover = itemView.findViewById(R.id.ivDeckCover);
            tvDeckName = itemView.findViewById(R.id.tvDeckName);
            rlDeck = itemView.findViewById(R.id.rlDeck);
        }
    }
}
