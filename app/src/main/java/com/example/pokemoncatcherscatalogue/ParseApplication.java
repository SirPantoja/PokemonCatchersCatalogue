package com.example.pokemoncatcherscatalogue;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import models.ParseCard;


public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register custom Parse objects
        ParseObject.registerSubclass(ParseCard.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("pokemon-catchers-catalogue") // should correspond to APP_ID env variable
                .clientKey("pcc2020")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://pokemon-catchers-catalogue.herokuapp.com/parse/").build());
    }
}