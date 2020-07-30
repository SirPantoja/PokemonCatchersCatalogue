package fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.pokemoncatcherscatalogue.ParseApplication;
import com.example.pokemoncatcherscatalogue.R;

import models.Series;
import models.Set;
import adapters.SetAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.SetDao;
import okhttp3.Headers;

public class SetFragment extends Fragment {

    public static final String TAG = "SetFragment";
    private RecyclerView rvSets;
    protected SetAdapter adapter;
    protected List<Set> sets;
    protected List<Series> series;
    private Context context;
    private SetDao setDao;
    private SwipeRefreshLayout swipeContainer;

    public SetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        // Set up the data access object
        setDao = ((ParseApplication) context.getApplicationContext()).getMyDatabase().setDao();

        // Set up the Recycler View
        rvSets = view.findViewById(R.id.rvSets);
        sets = new ArrayList<>();
        series = new ArrayList<>();
        /*series = setsToSeries(sets);
        adapter = new SetAdapter(series);
        rvSets.setAdapter(adapter);*/
        rvSets.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up the swipeContainer
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                getSets(sets);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Query the database for existing set data
        List<Set> newSets = new ArrayList<>();
        newSets = setDao.getAll();
        for (Set set : newSets) {
            Log.i(TAG, "Retrieving" + set.getName());
            sets.add(set);
        }


        // Make the API call for sets only if there are no sets in SQLite
        Log.i(TAG, "About to call getSets");
        if (newSets.isEmpty()) {
            getSets(sets);
        } else {
            setsToSeries(sets);
            adapter = new SetAdapter(series);
            rvSets.setAdapter(adapter);
        }
    }

    // Get the set list from the API
    private void getSets(final List<Set> sets) {
        AsyncHttpClient client = ((ParseApplication) context.getApplicationContext()).getClient();
        RequestParams params = new RequestParams();
        sets.clear();

        client.get("https://api.pokemontcg.io/v1/sets", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JsonHttpResponseHandler.JSON json) {
                Log.i(TAG, "onSuccess");
                JSONArray jsonArray;
                JSONObject jsonObject = json.jsonObject;
                try {
                    jsonArray = jsonObject.getJSONArray("sets");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
                // Create a new set list to store fresh data
                final List<Set> newSets = new ArrayList<>();
                Log.i(TAG, jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        // Populate the set object
                        Set set = new Set(jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("logoUrl"),
                                jsonArray.getJSONObject(i).getString("symbolUrl"), jsonArray.getJSONObject(i).getString("code"),
                                jsonArray.getJSONObject(i).getInt("totalCards"), jsonArray.getJSONObject(i).getString("series"));
                        // Add the set to sets
                        newSets.add(set);
                        sets.add(set);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                // Query the database for existing set data
                Log.i(TAG, "Entered the AsyncTask for saving");
                setDao.insertSet(newSets.toArray(new Set[0]));
                for (Set set : newSets) {
                    Log.i(TAG, "Saving" + set.getName());
                }
                Log.i(TAG, "DATA SET ABOUT TO BE CHANGED");
                setsToSeries(sets);
                adapter = new SetAdapter(series);
                rvSets.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                // Set the swipeContainer to false in case that was what called getSets
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure", throwable);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set, container, false);
    }

    // Takes a list of sets, finds the series from the sets and then edits each series to have an associated list of sets
    private void setsToSeries(List<Set> sets) {
        Log.i(TAG, "setsToSeries");
        series.clear();
        // Want to preserve series order so use ordered hash map
        LinkedHashMap<String, List<Set>> titles = new LinkedHashMap<>();
        for (Set set : sets) {
            if (!(titles.containsKey(set.getSeries()))) {
                // Create a new entry since not already contained
                Log.i(TAG, "Title: " + set.getSeries());
                ArrayList<Set> newList = new ArrayList<>();
                newList.add(set);
                titles.put(set.getSeries(), newList);
            } else {
                // Already contains this series so just add it
                titles.get(set.getSeries()).add(set);
            }
        }

        // Getting an iterator
        Iterator hmIterator = titles.entrySet().iterator();

        // Iterate through the hash map and add to the series object
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            series.add(new Series((String)mapElement.getKey(), (List)mapElement.getValue()));
            Log.i(TAG, "Adding: " +  (String)mapElement.getKey() + " and: " + ((List)mapElement.getValue()).toString());
        }
    }
}