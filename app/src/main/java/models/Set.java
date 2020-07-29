package models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Set implements Parcelable {

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
    @ColumnInfo
    private String series;

    // Default empty constructor
    public Set() { }

    public Set(String name, String logoUrl, String symbolUrl, String code, int totalCards, String series) {
        this.name = name;
        this.logoUrl = logoUrl;
        this.symbolUrl = symbolUrl;
        this.code = code;
        this.totalCards = totalCards;
        this.series = series;
    }

    protected Set(Parcel in) {
        name = in.readString();
        logoUrl = in.readString();
        symbolUrl = in.readString();
        code = in.readString();
        totalCards = in.readInt();
        series = in.readString();
    }

    public static final Creator<Set> CREATOR = new Creator<Set>() {
        @Override
        public Set createFromParcel(Parcel in) {
            return new Set(in);
        }

        @Override
        public Set[] newArray(int size) {
            return new Set[size];
        }
    };

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

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
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

    public void setCode(@NonNull String code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(logoUrl);
        parcel.writeString(symbolUrl);
        parcel.writeString(code);
        parcel.writeInt(totalCards);
        parcel.writeString(series);
    }
}