package instagram.android.example.com.instagram.dragger.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import instagram.android.example.com.instagram.service.InstagramService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    String baseUrl;

    public NetworkModule (String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    @Provides
    @Singleton
    InstagramService provideInstagramService(Retrofit retrofit) {
        return retrofit.create(InstagramService.class);
    }
}
