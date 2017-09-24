package instagram.android.example.com.instagram.dragger.component;

import javax.inject.Singleton;

import dagger.Component;
import instagram.android.example.com.instagram.dragger.module.ApplicationModule;
import instagram.android.example.com.instagram.dragger.module.NetworkModule;
import instagram.android.example.com.instagram.dragger.module.SettingsModule;
import instagram.android.example.com.instagram.ui.ListFragment;
import instagram.android.example.com.instagram.ui.MainActivity;
import instagram.android.example.com.instagram.ui.MainFragment;
import instagram.android.example.com.instagram.ui.UserProfileActivity;

@Singleton
@Component (modules = {ApplicationModule.class, NetworkModule.class, SettingsModule.class})
public interface NetworkComponent {

    void inject(MainActivity activity);
    void inject(MainFragment fragment);
    void inject(ListFragment fragment);
    void inject(UserProfileActivity activity);
}
