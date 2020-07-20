package models;

import org.parceler.Parcel;

@Parcel
public class Card implements Comparable<Card> {

    // TODO change these to public and change their references
    public String name;
    public String id;          // Unique identifier for each card
    public String url;
    public String setCode;
    public int number;         // Number of card within the set
    public int count = 0;

    // Required empty public constructor
    public Card () { }

    public Card (String name, String id, String url, String setCode, int number) {
        this.name = name;
        this.id = id;
        this.url = url;
        this.setCode = setCode;
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
