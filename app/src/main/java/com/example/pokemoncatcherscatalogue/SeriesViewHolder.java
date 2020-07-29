package com.example.pokemoncatcherscatalogue;

import android.content.Context;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class SeriesViewHolder extends GroupViewHolder {

    private TextView tvSeriesName;
    private ImageView ivArrow;
    private Context context;

    public SeriesViewHolder(View itemView) {
        super(itemView);
        tvSeriesName = itemView.findViewById(R.id.tvSeriesName);
        ivArrow = itemView.findViewById(R.id.ivArrow);
        context = itemView.getContext();
    }

    public void setTvSeriesName(ExpandableGroup group) {
        tvSeriesName.setText(group.getTitle());
    }

    @Override
    public void expand() {
        ivArrow.animate().rotation(90f).setDuration(500);
    }

    @Override
    public void collapse() {
        ivArrow.animate().rotation(0f).setDuration(500);
    }
}
