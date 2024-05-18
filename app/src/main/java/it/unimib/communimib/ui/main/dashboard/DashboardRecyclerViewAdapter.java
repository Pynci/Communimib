package it.unimib.communimib.ui.main.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.model.Post;

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private List<Post> postList;
    private final OnPostClickListener onPostClickListener;
    private final Context context;

    public void addItem(Post newPost) {
        if (!postList.contains(newPost)) {
            postList.add(0, newPost);
            notifyItemInserted(0);
        }
    }

    public void editItem(Post editedPost) {
        int position = postList.indexOf(editedPost);
        if (position != -1) {
            postList.set(position, editedPost);
            notifyItemChanged(position);
        }
    }

    public void removeItem(Post removedPost) {
        int position = postList.indexOf(removedPost);
        if (position != -1) {
            postList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clearPostList() {
        this.postList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public DashboardRecyclerViewAdapter(OnPostClickListener onPostClickListener, Context context) {
        this.postList = new ArrayList<>();
        this.onPostClickListener = onPostClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view, context, onPostClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bind(postList.get(position));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
