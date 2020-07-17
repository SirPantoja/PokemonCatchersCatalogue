package models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.boltsinternal.Task;

@ParseClassName("Friend")
public class Friend extends ParseObject {

    // Default public constructor
    public Friend() { }

    public Friend(ParseUser currUser, ParseUser friend) {
        setUser(currUser);
        setFriend(friend);
    }

    public void setUser(ParseUser user) {
        put("ptrUser", user);
    }

    public ParseUser getUser() {
        return getParseUser("ptrUser");
    }

    public void setProfilePic(ParseFile profilePic) {
        put("profilePic", profilePic);
    }

    public ParseFile getProfilePic() {
        return getParseFile("profilePic");
    }

    public void setFriend(ParseUser user) {
        put("actualFriend", user);
    }

    public ParseUser getFriend() {
        return getParseUser("actualFriend");
    }
}
