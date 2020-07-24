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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pokemoncatcherscatalogue.HomeActivity;
import com.example.pokemoncatcherscatalogue.MainActivity;
import com.example.pokemoncatcherscatalogue.ParseApplication;
import com.example.pokemoncatcherscatalogue.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    private Button btnLogout;
    private Button btnUpdate;
    private Button btnCamera;
    private Button btnRevert;
    private ImageView ivProfile;
    private TextView tvUsername;
    private EditText etBio;
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
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnCamera = view.findViewById(R.id.btnCamera);
        btnRevert = view.findViewById(R.id.btnRevert);
        ivProfile = view.findViewById(R.id.ivProfile);
        tvUsername = view.findViewById(R.id.tvUsername);
        etBio = view.findViewById(R.id.tvBio);

        // Set on click listener for button logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Logout clicked", Toast.LENGTH_SHORT).show();
                ParseUser.logOut();
                // Send the user back to the login page
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

        // Set on click listener for button update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the text and upload to Parse
                String bio = etBio.getText().toString();
                ParseUser user = ParseUser.getCurrentUser();
                user.put("bio", bio);
                user.saveInBackground();
            }
        });

        // Set on click listener for the revert collection button
        btnRevert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Put user back into privileged mode
                ((ParseApplication) context.getApplicationContext()).permReset();
                // Use an intent to return them to the set fragment
                Intent intent = new Intent(context, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Set on click listener for the camera button
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Camera", Toast.LENGTH_SHORT).show();
            }
        });

        ParseUser user = ParseUser.getCurrentUser();
        tvUsername.setText(user.getUsername());
        etBio.setText(user.getString("bio"));
        Glide.with(view).load(user.getParseFile("profilePic").getUrl()).centerCrop().into(ivProfile);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}