package com.example.pokemoncatcherscatalogue;

import android.app.Application;
import android.media.MediaPlayer;

import androidx.room.Room;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import models.Deck;
import models.Friend;
import models.ParseCard;


public class ParseApplication extends Application {

    MyDatabase myDatabase;
    AsyncHttpClient client;
    ParseUser guestUser;            // Obtained when accessing a friend's collection
    public static boolean perm = true;     // Permissions to edit user collection; only false when viewing a friend's collection
    MediaPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();

        // when upgrading versions, kill the original tables by using fallbackToDestructiveMigration()
        myDatabase = Room.databaseBuilder(this, MyDatabase.class, MyDatabase.NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        // Instantiate the API client
        client = new AsyncHttpClient();

        // Register custom Parse objects
        ParseObject.registerSubclass(ParseCard.class);
        ParseObject.registerSubclass(Friend.class);
        ParseObject.registerSubclass(Deck.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("pokemon-catchers-catalogue") // should correspond to APP_ID env variable
                .clientKey("pcc2020")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://pokemon-catchers-catalogue.herokuapp.com/parse/").build());
    }

    public void stopMediaPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public MyDatabase getMyDatabase() {
        return myDatabase;
    }

    public AsyncHttpClient getClient() {
        return client;
    }

    public ParseUser getGuestUser() {
        return guestUser;
    }

    public void setGuestUser(ParseUser guestUser) {
        this.guestUser = guestUser;
        perm = false;
    }

    public void permReset() {
        perm = true;
    }

    // Given the privilege level return the proper user
    public ParseUser userPerm() {
        if (ParseApplication.perm) {
            return ParseUser.getCurrentUser();
        } else {
            return getGuestUser();
        }
    }

    public void startMediaPlayer(int file) {
        // Make sure the player is null and stopped
        if (player != null) {
            stopMediaPlayer();
        }
        // Instantiate the player and play the new song
        player = MediaPlayer.create(this, file);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // Repeat ad infinitum
                player.start();
            }
        });
        player.start();
    }
}