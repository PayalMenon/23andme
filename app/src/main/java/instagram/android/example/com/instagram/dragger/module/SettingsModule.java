package instagram.android.example.com.instagram.dragger.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import instagram.android.example.com.instagram.InstagramSettings;

@Module
public class SettingsModule {

    @Provides
    @Singleton
    InstagramSettings providePreference() {
        return InstagramSettings.getInstance();
    }
}
