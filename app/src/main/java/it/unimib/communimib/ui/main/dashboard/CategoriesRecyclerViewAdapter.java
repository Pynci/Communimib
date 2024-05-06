package it.unimib.communimib.ui.main.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.communimib.R;

public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.ViewHolder> {

    private final List<String> categoriesList;
    private final OnCategoryClickListener onCategoryClickListener;

    private String currentCategory;

    public interface OnCategoryClickListener {
        void onItemClick(String category);
    }

    public void setCurrentCategory(String category){
        currentCategory = category;
        notifyDataSetChanged();
    }

    public CategoriesRecyclerViewAdapter(List<String> categories, OnCategoryClickListener onCategoryClickListener) {
        this.categoriesList = categories;
        this.onCategoryClickListener = onCategoryClickListener;
        this.currentCategory = "Tutti";
    }

    @NonNull
    @Override
    public CategoriesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoriesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesRecyclerViewAdapter.ViewHolder holder, int position) {
        if(categoriesList.get(position).equals(currentCategory)){
            holder.bindClicked(categoriesList.get(position));
        } else {
            holder.bind(categoriesList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView categoryName;
        View view;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryItem_categoryName);
            view = itemView.findViewById(R.id.categoryItem_view);
            constraintLayout = itemView.findViewById(R.id.categoryItem_constraintLayout);

            constraintLayout.setOnClickListener(this);
        }

        public void bindClicked(String category){
            categoryName.setText(category);
            view.setVisibility(View.VISIBLE);
        }
        public void bind(String category){
            categoryName.setText(category);
            view.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            onCategoryClickListener.onItemClick(categoriesList.get(getAdapterPosition()));
        }
    }
}
