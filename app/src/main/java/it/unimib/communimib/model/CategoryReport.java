package it.unimib.communimib.model;

import java.util.List;

import it.unimib.communimib.ui.main.reports.ReportsHorizontalRecyclerViewAdapter;

public class CategoryReport {

    private String categoryName;
    private ReportsHorizontalRecyclerViewAdapter reportsHorizontalRecyclerViewAdapter;

    public CategoryReport(String categoryName, ReportsHorizontalRecyclerViewAdapter reportsHorizontalRecyclerViewAdapter) {
        this.categoryName = categoryName;
        this.reportsHorizontalRecyclerViewAdapter = reportsHorizontalRecyclerViewAdapter;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ReportsHorizontalRecyclerViewAdapter getReportsHorizontalRecyclerViewAdapter() {
        return reportsHorizontalRecyclerViewAdapter;
    }

    public void setReportsHorizontalRecyclerViewAdapter(ReportsHorizontalRecyclerViewAdapter reportsHorizontalRecyclerViewAdapter) {
        this.reportsHorizontalRecyclerViewAdapter = reportsHorizontalRecyclerViewAdapter;
    }

}
