package instagram.android.example.com.instagram.pojo;


import com.google.gson.annotations.Expose;

public class SelfRecentImages {

    @Expose
    String id;

    Images images;

    public Images getImages() {
        return images;
    }

    public String getId() {
        return id;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public void setId(String id) {
        this.id = id;
    }
}
