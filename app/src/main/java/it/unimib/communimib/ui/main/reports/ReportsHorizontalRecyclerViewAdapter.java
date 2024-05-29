package it.unimib.communimib.ui.main.reports;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.util.BuildingsImagesHelper;

public class ReportsHorizontalRecyclerViewAdapter extends RecyclerView.Adapter<ReportsHorizontalRecyclerViewAdapter.ViewHolder> {

    private final boolean isUnimibUser;
    private List<Report> reportList;
    private final OnReportClickListener onReportClickListener;
    private final int layout;
    private final Context context;

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
        int position = reportList.indexOf(removedReport);
        if (position != -1) {
            reportList.remove(position);
            notifyItemRemoved(position);
        }
    }


    public boolean isReportListEmpty(){
        return reportList.isEmpty();
    }

    public void clearReportList(){
        this.reportList = new ArrayList<>();
    }


    public ReportsHorizontalRecyclerViewAdapter(boolean isUnimibUser, OnReportClickListener onReportClickListener,
                                                Context context, int layout){
        reportList = new ArrayList<>();
        this.isUnimibUser = isUnimibUser;
        this.layout = layout;
        this.onReportClickListener = onReportClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ReportsHorizontalRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(reportList.get(position));
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final CardView card;
        private final TextView title;
        private final TextView description;
        private final TextView buiding;
        private final ImageView propic;
        private final TextView name;
        private final TextView surname;
        private final Button closeButton;

        private final ImageView buildingImage;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            title = itemView.findViewById(R.id.reportListItem_title);
            description = itemView.findViewById(R.id.reportListItem_description);
            buiding = itemView.findViewById(R.id.reportListItem_building);
            propic = itemView.findViewById(R.id.reportListItem_imageView);
            name = itemView.findViewById(R.id.reportListItem_user_name);
            surname = itemView.findViewById(R.id.reportListItem_user_surname);
            closeButton = itemView.findViewById(R.id.reportListItem_closeButton);
            buildingImage = itemView.findViewById(R.id.reportListItem_buildingImage);
            card = itemView.findViewById(R.id.cardview_report);

            if (isUnimibUser){
                closeButton.setVisibility(View.VISIBLE);
            }
            closeButton.setOnClickListener(this);
            card.setOnClickListener(this);
        }

        public void bind(Report report){
            title.setText(report.getTitle());
            description.setText(report.getDescription());
            buiding.setText(report.getBuilding());
            name.setText(report.getAuthor().getName());
            surname.setText(report.getAuthor().getSurname());
            BuildingsImagesHelper.setBuildingImage(buildingImage, report.getBuilding());
            if(report.getAuthor().getPropic() != null){
                Glide
                        .with(context)
                        .load(Uri.parse(report.getAuthor().getPropic()))
                        .into(propic);
            }

            closeButton.setOnClickListener(v -> onReportClickListener.onCloseReportClick(report));
            propic.setOnClickListener(v -> onReportClickListener.onProfileClick(report.getAuthor()));
            name.setOnClickListener(v -> onReportClickListener.onProfileClick(report.getAuthor()));
            surname.setOnClickListener(v -> onReportClickListener.onProfileClick(report.getAuthor()));
        }

        @Override
        public void onClick(View v) {
            onReportClickListener.onItemClick(reportList.get(getBindingAdapterPosition()));
        }

    }


}
