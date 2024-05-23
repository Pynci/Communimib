package it.unimib.communimib.ui.main.dashboard.pictures;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import it.unimib.communimib.R;

public class PostPicturesRecyclerViewAdapter extends RecyclerView.Adapter<PostPicturesRecyclerViewAdapter.ViewHolder> {

    private List<String> images;
    private Context context;

    public PostPicturesRecyclerViewAdapter(List<String> images, Context context){
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public PostPicturesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_picture_item, parent, false);
        return new PostPicturesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.postImageItem_imageView);
        }

        public void bind(String image){
            Glide
                    .with(context)
                    .load(Uri.parse(image))
                    .into(imageView);
        }

    }

}
