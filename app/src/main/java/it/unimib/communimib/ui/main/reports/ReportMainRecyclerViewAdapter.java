package it.unimib.communimib.ui.main.reports;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.model.Report;

public class ReportMainRecyclerViewAdapter extends RecyclerView.Adapter<ReportMainRecyclerViewAdapter.ViewHolder> {

    private final List<String> categoryList;

    public ReportMainRecyclerViewAdapter(List<String> categoryList) {
        this.categoryList = categoryList;
    }


    @NonNull
    @Override
    public ReportMainRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_horizontal_recycler_view_item, parent, false);
        return new ViewHolder(view); //TODO da riguardare se ci sono errori
    }

    @Override
    public void onBindViewHolder(@NonNull ReportMainRecyclerViewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if(categoryList != null){
            return categoryList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView categoryName;
        private RecyclerView horizontalRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.reportHorizontalRecyclerViewItem_categoryName);
            horizontalRecyclerView = itemView.findViewById(R.id.reportHorizontalRecyclerViewItem_recyclerView);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
