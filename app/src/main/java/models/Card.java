package models;

import org.parceler.Parcel;

@Parcel
public class Card implements Comparable<Card> {

    // To ease the readability of the sort switch case
    public enum SORT {
        NUMBER,
        TYPE,
        HP,
        RARITY,
        ALPHABETICAL
    }

    public String name;
    public String id;                           // Unique identifier for each card
    public String url;
    public String setCode;
    public int number;                          // Number of card within the set
    public int count = 0;
    public static SORT sort = SORT.NUMBER;            // Set the default as a set number sort

    // Required empty public constructor
    public Card () { }

    public Card (String name, String id, String url, String setCode, int number) {
        this.name = name;
        this.id = id;
        this.url = url;
        this.setCode = setCode;
        this.number = number;
    }

    @Override
    public int compareTo(Card card) {
        switch(card.sort) {
            case NUMBER:
                return this.number - card.number;
            case TYPE:
                break;
            case HP:
                break;
            case RARITY:
                break;
            case ALPHABETICAL:
                return this.name.compareTo(card.name);
            default:
                break;
        }
        return 1;
    }
}
