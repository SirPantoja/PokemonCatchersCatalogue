package fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.pokemoncatcherscatalogue.R;
import models.Set;
import adapters.SetAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class SetFragment extends Fragment {

    public static final String TAG = "SetFragment";
    private RecyclerView rvSets;
    protected SetAdapter adapter;
    protected List<Set> sets;
    private Context context;

    public SetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        // Set up the Recycler View
        rvSets = view.findViewById(R.id.rvSets);
        sets = new ArrayList<>();
        adapter = new SetAdapter(sets);
        rvSets.setAdapter(adapter);
        rvSets.setLayoutManager(new LinearLayoutManager(getContext()));

        // Make the API call for sets
        Log.i(TAG, "About to call getSets");
        getSets(sets);
    }

    // Get the set list from the API
    private void getSets(final List<Set> sets) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.get("https://api.pokemontcg.io/v1/sets", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JsonHttpResponseHandler.JSON json) {
                Log.i(TAG, "onSuccess");
                // Access a JSON object response with `json.jsonObject`
                Log.d(TAG, json.jsonObject.toString());
                JSONArray jsonArray;
                JSONObject jsonObject = json.jsonObject;
                try {
                    jsonArray = jsonObject.getJSONArray("sets");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
                Log.i(TAG, jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        // Populate the set object
                        Set set = new Set(jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("logoUrl"),
                                jsonArray.getJSONObject(i).getString("symbolUrl"), jsonArray.getJSONObject(i).getString("code"),
                                jsonArray.getJSONObject(i).getInt("totalCards"));
                        // Add the set to sets
                        sets.add(set);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure");
                // TODO throw throwable
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set, container, false);
    }
}