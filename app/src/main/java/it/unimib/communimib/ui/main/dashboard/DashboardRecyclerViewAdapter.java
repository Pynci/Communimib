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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Post post){

        }

        @Override
        public void onClick(View v) {

        }
    }
}
