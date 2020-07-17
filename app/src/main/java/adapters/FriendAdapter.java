package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokemoncatcherscatalogue.R;
import com.parse.ParseException;

import java.util.List;

import models.Friend;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<Friend> friends;
    private Context context;

    public FriendAdapter(List<Friend> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View friendView = inflater.inflate(R.layout.friend_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(friendView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
        // Get the friend based on position
        Friend friend = friends.get(position);

        // Set item views
        TextView tvFriendUsername = holder.tvFriendUsername;
        ImageView ivProfilePic = holder.ivProfilePic;

        // Populate the views
        try {
            tvFriendUsername.setText(friend.getFriend().fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Glide.with(context).load(friend.getProfilePic().getUrl()).into(ivProfilePic);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFriendUsername;
        public ImageView ivProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Link up the views
            tvFriendUsername = itemView.findViewById(R.id.tvFriendUsername);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        }
    }
}
