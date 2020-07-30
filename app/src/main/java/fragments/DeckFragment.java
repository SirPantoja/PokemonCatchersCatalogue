package fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.pokemoncatcherscatalogue.NewDeckActivity;
import com.example.pokemoncatcherscatalogue.ParseApplication;
import com.example.pokemoncatcherscatalogue.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import adapters.DeckAdapter;
import models.Deck;
import models.ParseCard;

public class DeckFragment extends Fragment {

    public static final String TAG = "DeckFragment";
    private Context context;
    private List<Deck> decks;
    private DeckAdapter adapter;

    public DeckFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        // Link up views
        ImageView ivDeck = view.findViewById(R.id.ivDeck);
        Button btnDeck = view.findViewById(R.id.btnDeck);
        RecyclerView rvDeck = view.findViewById(R.id.rvDeck);

        // Load data into views
        Glide.with(context).load("https://cdn1.dotesports.com/wp-content/uploads/2020/02/22021537/494815920e06ed9b01f27f4a03da4033.jpg").into(ivDeck);

        // Set on click listener for btnDeck
        btnDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "New deck!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, NewDeckActivity.class);
                context.startActivity(intent);
            }
        });

        // Initialize decks
        decks = new ArrayList<>();
        // Create the adapter
        adapter = new DeckAdapter(decks);
        // Set the adapter to the recycler view
        rvDeck.setAdapter(adapter);
        // Set the layout manager
        rvDeck.setLayoutManager(new GridLayoutManager(context, 3));

        getDecks();
    }

    private void getDecks() {
        // Make the parse query and then update the adapter's data set
        ParseQuery<Deck> query = ParseQuery.getQuery(Deck.class);
        ParseUser user = ParseUser.getCurrentUser();
        // Set the query parameters
        query.whereEqualTo("owner", user);
        query.findInBackground(new FindCallback<Deck>() {
            public void done(List<Deck> itemList, ParseException e) {
                if (e == null) {
                    Log.i(TAG, "Made query");
                    decks.addAll(itemList);
                    // Since we incremented card.count we have to let the adapter know so the view changes
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deck, container, false);
    }
}