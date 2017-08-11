package instagram.android.example.com.instagram.pojo;

import com.google.gson.annotations.Expose;

public class User {

    @Expose
    String id;

    @Expose
    String username;

    @Expose
    String full_name;

    @Expose
    String profile_picture;

    SelfCount counts;

    public String getFull_name() {
        return full_name;
    }

    public String getId() {
        return id;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public String getUsername() {
        return username;
    }

    public SelfCount getCounts() {
        return counts;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCounts(SelfCount counts) {
        this.counts = counts;
    }
}
