package fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokemoncatcherscatalogue.R;

import java.util.ArrayList;
import java.util.List;

import adapters.FriendAdapter;
import models.Friend;

public class FriendsFragment extends Fragment {

    public static final String TAG = "FriendsFragment";
    private RecyclerView rvFriends;
    protected FriendAdapter adapter;
    protected List<Friend> friends;
    private Context context;


    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        // Set up the Recycler View
        rvFriends = view.findViewById(R.id.rvFriends);

        // Initialize the friends list
        friends = new ArrayList<Friend>();
        // Create the adapter and pass in friends
        adapter = new FriendAdapter(friends);
        // Attach the adapter to the recycler view
        rvFriends.setAdapter(adapter);
        // Attach the layout manager to the recycler view
        rvFriends.setLayoutManager(new LinearLayoutManager(context));

        // TODO populate the friends list by querying the Parse database

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }
}