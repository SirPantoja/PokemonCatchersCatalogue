package models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.parceler.Parcel;

@Parcel
public class Card implements Comparable<Card> {

    // TODO change these to public and change their references
    public String name;
    public String id;          // Unique identifier for each card
    public String url;
    public String setName;
    public int number;         // Number of card within the set
    public int count = 0;

    // Required empty public constructor
    public Card () { }

    public Card (String name, String id, String url, String setName, int number) {
        this.name = name;
        this.id = id;
        this.url = url;
        this.setName = setName;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public int compareTo(Card card) {
        return this.getNumber() - card.getNumber();
    }
}
