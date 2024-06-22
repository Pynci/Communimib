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

        private final ImageView propic;
        private final TextView name;
        private final TextView surname;
        private final ImageView badge;
        private final TextView description;
        private final TextView datetime;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            propic = itemView.findViewById(R.id.commentItem_propic);
            name = itemView.findViewById(R.id.commentItem_name);
            surname = itemView.findViewById(R.id.commentItem_surname);
            badge = itemView.findViewById(R.id.commentItem_unimibEmployeeBadge);
            description = itemView.findViewById(R.id.commentItem_text);
            datetime = itemView.findViewById(R.id.commentItem_datetime);
        }

        public void bind(Comment comment) {
            if (comment.getAuthor().getPropic() != null) {
                Glide.with(context).load(Uri.parse(comment.getAuthor().getPropic())).into(propic);
            }
            else{
                Glide.with(context).load(R.drawable.user_filled).into(propic);
            }

            if(comment.getAuthor().isUnimibEmployee()){
               badge.setVisibility(View.VISIBLE);
            }
            else{
                badge.setVisibility(View.GONE);
            }

            name.setText(comment.getAuthor().getName());
            surname.setText(comment.getAuthor().getSurname());
            description.setText(comment.getText());
            datetime.setText(DateFormatter.format(comment.getTimestamp(), context));
        }
    }
}
