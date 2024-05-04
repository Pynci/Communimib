package it.unimib.communimib.ui.main.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.communimib.R;

public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.ViewHolder> {

    private final List<String> categoriesList;
    private final Context context;
    private final OnCategoryClickListener onCategoryClickListener;

    public interface OnCategoryClickListener {
        void onItemClick(String category);
    }

    public CategoriesRecyclerViewAdapter(List<String> categories, Context context, OnCategoryClickListener onCategoryClickListener) {
        this.categoriesList = categories;
        this.context = context;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @NonNull
    @Override
    public CategoriesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoriesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(categoriesList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        boolean clicked;
        TextView categoryName;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clicked = false;
            categoryName = itemView.findViewById(R.id.categoryItem_categoryName);
            view = itemView.findViewById(R.id.categoryItem_view);
        }

        public void bind(String category){
            categoryName.setText(category);
            if(clicked){
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onClick(View v) {
            clicked = true;
            onCategoryClickListener.onItemClick(categoriesList.get(getAdapterPosition()));
        }
    }
}
