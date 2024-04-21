package it.unimib.communimib.ui.main.reports;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.model.CategoryReport;
import it.unimib.communimib.model.Report;

public class ReportMainRecyclerViewAdapter extends RecyclerView.Adapter<ReportMainRecyclerViewAdapter.ViewHolder> {

    //private final List<String> categoryList;
    //private final List<ReportsHorizontalRecyclerViewAdapter> reportsHorizontalRecyclerViewAdapterList;

    private final List<CategoryReport> categoryReportList;

    public ReportMainRecyclerViewAdapter(List<CategoryReport> categoryReportList) {
        this.categoryReportList = categoryReportList;
    }


    public void addItem(String category, Report report){
        for (CategoryReport categoryReport: categoryReportList) {
            if(categoryReport.getCategoryName().equals(category)){
                categoryReport.getReportsHorizontalRecyclerViewAdapter().addItem(report);
            }
        }
    }

    public void editItem(String category, Report report){
        for (CategoryReport categoryReport: categoryReportList) {
            if(categoryReport.getCategoryName().equals(category)){
                categoryReport.getReportsHorizontalRecyclerViewAdapter().editItem(report);
            }
        }
    }

    public void removeItem(String category, Report report){
        for (CategoryReport categoryReport: categoryReportList) {
            if(categoryReport.getCategoryName().equals(category)){
                categoryReport.getReportsHorizontalRecyclerViewAdapter().removeItem(report);
            }
        }
    }


    @NonNull
    @Override
    public ReportMainRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_horizontal_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportMainRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(categoryReportList.get(position));
    }

    @Override
    public int getItemCount() {
        if(categoryReportList != null){
            return categoryReportList.size();
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

        public void bind(CategoryReport categoryReport){
            categoryName.setText(categoryReport.getCategoryName());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            horizontalRecyclerView.setLayoutManager(layoutManager);
            horizontalRecyclerView.setAdapter(categoryReport.getReportsHorizontalRecyclerViewAdapter());
        }

        @Override
        public void onClick(View v) {

        }
    }
}
