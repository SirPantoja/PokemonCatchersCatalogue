package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokemoncatcherscatalogue.R;
import com.example.pokemoncatcherscatalogue.SeriesViewHolder;
import com.example.pokemoncatcherscatalogue.SetViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

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
