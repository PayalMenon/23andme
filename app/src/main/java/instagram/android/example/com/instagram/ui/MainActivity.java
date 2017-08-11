package instagram.android.example.com.instagram.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import instagram.android.example.com.instagram.Application;
import instagram.android.example.com.instagram.R;


public class MainActivity extends AppCompatActivity {

    private static final String MAIN_FRAGMENT = "main_fragment";

    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Application) getApplication()).getNetworkComponent().inject(this);

        addMainFragment();
    }

    private void addMainFragment() {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MainFragment fragment = (MainFragment) manager.findFragmentByTag(MAIN_FRAGMENT);

        if (fragment == null) {

            fragment = new MainFragment();
            transaction.add(R.id.fragment_container, fragment, MAIN_FRAGMENT);
            transaction.commit();
        }
    }

    public void startProgressDialog() {
        progressBar = new ProgressDialog(this);
        progressBar.show();
    }

    public void finishProgress() {
       progressBar.cancel();
    }

    public void finishLogin() {

        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        FragmentManager manager = getSupportFragmentManager();
        MainFragment fragment = (MainFragment) manager.findFragmentByTag(MAIN_FRAGMENT);
        manager.beginTransaction().remove(fragment).commit();

        finishProgress();
        finish();
    }
}
