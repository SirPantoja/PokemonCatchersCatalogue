package models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity
public class Card implements Comparable<Card> {

    // To ease the readability of the sort switch case
    public enum SORT {
        NUMBER,
        TYPE,
        HP,
        RARITY,
        ALPHABETICAL
    }

    @ColumnInfo
    public String name;
    @ColumnInfo
    @PrimaryKey
    @NonNull
    public String id;                                    // Unique identifier for each card
    @ColumnInfo
    public String url;
    @ColumnInfo
    public String setCode;
    @ColumnInfo
    public int number = 0;                               // Number of card within the set
    @ColumnInfo
    public int count = 0;
    @ColumnInfo
    public static SORT sort = SORT.NUMBER;               // Set the default as a set number sort
    @ColumnInfo
    public Integer hp = 0;
    @ColumnInfo
    public int rarity = 0;
    @ColumnInfo
    public String type = "none";
    @Ignore
    public String text = "";

    // Required empty public constructor
    public Card () { }

    @Ignore
    public Card (String name, @NotNull String id, String url, String setCode) {
        this.name = name;
        this.id = id;
        this.url = url;
        this.setCode = setCode;
    }

    // Does a custom comparison based on the static member variable sort
    @Override
    public int compareTo(Card card) {
        switch(card.sort) {
            case NUMBER:
                return this.number - card.number;
            case TYPE:
                return this.type.compareTo(card.type);
            case HP:
                return card.hp - this.hp;
            case RARITY:
                return card.rarity - this.rarity;
            case ALPHABETICAL:
                return this.name.compareTo(card.name);
            default:
                break;
        }
        return 1;
    }

    // Sometimes the API provides improper format so this does error handling
    public void setHp(String hp) {
        try {
            this.hp = Integer.parseInt(hp);
        } catch (NumberFormatException e) {
            this.hp = 0;
        }
    }

    // Rarity comes as a string; this assigns a numerical value to each rarity string value
    public void setRarity(String rarity) {
        if (rarity.equalsIgnoreCase("common")) {
            this.rarity = 1;
        } else if (rarity.equalsIgnoreCase("uncommon")) {
            this.rarity = 2;
        } else if (rarity.equalsIgnoreCase("rare")) {
            this.rarity = 3;
        } else if (rarity.equalsIgnoreCase("rare holo")) {
            this.rarity = 4;
        } else if (rarity.equalsIgnoreCase("rare holo lv.x") || rarity.equalsIgnoreCase("rare holo ex") || rarity.equalsIgnoreCase("rare holo gx")) {
            this.rarity = 5;
        } else if (rarity.equalsIgnoreCase("rare secret") || rarity.equalsIgnoreCase("rare ultra")) {
            this.rarity = 6;
        } else {
            this.rarity = 0;
        }
    }

}
