package instagram.android.example.com.instagram;

import android.content.Context;

import instagram.android.example.com.instagram.dragger.component.DaggerNetworkComponent;
import instagram.android.example.com.instagram.dragger.component.NetworkComponent;
import instagram.android.example.com.instagram.dragger.module.ApplicationModule;
import instagram.android.example.com.instagram.dragger.module.NetworkModule;

public class Application extends android.app.Application{

    private NetworkComponent networkComponent;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        networkComponent = DaggerNetworkComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule("https://api.instagram.com"))
                .build();

        this.context = getApplicationContext();
    }

    public NetworkComponent getNetworkComponent() {
        return this.networkComponent;
    }

    public static Context getContext(){
        return context;
    }
}
