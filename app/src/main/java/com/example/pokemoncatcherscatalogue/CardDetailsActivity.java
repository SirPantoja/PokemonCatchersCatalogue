package com.example.pokemoncatcherscatalogue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.File;
import java.util.List;

import models.Card;
import models.ParseCard;

public class CardDetailsActivity extends AppCompatActivity {

    public static final String TAG = "CardDetailsActivity";
    private ImageView ivCardDetails;
    private TextView tvCardName;
    private Button btnTakePic;
    private Switch switchCardDetails;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;
    private Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);

        // Link up views
        ivCardDetails = findViewById(R.id.ivCardDetails);
        tvCardName = findViewById(R.id.tvCardName);
        btnTakePic = findViewById(R.id.btnTakePic);
        switchCardDetails = findViewById(R.id.switchCardDetails);

        // Get the intent
        Intent intent = getIntent();
        card = Parcels.unwrap(getIntent().getParcelableExtra("card"));

        // Fill up the views
        tvCardName.setText(card.getName());
        Glide.with(this).load(card.getUrl()).into(ivCardDetails);

        // Set on click listener for btnTakePic
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CardDetailsActivity.this, "Button clicked", Toast.LENGTH_SHORT).show();
                onLaunchCamera();
            }
        });

        // Set an on switch listener for the switch
        switchCardDetails.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(CardDetailsActivity.this, "ON", Toast.LENGTH_SHORT).show();
                    getCustomUrl();
                } else {
                    Toast.makeText(CardDetailsActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                    Glide.with(CardDetailsActivity.this).load(card.getUrl()).into(ivCardDetails);
                }
            }
        });
    }

    private void getCustomUrl() {
        // First query Parse to see if the card already exists
        ParseQuery<ParseCard> query = ParseQuery.getQuery(ParseCard.class);
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.whereEqualTo("cardId", card.getId());
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseCard>() {
            public void done(List<ParseCard> itemList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    if (itemList.isEmpty()) {
                        // This means this card doesn't already exist so we do nothing since count is at 0
                        Toast.makeText(CardDetailsActivity.this, "No image found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // This means we found the card so we just need to update it
                    ParseCard newParseCard = itemList.get(0);
                    if (newParseCard.getCustomCardImage() == null) {
                        Log.i(TAG, "No custom image yet");
                        return;
                    }
                    Log.i(TAG, "Successfully retrieved card: " + newParseCard.getCustomCardImage().getUrl());
                    // Add the taken image
                    Glide.with(CardDetailsActivity.this).load(newParseCard.getCustomCardImage().getUrl()).into(ivCardDetails);
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    private void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String photoFileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

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
                // Create a ParseFile and upload it to Parse
                ParseFile parseFile = new ParseFile(photoFile);
                putPhoto(parseFile);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void putPhoto(final ParseFile parseFile) {
        // First query Parse to see if the card already exists
        ParseQuery<ParseCard> query = ParseQuery.getQuery(ParseCard.class);
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.whereEqualTo("cardId", card.getId());
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseCard>() {
            public void done(List<ParseCard> itemList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    if (itemList.isEmpty()) {
                        // This means this card doesn't already exist so we do nothing since count is at 0
                        Toast.makeText(CardDetailsActivity.this, "Card must be registered into your collection", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // This means we found the card so we just need to update it
                    ParseCard newParseCard = itemList.get(0);
                    Log.i(TAG, "Successfully retrieved card");
                    // Add the taken image
                    newParseCard.setCustomCardImage(parseFile);
                    newParseCard.saveInBackground();
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }
}