package com.example.pokemoncatcherscatalogue;

public class Set {

    private String name;
    private String logoUrl;
    private String symbolUrl;
    private int totalCards;

    public Set(String name, String logoUrl, String symbolUrl, int totalCards) {
        this.name = name;
        this.logoUrl = logoUrl;
        this.symbolUrl = symbolUrl;
        this.totalCards = totalCards;
    }

    public String getName() {
        return name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getSymbolUrl() {
        return symbolUrl;
    }

    public int getTotalCards() {
        return totalCards;
    }
}
