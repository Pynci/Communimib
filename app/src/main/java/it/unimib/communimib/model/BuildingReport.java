package it.unimib.communimib.model;

import it.unimib.communimib.ui.main.reports.ReportsHorizontalRecyclerViewAdapter;

public class BuildingReport {

    private String buildingName;
    private ReportsHorizontalRecyclerViewAdapter reportsHorizontalRecyclerViewAdapter;

    public BuildingReport(String buildingName, ReportsHorizontalRecyclerViewAdapter reportsHorizontalRecyclerViewAdapter) {
        this.buildingName = buildingName;
        this.reportsHorizontalRecyclerViewAdapter = reportsHorizontalRecyclerViewAdapter;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public ReportsHorizontalRecyclerViewAdapter getReportsHorizontalRecyclerViewAdapter() {
        return reportsHorizontalRecyclerViewAdapter;
    }

    public void setReportsHorizontalRecyclerViewAdapter(ReportsHorizontalRecyclerViewAdapter reportsHorizontalRecyclerViewAdapter) {
        this.reportsHorizontalRecyclerViewAdapter = reportsHorizontalRecyclerViewAdapter;
    }

}
