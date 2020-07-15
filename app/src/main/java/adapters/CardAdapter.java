package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokemoncatcherscatalogue.CardDetailsActivity;
import com.example.pokemoncatcherscatalogue.R;
import com.example.pokemoncatcherscatalogue.SingleSetActivity;

import org.parceler.Parcels;

import java.util.List;

import models.Card;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

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

        // Return a ew holder instance
        ViewHolder viewHolder = new ViewHolder(cardView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {
        // Get the right card based off position
        final Card card = cards.get(position);

        // Set the item views based off card
        TextView tvCardName = holder.tvCardName;
        tvCardName.setText(card.getName());
        ImageView ivCard = holder.ivCard;
        Glide.with(context).load(card.getUrl()).into(ivCard);

        // Set on click listener for ivCard
        ivCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SingleSetActivity.isChecked) {
                    // TODO Increment the card count for this card
                    Toast.makeText(context, "Incremented", Toast.LENGTH_SHORT).show();
                } else {
                    // Use an intent to go the CardDetailsActivity since we are not in edit mode
                    Intent intent = new Intent(context, CardDetailsActivity.class);
                    intent.putExtra("card", Parcels.wrap(card));
                    context.startActivity(intent);
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
        public ImageView ivCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardName = itemView.findViewById(R.id.tvCardName);
            ivCard = itemView.findViewById(R.id.ivCard);
        }
    }
}
