package it.unimib.communimib.ui.main.reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.model.Report;

public class ReportsRecyclerViewAdapter extends RecyclerView.Adapter<ReportsRecyclerViewAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onCloseReportClick(Report report);
    }
    private final boolean isUnimibUser;
    private final List<Report> reportList;
    private final OnItemClickListener onItemClickListener;
    private final int layout;

    public void addItem(Report newReport) {
        reportList.add(0, newReport);
        notifyItemInserted(0);
    }

    public void editItem(Report editedReport) {
        int position = reportList.indexOf(editedReport);
        if (position != -1) {
            reportList.set(position, editedReport);
            notifyItemChanged(position);
        }
    }

    public void removeItem(Report removedReport) {
        int position = -1;
        for (int i = 0; i < reportList.size(); i++) {
            if(reportList.get(i).getRid().equals(removedReport.getRid())){
                position = i;
                break;
            }
        }
        if (position != -1) {
            reportList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ReportsRecyclerViewAdapter(boolean isUnimibUser, int layout, OnItemClickListener onItemClickListener){
        reportList = new ArrayList<>();
        this.isUnimibUser = isUnimibUser;
        this.layout = layout;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ReportsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(reportList.get(position));
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView title;
        private final TextView description;
        private final TextView buiding;
        private final ImageView propic;
        private final TextView name;
        private final TextView surname;
        private final Button closeButton;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            title = itemView.findViewById(R.id.reportListItem_title);
            description = itemView.findViewById(R.id.reportListItem_description);
            buiding = itemView.findViewById(R.id.reportListItem_building);
            propic = itemView.findViewById(R.id.reportListItem_imageView);
            name = itemView.findViewById(R.id.reportListItem_user_name);
            surname = itemView.findViewById(R.id.reportListItem_user_surname);
            closeButton = itemView.findViewById(R.id.reportListItem_closeButton);
            if (isUnimibUser){
                closeButton.setVisibility(View.VISIBLE);
            }
            closeButton.setOnClickListener(this);
        }

        public void bind(Report report){
            title.setText(report.getTitle());
            description.setText(report.getDescription());
            buiding.setText(report.getBuilding());
            name.setText(report.getAuthor().getName());
            surname.setText(report.getAuthor().getSurname());
            //propic.setImageResource();

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.reportListItem_closeButton){
                onItemClickListener.onCloseReportClick(reportList.get(getAdapterPosition()));
            }
        }
    }


}
