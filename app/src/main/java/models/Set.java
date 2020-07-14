package models;

public class Set {

    private String name;
    private String logoUrl;
    private String symbolUrl;
    private String code;
    private int totalCards;

    public Set(String name, String logoUrl, String symbolUrl, String code, int totalCards) {
        this.name = name;
        this.logoUrl = logoUrl;
        this.symbolUrl = symbolUrl;
        this.code = code;
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

    public String getSetCode() {
        return code;
    }

    public int getTotalCards() {
        return totalCards;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setSymbolUrl(String symbolUrl) {
        this.symbolUrl = symbolUrl;
    }

    public void setTotalCards(int totalCards) {
        this.totalCards = totalCards;
    }
}