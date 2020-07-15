package models;

public class Card implements Comparable<Card> {

    private String name;
    private String id;          // Unique identifier for each card
    private String url;
    private int number;         // Number of card within the set

    public Card (String name, String id, String url, int number) {
        this.name = name;
        this.id = id;
        this.url = url;
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
