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

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private List<Comment> commentList;
    private final Context context;

    public CommentsAdapter(Context context) {
        commentList = new ArrayList<>();
        this.context = context;
    }

    public void addItem(Comment newComment) {
        if (!commentList.contains(newComment)) {
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
            notifyItemRemoved(position);
        }
    }

    public void clearCommentList() {
        this.commentList = new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        holder.bind(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView userPropic;
        private final TextView userName;
        private final TextView userSurname;
        private final TextView commentDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userPropic = itemView.findViewById(R.id.commentItem_propic);
            userName = itemView.findViewById(R.id.commentItem_name);
            userSurname = itemView.findViewById(R.id.commentItem_surname);
            commentDescription = itemView.findViewById(R.id.commentItem_text);
        }

        public void bind(Comment comment) {

            if(comment.getCommentCreator().getPropic() != null){
                Glide
                        .with(context)
                        .load(Uri.parse(comment.getCommentCreator().getPropic()))
                        .into(userPropic);
            }

            userName.setText(comment.getCommentCreator().getName());
            userSurname.setText(comment.getCommentCreator().getSurname());
            commentDescription.setText(comment.getText());
        }
    }
}
