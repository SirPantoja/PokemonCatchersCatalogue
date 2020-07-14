package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokemoncatcherscatalogue.R;
import com.example.pokemoncatcherscatalogue.Set;
import com.example.pokemoncatcherscatalogue.SetAdapter;

import java.util.ArrayList;
import java.util.List;

public class SetFragment extends Fragment {

    public static final String TAG = "SetFragment";
    private RecyclerView rvSets;
    protected SetAdapter adapter;
    protected List<Set> sets;

    public SetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the Recycler View
        rvSets = view.findViewById(R.id.rvSets);
        sets = getSets();
        adapter = new SetAdapter(sets);
        rvSets.setAdapter(adapter);
        rvSets.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    // Get the set list from the API
    private List<Set> getSets() {
        return new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set, container, false);
    }
}