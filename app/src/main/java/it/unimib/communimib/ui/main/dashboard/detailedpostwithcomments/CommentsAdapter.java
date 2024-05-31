package it.unimib.communimib.ui.main.dashboard.detailedpostwithcomments;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.model.Comment;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.ui.main.dashboard.OnPostClickListener;
import it.unimib.communimib.ui.main.dashboard.PostViewHolder;
import it.unimib.communimib.util.DateFormatter;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Comment> commentList;
    private TextView comments;
    private final Context context;

    public CommentsAdapter(Context context, View view) {
        this.commentList = new ArrayList<>();
        this.context = context;
        comments = view.findViewById(R.id.comments);
    }

    public void addItem(Comment newComment) {
        if (!commentList.contains(newComment)) {
            if(commentList.isEmpty()){
                comments.setVisibility(View.VISIBLE);
            }
            commentList.add(0, newComment);
            notifyItemInserted(0);
        }
    }

    public void editItem(Comment editedComment) {
        int position = commentList.indexOf(editedComment);
        if (position != -1) {
            commentList.set(position, editedComment);
            notifyItemChanged(position);
        }
    }

    public void removeItem(Comment removedComment) {
        int position = commentList.indexOf(removedComment);
        if (position != -1) {
            commentList.remove(position);
            if(commentList.isEmpty()){
                comments.setVisibility(View.INVISIBLE);
            }
            notifyItemRemoved(position);
        }
    }

    public void clearCommentList() {
        this.commentList = new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CommentViewHolder) holder).bind(commentList.get(position)); // Adjust for the post
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private final ImageView userPropic;
        private final TextView userName;
        private final TextView userSurname;
        private final TextView commentDescription;
        private final TextView dateTime;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userPropic = itemView.findViewById(R.id.commentItem_propic);
            userName = itemView.findViewById(R.id.commentItem_name);
            userSurname = itemView.findViewById(R.id.commentItem_surname);
            commentDescription = itemView.findViewById(R.id.commentItem_text);
            dateTime = itemView.findViewById(R.id.commentItem_datetime);
        }

        public void bind(Comment comment) {
            if (comment.getAuthor().getPropic() != null) {
                Glide.with(context).load(Uri.parse(comment.getAuthor().getPropic())).into(userPropic);
            }
            else{
                Glide.with(context).load(R.drawable.user_filled).into(userPropic);
            }
            userName.setText(comment.getAuthor().getName());
            userSurname.setText(comment.getAuthor().getSurname());
            commentDescription.setText(comment.getText());
            dateTime.setText(DateFormatter.format(comment.getTimestamp(), context));
        }
    }
}
