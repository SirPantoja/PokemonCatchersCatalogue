package models;

public class Card {

    private String name;
    private String id;          // Unique identifier for each card
    private int number;         // Number of card within the set

    public Card (String name, String id, int number) {
        this.name = name;
        this.id = id;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }
}
