package fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
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
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static com.example.pokemoncatcherscatalogue.CardDetailsActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;

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
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

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
                onLaunchCamera();
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

    private void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(context, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String photoFileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivProfile.setImageBitmap(takenImage);
                // Create a ParseFile and upload it to Parse
                ParseFile parseFile = new ParseFile(photoFile);
                ParseUser.getCurrentUser().put("profilePic", parseFile);
                ParseUser.getCurrentUser().saveInBackground();
                // Load the image into a view
                //Log.i(TAG, "Loading in image: " + parseFile.toString());
                //Glide.with(context).load(parseFile.getUrl()).centerCrop().into(ivProfile);
            } else { // Result was a failure
                Toast.makeText(context, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}