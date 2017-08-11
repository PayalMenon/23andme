package instagram.android.example.com.instagram.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import instagram.android.example.com.instagram.Application;
import instagram.android.example.com.instagram.InstagramSettings;
import instagram.android.example.com.instagram.R;
import instagram.android.example.com.instagram.pojo.SearchData;
import instagram.android.example.com.instagram.pojo.SearchInfo;
import instagram.android.example.com.instagram.pojo.SelfData;
import instagram.android.example.com.instagram.pojo.SelfRecentImages;
import instagram.android.example.com.instagram.service.InstagramService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListFragment extends Fragment implements UserProfileActivity.FragmentListener {

    @Inject
    InstagramService service;

    private RecyclerView listView;
    private ListAdapter listAdapter;
    private RecyclerView.LayoutManager manager;
    private List<ListObject> imageList = new ArrayList<>();
    private InstagramSettings preference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preference = InstagramSettings.getInstance();
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((Application) getActivity().getApplication()).getNetworkComponent().inject(this);

        listView = (RecyclerView) getActivity().findViewById(R.id.list_view);
        listAdapter = new ListAdapter(getActivity(), imageList, service);
        manager = new GridLayoutManager(getActivity(), 2);

        listView.setAdapter(listAdapter);
        listView.setLayoutManager(manager);

        ((UserProfileActivity) getActivity()).setFragmentListener(this);

        fetchSelfRecentMedia();
    }

    private void fetchSelfRecentMedia() {
        preference = InstagramSettings.getInstance();

        Call<SelfData> call = service.getSelfMedia(preference.getString(InstagramSettings.ACCESS_TOKEN, null));
        call.enqueue(new Callback<SelfData>() {
            @Override
            public void onResponse(Call<SelfData> call, Response<SelfData> response) {
                if (response.isSuccessful()) {
                    SelfData data = response.body();
                    List<SelfRecentImages> list = data.getData();
                    for (int i = 0; i < list.size(); i++) {
                        ListObject obj = new ListObject();
                        obj.url = list.get(i).getImages().getThumbnail().getUrl();
                        obj.id = list.get(i).getId();
                        imageList.add(obj);
                    }
                    final Call<SelfData> liked = service.getMediaLiked(preference.getString(InstagramSettings.ACCESS_TOKEN, null));
                    liked.enqueue(new Callback<SelfData>() {
                        @Override
                        public void onResponse(Call<SelfData> call, Response<SelfData> response) {
                            if (response.isSuccessful()) {
                                SelfData data = response.body();
                                List<SelfRecentImages> list = data.getData();
                                for (int i = 0; i < list.size(); i++) {
                                    SelfRecentImages images = list.get(i);
                                    for (int j = 0; j < imageList.size(); j++) {
                                        ListObject obj = imageList.get(j);
                                        if (obj.id.equals(images.getId())) {
                                            obj.liked = true;
                                            imageList.set(j, obj);
                                        }
                                    }
                                }
                            }
                            listAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<SelfData> call, Throwable t) {
                            listAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), getResources().getString(R.string.fetch_likedList_failed), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<SelfData> call, Throwable t) {
                Toast.makeText(getActivity(), getResources().getString(R.string.fetch_recent_failed), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchStreamSearch(String query) {
        Call<SearchData> call = service.getStreamSearch(preference.getString(InstagramSettings.ACCESS_TOKEN, null), query);
        call.enqueue(new Callback<SearchData>() {
            @Override
            public void onResponse(Call<SearchData> call, Response<SearchData> response) {

                if (response.isSuccessful()) {
                    SearchData data = response.body();
                    List<SearchInfo> list = data.getData();
                    imageList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        ListObject obj = new ListObject();
                        obj.url = list.get(i).getProfilePicture();
                        obj.id = list.get(i).getId();
                        imageList.add(obj);
                    }
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SearchData> call, Throwable t) {
                Toast.makeText(getActivity(), getResources().getString(R.string.fetch_search_list_failed), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void updateFragmentList(String query) {
        fetchStreamSearch(query);
    }

    public class ListObject {
        String url;
        String id;
        boolean liked;
    }
}
