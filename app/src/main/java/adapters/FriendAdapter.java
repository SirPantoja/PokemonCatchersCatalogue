package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokemoncatcherscatalogue.ParseApplication;
import com.example.pokemoncatcherscatalogue.R;
import com.parse.ParseException;

import java.util.List;

import models.Friend;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    public static final String TAG = "FriendAdapter";
    private static final long FADE_DURATION = 500;
    private List<Friend> friends;
    private Context context;
    private Context appContext;

    public FriendAdapter(List<Friend> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        appContext = context.getApplicationContext();
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
        final Friend friend = friends.get(position);

        // Set item views
        TextView tvFriendUsername = holder.tvFriendUsername;
        TextView tvBio = holder.tvBio;
        ImageView ivProfilePic = holder.ivProfilePic;

        // Set on click listener for the button
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the virtual user up for collection viewing
                try {
                    ((ParseApplication) appContext).setGuestUser(friend.getFriend().fetchIfNeeded());
                } catch (ParseException e) {
                    Log.e(TAG, "Could not change user privilege");
                    e.printStackTrace();
                }
            }
        });

        // Populate the views
        try {
            tvFriendUsername.setText(friend.getFriend().fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvBio.setText(friend.getBio());
        Glide.with(context).load(friend.getProfilePic().getUrl()).into(ivProfilePic);

        setScaleAnimation(holder.itemView);
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFriendUsername;
        public TextView tvBio;
        public ImageView ivProfilePic;
        public Button btnView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Link up the views
            tvFriendUsername = itemView.findViewById(R.id.tvFriendUsername);
            tvBio = itemView.findViewById(R.id.tvBio);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            btnView = itemView.findViewById(R.id.btnView);
        }
    }
}
