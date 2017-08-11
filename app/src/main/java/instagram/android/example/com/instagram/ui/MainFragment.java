package instagram.android.example.com.instagram.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import javax.inject.Inject;

import instagram.android.example.com.instagram.Application;
import instagram.android.example.com.instagram.InstagramSettings;
import instagram.android.example.com.instagram.R;
import instagram.android.example.com.instagram.pojo.AccessToken;
import instagram.android.example.com.instagram.pojo.Data;
import instagram.android.example.com.instagram.pojo.User;
import instagram.android.example.com.instagram.service.InstagramService;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private static final String CLIENT_ID = "dcacf87790ce49ee9c57ac252125ef54";
    private static final String CLIENT_SECRET = "6f92fff496034c21abb2149098b87a67";
    private static final String REDIRECT_URL = "https://developer1934.auth0.com/login/callback";

    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/?" +
            "client_id=" + CLIENT_ID +
            "&redirect_uri=" + REDIRECT_URL +
            "&response_type=code" +
            "&scope=likes+public_content";

    public static final String Access_Token_Body = "client_id=" + CLIENT_ID +
            "&client_secret=" + CLIENT_SECRET +
            "&redirect_uri=" + REDIRECT_URL +
            "&grant_type=authorization_code&" +
            "code=";

    @Inject
    InstagramService service;

    private String responseCode;
    private WebView view;

    private void initializeWebView() {
        view = (WebView) getActivity().findViewById(R.id.webView);
        view.setVerticalScrollBarEnabled(false);
        view.setHorizontalScrollBarEnabled(false);
        view.setWebViewClient(new OAuthWebViewClient());
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl(AUTH_URL);
    }

    private void getAccessToken() {

        ((MainActivity)getActivity()).startProgressDialog();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Access_Token_Body);
        stringBuilder.append(responseCode);

        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), stringBuilder.toString());
        Call<AccessToken> call = service.getAccessToken(body);
        call.enqueue(new Callback<AccessToken>() {

            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    AccessToken accessResponse = response.body();

                    InstagramSettings settings = InstagramSettings.getInstance();
                    settings.setString(InstagramSettings.ACCESS_TOKEN, accessResponse.getAccessToken());

                    Call<Data> user = service.getSelfInformation(accessResponse.getAccessToken());
                    user.enqueue(new Callback<Data>() {
                        @Override
                        public void onResponse(Call<Data> call, Response<Data> response) {
                            if (response.isSuccessful()) {

                                Data data = response.body();
                                User user = data.getData();
                                InstagramSettings settings = InstagramSettings.getInstance();
                                settings.setString(InstagramSettings.FULL_NAME, user.getFull_name());
                                settings.setString(InstagramSettings.USER_NAME, user.getUsername());
                                settings.setString(InstagramSettings.PROFILE_PIC, user.getProfile_picture());
                                settings.setString(InstagramSettings.USER_ID, user.getId());
                                settings.setInteger(InstagramSettings.POSTS, user.getCounts().getPosts());
                                settings.setInteger(InstagramSettings.FOLLOWERS, user.getCounts().getFollowedBy());
                                settings.setInteger(InstagramSettings.FOLLOWING, user.getCounts().getFollows());

                                ((MainActivity) getActivity()).finishLogin();
                            }
                        }

                        @Override
                        public void onFailure(Call<Data> call, Throwable t) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.fetch_selfInfo_failed), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Toast.makeText(getActivity(), getResources().getString(R.string.fetch_accessToken_failed), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((Application) getActivity().getApplication()).getNetworkComponent().inject(this);

        initializeWebView();
    }


    private class OAuthWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith(REDIRECT_URL)) {
                String urls[] = url.split("=");
                responseCode = urls[1];
                getAccessToken();
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

    }
}
