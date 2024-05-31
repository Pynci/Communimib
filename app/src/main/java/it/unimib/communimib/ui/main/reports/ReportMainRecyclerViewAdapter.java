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
import it.unimib.communimib.model.BuildingReport;
import it.unimib.communimib.model.Report;

public class ReportMainRecyclerViewAdapter extends RecyclerView.Adapter<ReportMainRecyclerViewAdapter.ViewHolder> {

    private final List<BuildingReport> buildingReportList;

    public ReportMainRecyclerViewAdapter(List<BuildingReport> buildingReportList) {
        this.buildingReportList = buildingReportList;
    }


    public void addItem(String building, Report report){
        for (BuildingReport buildingReport : buildingReportList) {
            if(buildingReport.getBuildingName().equals(building)){
                if(buildingReport.getReportsHorizontalRecyclerViewAdapter().isReportListEmpty()){
                    notifyDataSetChanged();
                }
                buildingReport.getReportsHorizontalRecyclerViewAdapter().addItem(report);
            }
        }
    }

    public void editItem(String building, Report report){
        for (BuildingReport buildingReport : buildingReportList) {
            if(buildingReport.getBuildingName().equals(building)){
                buildingReport.getReportsHorizontalRecyclerViewAdapter().editItem(report);
            }
        }
    }

    public void removeItem(String building, Report report){
        for (BuildingReport buildingReport : buildingReportList) {
            if(buildingReport.getBuildingName().equals(building)){
                buildingReport.getReportsHorizontalRecyclerViewAdapter().removeItem(report);
                if(buildingReport.getReportsHorizontalRecyclerViewAdapter().isReportListEmpty()){
                    notifyDataSetChanged();
                }
            }
        }
    }

    public boolean isEmpty(){
        for (BuildingReport buildingReport: buildingReportList) {
            if(!buildingReport.getReportsHorizontalRecyclerViewAdapter().isReportListEmpty()){
                return false;
            }
        }
        return true;
    }

    public void clearHorizontalAdapters(){
        for (BuildingReport buildingReport : buildingReportList) {
            buildingReport.getReportsHorizontalRecyclerViewAdapter().clearReportList();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReportMainRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_horizontal_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportMainRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(buildingReportList.get(position));
    }

    @Override
    public int getItemCount() {
        if(buildingReportList != null){
            return buildingReportList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView buildingName;
        private View marginTop;
        private View marginBottom;
        private RecyclerView horizontalRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            marginTop = itemView.findViewById(R.id.horizontalRecyclerViewItem_marginTop);
            marginBottom = itemView.findViewById(R.id.horizontalRecyclerViewItem_marginBottom);
            buildingName = itemView.findViewById(R.id.reportHorizontalRecyclerViewItem_buildingName);
            horizontalRecyclerView = itemView.findViewById(R.id.reportHorizontalRecyclerViewItem_recyclerView);

        }

        public void bind(BuildingReport buildingReport){
            buildingName.setText(buildingReport.getBuildingName());
            if(buildingReport.getReportsHorizontalRecyclerViewAdapter().isReportListEmpty()){
                buildingName.setVisibility(View.GONE);
                marginTop.setVisibility(View.GONE);
                marginBottom.setVisibility(View.GONE);
            } else {
                buildingName.setVisibility(View.VISIBLE);
                marginTop.setVisibility(View.VISIBLE);
                marginBottom.setVisibility(View.VISIBLE);
            }
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            horizontalRecyclerView.setLayoutManager(layoutManager);
            horizontalRecyclerView.setAdapter(buildingReport.getReportsHorizontalRecyclerViewAdapter());
        }

        @Override
        public void onClick(View v) {
            // qui non ci va niente, lasciare vuoto
        }
    }
}
