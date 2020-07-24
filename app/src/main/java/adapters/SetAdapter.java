package adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokemoncatcherscatalogue.R;
import com.example.pokemoncatcherscatalogue.SetStatistics;
import com.example.pokemoncatcherscatalogue.SingleSetActivity;

import org.parceler.Parcels;

import java.util.List;

import models.Set;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {

    public static final String TAG = "SetAdapter";
    private static final long FADE_DURATION = 1250;
    private List<Set> sets;
    private Context context;
    public SetAdapter (List<Set> sets) {
        this.sets = sets;
    }

    @NonNull
    @Override
    public SetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View setView = inflater.inflate(R.layout.set_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(setView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SetAdapter.ViewHolder holder, int position) {
        Log.i(TAG, "OnBindViewHolder");
        // Get the data based off the position
        final Set set = sets.get(position);

        // Set the item views based on the given data
        ImageView ivLogo = holder.ivLogo;
        TextView tvSetName = holder.tvSetName;
        RelativeLayout rlSet = holder.rlSet;
        Button btnStatistics = holder.btnStatistics;

        // Put in the data
        tvSetName.setText(set.getName());
        Glide.with(context).load(set.getLogoUrl()).into(ivLogo);

        // Set up the on click listener for rlSet
        rlSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start an intent to go to the SingleSetActivity
                Intent intent = new Intent(context, SingleSetActivity.class);
                intent.putExtra("code", set.getCode());
                intent.putExtra("logo", set.getLogoUrl());
                context.startActivity(intent);
            }
        });

        // Set on click listener to go to the SetStatistics activity
        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SetStatistics.class);
                intent.putExtra("code", set.getCode());
                intent.putExtra("logo", set.getLogoUrl());
                intent.putExtra("symbol", set.getSymbolUrl());
                intent.putExtra("name", set.getName());
                intent.putExtra("total", set.getTotalCards());
                context.startActivity(intent);
            }
        });

        // Set the view to fade in
        setFadeAnimation(holder.itemView);
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivLogo;
        public TextView tvSetName;
        public RelativeLayout rlSet;
        public Button btnStatistics;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Link up views
            ivLogo = itemView.findViewById(R.id.ivLogo);
            tvSetName = itemView.findViewById(R.id.tvSetName);
            rlSet = itemView.findViewById(R.id.rlSet);
            btnStatistics = itemView.findViewById(R.id.btnStatistics);
        }
    }

    // Clears the adapter
    public void clear() {
        sets.clear();
        notifyDataSetChanged();
    }

    // Adds all the elements from the given list to the data set
    public void addAll(List<Set> sets) {
        this.sets.addAll(sets);
        notifyDataSetChanged();
    }
}
