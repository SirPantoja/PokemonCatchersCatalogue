package models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Card")
public class ParseCard extends ParseObject {

    // Default public constructor
    public ParseCard() { }

    public ParseCard(String setName, int setNumber, ParseUser owner, int count, String name, String cardId, int rarity) {
        setSetName(setName);
        setSetNumber(setNumber);
        setOwner(owner);
        setCount(count);
        setName(name);
        setCardId(cardId);
        setRarity(rarity);
    }

    public String getSetName() {
        return getString("setName");
    }

    public void setSetName(String setName) {
        put("setName", setName);
    }

    public int getSetNumber() {
        return getInt("setNumber");
    }

    public void setSetNumber(int setNumber) {
        put("setNumber", setNumber);
    }

    public ParseFile getCustomCardImage() {
        return getParseFile("customCardImage");
    }

    public void setCustomCardImage(ParseFile customCardImage) {
        put("customCardImage", customCardImage);
    }

    public ParseUser getOwner() {
        return getParseUser("owner");
    }

    public void setOwner(ParseUser owner) {
        put("owner", owner);
    }

    public int getCount() {
        return getInt("count");
    }

    public void setCount(int count) {
        put("count", count);
    }

    // If the card already exists then we just increment how many we have
    public void incrementCount() {
        increment("count");
    }

    public void decrementCount() {
        increment("count", -1);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getCardId() {
        return getString("cardId");
    }

    public void setCardId(String cardId) {
        put("cardId", cardId);
    }

    public int getRarity() {
        return getInt("rarity");
    }

    public void setRarity(int rarity) {
        put("rarity", rarity);
    }
}
