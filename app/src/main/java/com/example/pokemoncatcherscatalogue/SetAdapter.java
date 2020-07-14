package com.example.pokemoncatcherscatalogue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {

    private List<Set> sets;
    private Context context;
    public SetAdapter (List<Set> sets) {
        this.sets = sets;
    }

    @NonNull
    @Override
    public SetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        // Get the data based off the position
        Set set = sets.get(position);

        // Set the item views based on the given data
        ImageView ivLogo = holder.ivLogo;
        TextView tvSetName = holder.tvSetName;

        // Put in the data
        tvSetName.setText(set.getName());
        Glide.with(context).load(set.getLogoUrl()).into(ivLogo);
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivLogo;
        public TextView tvSetName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Link up views
            ivLogo = itemView.findViewById(R.id.ivLogo);
            tvSetName = itemView.findViewById(R.id.tvSetName);
        }
    }
}
