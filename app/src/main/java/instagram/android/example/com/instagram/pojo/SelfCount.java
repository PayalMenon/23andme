package instagram.android.example.com.instagram.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelfCount {

    @SerializedName("media")
    @Expose
    int posts;

    @Expose
    int follows;

    @SerializedName("followed_by")
    @Expose
    int followedBy;

    public int getFollowedBy() {
        return followedBy;
    }

    public int getFollows() {
        return follows;
    }

    public int getPosts() {
        return posts;
    }

    public void setFollowedBy(int followedBy) {
        this.followedBy = followedBy;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }
}
