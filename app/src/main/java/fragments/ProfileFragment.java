package fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pokemoncatcherscatalogue.MainActivity;
import com.example.pokemoncatcherscatalogue.R;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    private Button btnLogout;
    private ImageView ivProfile;
    private TextView tvUsername;
    private TextView tvBio;
    private Context context;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        // Link up views
        btnLogout = view.findViewById(R.id.btnLogout);
        ivProfile = view.findViewById(R.id.ivProfile);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvBio = view.findViewById(R.id.tvBio);

        // Set on click listener for button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Logout clicked", Toast.LENGTH_SHORT).show();
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // This will now be null
                // Send the user back to the login page
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

        ParseUser user = ParseUser.getCurrentUser();
        tvUsername.setText(user.getUsername());
        tvBio.setText(user.getString("bio"));
        Glide.with(view).load(user.getParseFile("profilePic").getUrl()).centerCrop().into(ivProfile);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}