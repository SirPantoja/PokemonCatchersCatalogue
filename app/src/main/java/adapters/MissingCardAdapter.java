package adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokemoncatcherscatalogue.R;

import java.util.ArrayList;

import models.Card;

public class MissingCardAdapter extends RecyclerView.Adapter<MissingCardAdapter.ViewHolder> {

    public static final String TAG = "MissingCardAdapter";
    private ArrayList<Card> cards;
    private Context context;

    public MissingCardAdapter(ArrayList<Card> cards) {
        this.cards = cards;
    }

    @NonNull
    @Override
    public MissingCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.missing_card_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull MissingCardAdapter.ViewHolder holder, int position) {
        // Get the current card
        Card card = cards.get(position);

        // Get view references from the view holder
        ImageView ivMissingCard = holder.ivMissingCard;
        TextView tvMissingCardName = holder.tvMissingCardName;

        // Put in the data to the views
        Glide.with(context).load(card.url).into(ivMissingCard);
        tvMissingCardName.setText(card.name);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivMissingCard;
        public TextView tvMissingCardName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Link up views
            ivMissingCard = itemView.findViewById(R.id.ivMissingCard);
            tvMissingCardName = itemView.findViewById(R.id.tvMissingCardName);
        }
    }

    public void clear() {
        cards.clear();
    }

    public void addAll(ArrayList<Card> cards) {
        this.cards.addAll(cards);
        notifyDataSetChanged();
    }
}
