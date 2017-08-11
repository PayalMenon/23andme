package instagram.android.example.com.instagram.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import instagram.android.example.com.instagram.InstagramSettings;
import instagram.android.example.com.instagram.R;
import instagram.android.example.com.instagram.service.InstagramService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private InstagramService service;
    private List<ListFragment.ListObject> imageList;
    private Context context;
    private InstagramSettings preference;

    public ListAdapter(Context context, List<ListFragment.ListObject> imageList, InstagramService service) {
        this.context = context;
        this.imageList = imageList;
        this.service = service;
        preference = InstagramSettings.getInstance();
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new ListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        ListFragment.ListObject obj = imageList.get(position);

        Glide.with(context).load(obj.url).asBitmap().placeholder(R.drawable.placeholder)
                .animate(android.R.anim.slide_in_left).into(holder.rowImage);

        if (obj.liked) {
            holder.likeButton.setBackground(context.getDrawable(R.drawable.like));
        } else {
            holder.likeButton.setBackground(context.getDrawable(R.drawable.no_selection));
        }

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String media_id = imageList.get(position).id;
                if (!imageList.get(position).liked) {
                    Call<Void> call = service.postMediaLike(media_id, preference.getString(InstagramSettings.ACCESS_TOKEN, null));
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                holder.likeButton.setBackground(context.getDrawable(R.drawable.like));
                                ListFragment.ListObject obj = imageList.get(position);
                                obj.liked = true;
                                imageList.set(position, obj);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(context, context.getResources().getString(R.string.failed_like), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Call<Void> call = service.postMediaDisliked(media_id, preference.getString(InstagramSettings.ACCESS_TOKEN, null));
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            holder.likeButton.setBackground(context.getDrawable(R.drawable.no_selection));
                            ListFragment.ListObject obj = imageList.get(position);
                            obj.liked = false;
                            imageList.set(position, obj);
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(context, context.getResources().getString(R.string.failed_unlike), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView rowImage;
        Button likeButton;

        public ListViewHolder(View itemView) {
            super(itemView);

            rowImage = (ImageView) itemView.findViewById(R.id.row_image);
            likeButton = (Button) itemView.findViewById(R.id.like_button);
        }
    }
}


