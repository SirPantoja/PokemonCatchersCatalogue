package com.example.pokemoncatcherscatalogue;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import models.Card;
import models.CardDao;
import models.Set;
import models.SetDao;

@Database(entities={Set.class, Card.class}, version=3)
public abstract class MyDatabase extends RoomDatabase {
    // Declare your data access objects as abstract
    public abstract SetDao setDao();
    public abstract CardDao cardDao();

    // Database name to be used
    public static final String NAME = "MyDataBase";
}
