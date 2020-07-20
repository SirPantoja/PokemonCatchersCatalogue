package com.example.pokemoncatcherscatalogue;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import models.Set;
import models.SetDao;

@Database(entities={Set.class}, version=1)
public abstract class MyDatabase extends RoomDatabase {
    // Declare your data access objects as abstract
    public abstract SetDao setDao();

    // Database name to be used
    public static final String NAME = "MyDataBase";
}
