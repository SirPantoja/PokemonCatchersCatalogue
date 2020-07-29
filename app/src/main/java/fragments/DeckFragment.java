package fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.pokemoncatcherscatalogue.R;

public class DeckFragment extends Fragment {

    private Context context;

    public DeckFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        // Link up views
        ImageView ivDeck = view.findViewById(R.id.ivDeck);

        // Load data into views
        Glide.with(context).load("https://cdn1.dotesports.com/wp-content/uploads/2020/02/22021537/494815920e06ed9b01f27f4a03da4033.jpg").into(ivDeck);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deck, container, false);
    }
}