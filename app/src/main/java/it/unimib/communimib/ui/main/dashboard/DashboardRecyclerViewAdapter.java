package it.unimib.communimib.ui.main.dashboard;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.util.DateFormatter;

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter<DashboardRecyclerViewAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(Post post);
    }

    private List<Post> postList;
    private final OnItemClickListener onItemClickListener;
    private Context context;

    public void addItem(Post newPost){
        postList.add(newPost);
        notifyItemInserted(0);
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

    public DashboardRecyclerViewAdapter(OnItemClickListener onItemClickListener, Context context) {
        this.postList = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public void cleanPostList(){
        this.postList = new ArrayList<>();
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

        private final ConstraintLayout constraintLayout;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.postItem_constraintLayout);
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

            constraintLayout.setOnClickListener(this);
        }

        public void bind(Post post){
            name.setText(post.getAuthor().getName());
            surname.setText(post.getAuthor().getSurname());
            title.setText(post.getTitle());
            description.setText(post.getDescription());
            if(post.getEmail() != null && !post.getEmail().equals("")){
                email.setText(post.getEmail());
            }
            else{
                email.setVisibility(View.GONE);
                emailIcon.setVisibility(View.GONE);
            }
            if(post.getLink() != null && !post.getLink().equals("")){
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

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.postItem_constraintLayout){
                onItemClickListener.onItemClick(postList.get(getAdapterPosition()));
            }
        }
    }
}
