package fragments;

import android.content.Context;
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
import android.widget.EditText;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.pokemoncatcherscatalogue.ParseApplication;
import com.example.pokemoncatcherscatalogue.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import adapters.CardAdapter;
import models.Card;
import okhttp3.Headers;

public class SearchFragment extends Fragment {

    public static final String TAG = "SearchFragment";
    private Context context;
    private CardAdapter adapter;
    private ArrayList<Card> cards;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        // Play the music
        assert context != null;
        ((ParseApplication)context.getApplicationContext()).startMediaPlayer(R.raw.lab);

        // Link up views
        RecyclerView rvSearch = view.findViewById(R.id.rvSearch);
        Button btnSearch = view.findViewById(R.id.btnSearch);
        final EditText etSearch = view.findViewById(R.id.etSearch);

        // Initialize cards
        cards = new ArrayList<>();
        // Create new adapter for recycler view
        adapter = new CardAdapter(cards);
        // Set the adapter
        rvSearch.setAdapter(adapter);
        // Set the layout manager
        rvSearch.setLayoutManager(new GridLayoutManager(context, 3));

        // Set on click listener for btnSearch
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the search parameter
                String param = etSearch.getText().toString();
                // Make the API call and populate the adapter
                searchCards(cards, param);
            }
        });
    }

    private void searchCards(final ArrayList<Card> cards, String param) {
        AsyncHttpClient client = ((ParseApplication) context.getApplicationContext()).getClient();
        RequestParams params = new RequestParams();
        params.put("name", param);

        client.get("https://api.pokemontcg.io/v1/cards", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess");

                // Clear the adapter first
                adapter.clear();

                // Get the cards array to access data
                JSONArray jsonArray;
                try {
                    jsonArray = json.jsonObject.getJSONArray("cards");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

                // Set the values in card through a loop
                for (int i = 0 ; i < jsonArray.length(); i++) {

                    // Create the card using the constructor
                    Card card;
                    try {
                        card = new Card(jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("id"),
                                jsonArray.getJSONObject(i).getString("imageUrl"), jsonArray.getJSONObject(i).getString("setCode"));
                    } catch (JSONException e) {
                        // This means there was a critical error with the API request
                        e.printStackTrace();
                        return;
                    }

                    // Get the optional parameter of type; not all cards have a valid number
                    try {
                        card.number = jsonArray.getJSONObject(i).getInt("number");
                    } catch (JSONException e) {
                        // Do nothing; it is expected that some cards do not have this
                    }

                    // Get the optional parameter of type; not all cards have a type
                    try {
                        card.type = jsonArray.getJSONObject(i).getJSONArray("types").getString(0);
                    } catch (JSONException e) {
                        // Do nothing; it is expected that some cards do not have this
                    }

                    // Get the optional parameter of hp; not all cards have hp
                    try {
                        card.setHp(jsonArray.getJSONObject(i).getString("hp"));
                    } catch (JSONException e) {
                        // Do nothing; it is expected that some cards do not have this
                    }

                    // Get the optional parameter of rarity; not all cards have a rarity
                    try {
                        card.setRarity(jsonArray.getJSONObject(i).getString("rarity"));
                    } catch (JSONException e) {
                        // Do nothing; it is expected that some cards do not have this
                    }

                    // Add the new card to the list of cards for the adapter
                    cards.add(card);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure, Error getting cards", throwable);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}