package models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Set {

    @ColumnInfo
    private String name;
    @ColumnInfo
    private String logoUrl;
    @ColumnInfo
    private String symbolUrl;
    @PrimaryKey
    @ColumnInfo
    @NonNull
    private String code;                    // Unique identifier per set, thus our primary key
    @ColumnInfo
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

    public String getCode() {
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