package it.unimib.communimib.ui.main.dashboard;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.util.DateFormatter;

public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
    private final TextView comments;
    private final ImageView commentIcon;
    private final ImageSlider imageSlider;
    private final CardView imageSliderCardview;

    private Post post;
    private final OnPostClickListener onPostClickListener;
    private final Context context;

    public PostViewHolder(@NonNull View itemView, Context context, OnPostClickListener onPostClickListener) {
        super(itemView);

        this.onPostClickListener = onPostClickListener;
        this.context = context;

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
        comments = itemView.findViewById(R.id.postItem_comments);
        commentIcon = itemView.findViewById(R.id.postItem_commentIcon);
        imageSlider = itemView.findViewById(R.id.postItem_imageSlider);
        imageSliderCardview = itemView.findViewById(R.id.postItem_imageSliderCardView);

        itemView.setOnClickListener(this);
    }

    public void bind(Post post) {
        this.post = post;

        name.setText(post.getAuthor().getName());
        surname.setText(post.getAuthor().getSurname());
        title.setText(post.getTitle());
        description.setText(post.getDescription());
        if (post.getEmail() != null && !post.getEmail().isEmpty()) {
            email.setText(post.getEmail());
        } else {
            email.setVisibility(View.GONE);
            emailIcon.setVisibility(View.GONE);
        }
        if (post.getLink() != null && !post.getLink().isEmpty()) {
            link.setText(post.getLink());
        } else {
            link.setVisibility(View.GONE);
            linkIcon.setVisibility(View.GONE);
        }
        dateTime.setText(DateFormatter.format(post.getTimestamp(), context));
        if (post.getAuthor().getPropic() != null) {
            Glide.with(context)
                    .load(Uri.parse(post.getAuthor().getPropic()))
                    .into(propic);
        }

        if(post.getComments() > 0){
            comments.setText(String.valueOf(post.getComments()));
            comments.setVisibility(View.VISIBLE);
            commentIcon.setVisibility(View.VISIBLE);
        }
        else{
            comments.setVisibility(View.GONE);
            commentIcon.setVisibility(View.GONE);
        }

        List<SlideModel> slideModels = new ArrayList<>();
        if (!post.getPictures().isEmpty()) {
            for (String picture : post.getPictures()) {
                slideModels.add(new SlideModel(picture, ScaleTypes.CENTER_CROP));
            }
            imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
            imageSlider.setVisibility(View.VISIBLE);
            imageSliderCardview.setVisibility(View.VISIBLE);
        } else {
            imageSlider.setVisibility(View.GONE);
            imageSliderCardview.setVisibility(View.GONE);
        }

        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                onPostClickListener.onImageSliderClick(post);
            }

            @Override
            public void doubleClick(int i) {
                //per ora non serve
            }
        });
    }

    @Override
    public void onClick(View v) {
        onPostClickListener.onItemClick(post);
    }
}
