package it.unimib.communimib.ui.main.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.util.DateFormatter;

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter<DashboardRecyclerViewAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(Post post);
        void onImageSliderClick(Post post);
    }

    private List<Post> postList;
    private final OnItemClickListener onItemClickListener;
    private final Context context;

    public void addItem(Post newPost){
        if(!postList.contains(newPost)){
            postList.add(0, newPost);
            notifyItemInserted(0);
        }
    }

    public void editItem(Post editedPost){
        int position = postList.indexOf(editedPost);
        if(position != -1){
            postList.set(position, editedPost);
            notifyItemChanged(position);
        }
    }

    public void removeItem(Post removedPost){
        int position = postList.indexOf(removedPost);
        if(position != -1){
            postList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void
    clearPostList(){
        this.postList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public DashboardRecyclerViewAdapter(OnItemClickListener onItemClickListener, Context context) {
        this.postList = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public DashboardRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new DashboardRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(postList.get(position));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView propic;
        private final TextView name;
        private final TextView surname;
        private final TextView title;
        private final TextView description;
        private final TextView dateTime;
        private final TextView email;
        private final ImageView emailIcon;
        private final TextView link;
        private final ImageView linkIcon;
        private final ImageSlider imageSlider;
        private final CardView imageSliderCardview;
        private final CardView imageInsideCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            propic = itemView.findViewById(R.id.postItem_propic);
            name = itemView.findViewById(R.id.postItem_name);
            surname = itemView.findViewById(R.id.postItem_surname);
            title = itemView.findViewById(R.id.postItem_title);
            description = itemView.findViewById(R.id.postItem_description);
            email = itemView.findViewById(R.id.postItem_email);
            emailIcon = itemView.findViewById(R.id.postItem_emailIcon);
            link = itemView.findViewById(R.id.postItem_link);
            linkIcon = itemView.findViewById(R.id.postItem_linkIcon);
            dateTime = itemView.findViewById(R.id.postItem_datetime);
            imageSlider = itemView.findViewById(R.id.postItem_imageSlider);
            imageSliderCardview = itemView.findViewById(R.id.postItem_imageSliderCardView);
            imageInsideCard = itemView.findViewById(R.id.postItem_ImageInsideCard);

            itemView.setOnClickListener(this);
            imageInsideCard.setOnClickListener(this);
            imageInsideCard.setCardBackgroundColor(Color.TRANSPARENT);
            imageInsideCard.setCardElevation(0);
        }

        public void bind(Post post){
            name.setText(post.getAuthor().getName());
            surname.setText(post.getAuthor().getSurname());
            title.setText(post.getTitle());
            description.setText(post.getDescription());
            if(post.getEmail() != null && !post.getEmail().isEmpty()){
                email.setText(post.getEmail());
            }
            else{
                email.setVisibility(View.GONE);
                emailIcon.setVisibility(View.GONE);
            }
            if(post.getLink() != null && !post.getLink().isEmpty()){
                link.setText(post.getLink());
            }
            else{
                link.setVisibility(View.GONE);
                linkIcon.setVisibility(View.GONE);
            }
            dateTime.setText(DateFormatter.format(post.getTimestamp(), context));
            if(post.getAuthor().getPropic() != null){
                Glide
                        .with(context)
                        .load(Uri.parse(post.getAuthor().getPropic()))
                        .into(propic);
            }

            List<SlideModel> slideModels = new ArrayList<>();
            if(!post.getPictures().isEmpty()){
                for (String picture : post.getPictures()) {
                    slideModels.add(new SlideModel(picture, ScaleTypes.FIT));
                }
                imageSlider.setImageList(slideModels, ScaleTypes.FIT);
                imageSlider.setVisibility(View.VISIBLE);
                imageSliderCardview.setVisibility(View.VISIBLE);
                imageInsideCard.setVisibility(View.VISIBLE);
            }
            else{
                imageSlider.setVisibility(View.GONE);
                imageSliderCardview.setVisibility(View.GONE);
                imageInsideCard.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.postItem_ImageInsideCard) {
                onItemClickListener.onImageSliderClick(postList.get(getAdapterPosition()));
            } else {
                onItemClickListener.onItemClick(postList.get(getAdapterPosition()));
            }
        }
    }
}
