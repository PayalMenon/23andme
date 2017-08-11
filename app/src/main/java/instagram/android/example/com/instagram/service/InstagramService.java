package instagram.android.example.com.instagram.service;

import instagram.android.example.com.instagram.pojo.AccessToken;
import instagram.android.example.com.instagram.pojo.Data;
import instagram.android.example.com.instagram.pojo.SearchData;
import instagram.android.example.com.instagram.pojo.SelfData;
import instagram.android.example.com.instagram.pojo.User;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InstagramService {

    @Headers({
            "Content-Type:application/x-www-form-urlencoded"
    })
    @POST("/oauth/access_token/")
    Call<AccessToken> getAccessToken(@Body RequestBody data);

    @GET("v1/users/self/")
    Call<Data> getSelfInformation(@Query("access_token") String accessToken);

    @GET("v1/users/self/media/recent/")
    Call<SelfData> getSelfMedia(@Query("access_token") String accessToken);

    @GET("/users/self/media/liked")
    Call<SelfData> getMediaLiked(@Query("access_token") String accessToken);

    @POST("v1/media/{media_id}/likes")
    Call<Void> postMediaLike(@Path(value="media_id") String value,
            @Query("access_token") String accessToken);

    @DELETE("v1/media/{media_id}/likes")
    Call<Void> postMediaDisliked(@Path(value="media_id") String value,
                             @Query("access_token") String accessToken);

    @GET("v1/users/search")
    Call<SearchData> getStreamSearch(@Query("access_token") String accessToken,
                                     @Query("q") String query);
}
