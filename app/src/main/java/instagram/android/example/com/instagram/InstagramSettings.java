package instagram.android.example.com.instagram;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class InstagramSettings {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String USER_NAME = "user_name";
    public static final String PROFILE_PIC = "profile_picture";
    public static final String FULL_NAME = "full_name";
    public static final String USER_ID = "id";
    public static final String POSTS = "posts";
    public static final String FOLLOWERS = "followers";
    public static final String FOLLOWING = "following";


    private static InstagramSettings sInstance;
    SharedPreferences preferences;
    Editor editor;

    private InstagramSettings(Context context) {
        preferences = context.getSharedPreferences("instagram_settings", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static synchronized InstagramSettings getInstance() {
        if (sInstance == null) {
            sInstance = new InstagramSettings(Application.getContext());
        }
        return sInstance;
    }

    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void setInteger(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public String getString(String key, String defaultVlaue) {
        return preferences.getString(key, defaultVlaue);
    }

    public int getInteger(String key, int defaultVlaue) {
        return preferences.getInt(key, defaultVlaue);
    }

    public void clearAll() {
        editor.clear();
        editor.commit();
    }

}
