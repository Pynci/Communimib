package it.unimib.communimib.ui.main.reports;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.model.Report;

public class ReportsRecyclerViewAdapter extends RecyclerView.Adapter<ReportsRecyclerViewAdapter.ViewHolder> {

    private final boolean isUnimibUser;
    private final List<Report> reportList;

    public ReportsRecyclerViewAdapter(List<Report> reportList, boolean isUnimibUser){
        this.reportList = reportList;
        this.isUnimibUser = isUnimibUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_list_item, parent, false);
        return new ReportsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if(reportList != null){
            return reportList.size();
        }
        else{
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Report report){

        }

    }

    public interface ItemClickCallback {
        void onCloseReportClick(Report report);
    }
}
