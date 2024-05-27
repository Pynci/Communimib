package it.unimib.communimib.model;

import it.unimib.communimib.ui.main.reports.ReportsHorizontalRecyclerViewAdapter;

public class BuildingReport {

    private final String buildingName;
    private final ReportsHorizontalRecyclerViewAdapter reportsHorizontalRecyclerViewAdapter;

    public BuildingReport(String buildingName, ReportsHorizontalRecyclerViewAdapter reportsHorizontalRecyclerViewAdapter) {
        this.buildingName = buildingName;
        this.reportsHorizontalRecyclerViewAdapter = reportsHorizontalRecyclerViewAdapter;
    }

    public String getBuildingName() {
        return buildingName;
    }


    public ReportsHorizontalRecyclerViewAdapter getReportsHorizontalRecyclerViewAdapter() {
        return reportsHorizontalRecyclerViewAdapter;
    }

}
