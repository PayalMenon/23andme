package instagram.android.example.com.instagram.pojo;

import com.google.gson.annotations.Expose;

public class Thumbnail {

    @Expose
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
