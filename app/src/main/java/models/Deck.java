package models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Deck")
public class Deck extends ParseObject {

    // Default public constructor
    public Deck() { }

    public Deck(ParseUser owner, List<ParseCard> cards, String deckName) {
        setOwner(owner);
        setCards(cards);
        setDeckName(deckName);
    }

    public void setOwner(ParseUser owner) {
        put("owner", owner);
    }

    public void setCards(List<ParseCard> cards) {
        addAll("cards", cards);
    }

    public void setDeckCover(ParseFile deckCover) {
        put("deckCover", deckCover);
    }

    public void setDeckName(String deckName) {
        put("deckName", deckName);
    }

    public ParseUser getOwner() {
        return getParseUser("owner");
    }

    public List<Card> getCards() {
        return getList("cards");
    }

    public ParseFile getDeckCover() {
        return getParseFile("deckCover");
    }

    public String getDeckName() {
        return getString("deckName");
    }
}
