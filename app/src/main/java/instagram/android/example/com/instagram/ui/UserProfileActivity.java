package instagram.android.example.com.instagram.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import instagram.android.example.com.instagram.InstagramSettings;
import instagram.android.example.com.instagram.R;

public class UserProfileActivity extends AppCompatActivity {

    private static final String LIST_FRAGMENT = "list_fragment";

    private InstagramSettings preference;
    private FragmentListener fragmentListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        preference = InstagramSettings.getInstance();

        populateUserInformation();
        addListFragment();
    }

    private void addListFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ListFragment fragment = (ListFragment) manager.findFragmentByTag(LIST_FRAGMENT);

        if (fragment == null) {

            fragment = new ListFragment();
            transaction.add(R.id.list_fragment_container, fragment, LIST_FRAGMENT);
            transaction.commit();
        }
    }

    private void populateUserInformation() {

        final ImageView profileImage = (ImageView) findViewById(R.id.profile_picture);
        Glide.with(this).load(preference.getString(InstagramSettings.PROFILE_PIC, null)).asBitmap().centerCrop().into(new BitmapImageViewTarget(profileImage) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                profileImage.setImageDrawable(circularBitmapDrawable);
            }
        });

        TextView fullName = (TextView) findViewById(R.id.full_name);
        fullName.setText(preference.getString(InstagramSettings.FULL_NAME, null));

        TextView userName = (TextView) findViewById(R.id.user_name);
        userName.setText(preference.getString(InstagramSettings.USER_NAME, null));

        TextView posts = (TextView) findViewById(R.id.posts);
        posts.setText(getResources().getString(R.string.posts, preference.getInteger(InstagramSettings.POSTS, 0)));

        TextView followers = (TextView) findViewById(R.id.followers);
        followers.setText(getResources().getString(R.string.followers, preference.getInteger(InstagramSettings.FOLLOWERS, 0)));

        TextView following = (TextView) findViewById(R.id.following);
        following.setText(getResources().getString(R.string.following, preference.getInteger(InstagramSettings.FOLLOWING, 0)));
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing()) {
            releaseFragment();
        }
    }

    private void releaseFragment() {
        FragmentManager manager = getSupportFragmentManager();
        ListFragment fragment = (ListFragment) manager.findFragmentByTag(LIST_FRAGMENT);
        manager.beginTransaction().remove(fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getResources().getString(R.string.search_text));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fragmentListener.updateFragmentList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_logout) {
            InstagramSettings preference = InstagramSettings.getInstance();
            preference.clearAll();

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setFragmentListener(FragmentListener listener) {

        this.fragmentListener = listener;
    }

    public interface FragmentListener {

        void updateFragmentList(String query);
    }
}
