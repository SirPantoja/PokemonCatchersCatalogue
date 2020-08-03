package adapters;

import android.content.Context;
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
    private ArrayList<Integer> stats;
    private Context context;

    public MissingCardAdapter(ArrayList<Card> cards, ArrayList<Integer> stats) {
        this.cards = cards;
        this.stats = stats;
    }

    @NonNull
    @Override
    public MissingCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewItem;

        // Inflate the custom layout
        if (viewType == 1) {
            // We aren't at the first first item so inflate normally
            viewItem = inflater.inflate(R.layout.missing_card_item, parent, false);
        } else {
            // We are at the first item so inflate a different layout
            viewItem = inflater.inflate(R.layout.missing_statistics, parent, false);
        }

        // Return a new holder instance
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MissingCardAdapter.ViewHolder holder, int position) {
        if (position == 0) {
            // We are at the header
            if (stats.isEmpty()) {
                return;
            }

            // Declare variables for ease of readability
            int total = stats.get(0);
            int uniqueCards = stats.get(1);
            int rares = stats.get(2);
            int uncommons = stats.get(3);
            int commons = stats.get(4);

            // Since stats is not empty we can populate the views
            holder.tvTotalCards2.setText(String.valueOf(total));
            holder.tvRepeats2.setText(String.valueOf(total - uniqueCards));
            holder.tvUniqueCards2.setText(String.valueOf(uniqueCards));
            holder.tvRares2.setText(String.valueOf(rares));
            holder.tvUncommons2.setText(String.valueOf(uncommons));
            holder.tvCommons2.setText(String.valueOf(commons));
            holder.tvOther2.setText(String.valueOf(uniqueCards - rares - uncommons - commons));
        } else {
            // Get the current card minus one since we have a header
            Card card = cards.get(position - 1);

            // Get view references from the view holder
            ImageView ivMissingCard = holder.ivMissingCard;
            TextView tvMissingCardName = holder.tvMissingCardName;

            // Put in the data to the views
            Glide.with(context).load(card.url).into(ivMissingCard);
            tvMissingCardName.setText(card.name);
        }
    }

    @Override
    public int getItemCount() {
        // Plus one since we have a header
        return cards.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivMissingCard;
        public TextView tvMissingCardName;
        public TextView tvTotalCards2;
        public TextView tvRepeats2;
        public TextView tvUniqueCards2;
        public TextView tvRares2;
        public TextView tvUncommons2;
        public TextView tvCommons2;
        public TextView tvOther2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Link up views
            ivMissingCard = itemView.findViewById(R.id.ivMissingCard);
            tvMissingCardName = itemView.findViewById(R.id.tvMissingCardName);

            // Link up header views
            tvTotalCards2 = itemView.findViewById(R.id.tvTotalCards2);
            tvRepeats2 = itemView.findViewById(R.id.tvRepeats2);
            tvUniqueCards2 = itemView.findViewById(R.id.tvUniqueCards2);
            tvRares2 = itemView.findViewById(R.id.tvRares2);
            tvUncommons2 = itemView.findViewById(R.id.tvUncommons2);
            tvCommons2 = itemView.findViewById(R.id.tvCommons2);
            tvOther2 = itemView.findViewById(R.id.tvOther2);
        }
    }

    public void clear() {
        cards.clear();
    }

    public void addAll(ArrayList<Card> cards) {
        this.cards.addAll(cards);
        notifyDataSetChanged();
    }

    // If we are at position 0 we want to know and inflate a different layout
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

}
