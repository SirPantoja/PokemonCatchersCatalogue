package models;

public class Friend {
    private String username;
    private String profileUrl;

    public Friend (String username, String profileUrl) {
        this.username = username;
        this.profileUrl = profileUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileUrl() {
        return profileUrl;
    }
}
