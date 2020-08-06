package fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.pokemoncatcherscatalogue.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import adapters.FriendAdapter;
import models.Friend;

public class FriendsFragment extends Fragment {

    public static final String TAG = "FriendsFragment";
    protected FriendAdapter adapter;
    protected List<Friend> friends;
    private EditText etFriendUsername;


    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getContext();

        // Set up the Recycler View and other views
        RecyclerView rvFriends = view.findViewById(R.id.rvFriends);
        etFriendUsername = view.findViewById(R.id.etFriendUsername);
        Button btnAdd = view.findViewById(R.id.btnAdd);

        // Initialize the friends list
        friends = new ArrayList<>();
        // Create the adapter and pass in friends
        adapter = new FriendAdapter(friends);
        // Attach the adapter to the recycler view
        rvFriends.setAdapter(adapter);
        // Attach the layout manager to the recycler view
        rvFriends.setLayoutManager(new LinearLayoutManager(context));

        // Set on click listener for btnAdd
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFriend(etFriendUsername.getText().toString());
            }
        });

        // Set up swipe to refresh
        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                friends.clear();
                getFriends(friends);
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // populate the friends list by querying the Parse database
        getFriends(friends);
    }

    private void addFriend(String username) {
        Log.i(TAG, username);
        // Query Parse to find this user
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        query.setLimit(1);                                                  // Since we only want to retrieve a single user at most
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects.isEmpty()) {
                        // Error checking. Did not find user
                        Log.i(TAG, "Could not find user");
                    } else {
                        Log.i(TAG, "Added User");
                        ParseUser newFriendUser = objects.get(0);
                        Friend newFriend = new Friend(ParseUser.getCurrentUser(), newFriendUser);
                        newFriend.saveInBackground();
                    }
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });

    }

    private void getFriends(final List<Friend> friends) {
        // Query Parse to get list of friends
        List<ParseQuery<Friend>> queries = new ArrayList<>();
        queries.add(ParseQuery.getQuery(Friend.class));
        queries.get(0).whereEqualTo("ptrUser", ParseUser.getCurrentUser());
        queries.add(ParseQuery.getQuery(Friend.class));
        queries.get(1).whereEqualTo("actualFriend", ParseUser.getCurrentUser());
        ParseQuery<Friend> query = ParseQuery.or(queries);
        query.findInBackground(new FindCallback<Friend>() {
            public void done(List<Friend> itemList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    if (itemList.isEmpty()) {
                        // This means this user has no friends
                        return;
                    } else {
                        // Loop across itemList and add friends to friends
                        for (Friend friend : itemList) {
                            // Check if the friend fields are out of order
                            try {
                                Log.i(TAG, "User: " + friend.getUser().fetchIfNeeded().getUsername() + " Friend: " + friend.getFriend().fetchIfNeeded().getUsername());
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                            try {
                                if (friend.getFriend().fetchIfNeeded().getUsername().equals(ParseUser.getCurrentUser().fetchIfNeeded().getUsername()))
                                {
                                    Log.i(TAG, "In here");
                                    // Switch the two fields
                                    friend.setFriend(friend.getUser());
                                    friend.setUser(ParseUser.getCurrentUser());
                                }
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                            friends.add(friend);
                        }
                        // Since we incremented friends we have to let the adapter know so the view changes
                        adapter.notifyDataSetChanged();
                    }
                    Log.i(TAG, "Success");
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
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }
}