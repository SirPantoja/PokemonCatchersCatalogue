package adapters;

import android.app.Activity;
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
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokemoncatcherscatalogue.R;
import com.example.pokemoncatcherscatalogue.SeriesViewHolder;
import com.example.pokemoncatcherscatalogue.SetStatistics;
import com.example.pokemoncatcherscatalogue.SetViewHolder;
import com.example.pokemoncatcherscatalogue.SingleSetActivity;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import org.parceler.Parcels;

import java.util.List;

import models.Set;

public class SetAdapter extends ExpandableRecyclerViewAdapter<SeriesViewHolder, SetViewHolder> {

    public SetAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public SeriesViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.series_item, parent, false);
        return new SeriesViewHolder(view);
    }

    @Override
    public SetViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item, parent, false);
        return new SetViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(SetViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Set set = (Set) group.getItems().get(childIndex);
        holder.setAll(set);
    }

    @Override
    public void onBindGroupViewHolder(SeriesViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setTvSeriesName(group);
    }
}
