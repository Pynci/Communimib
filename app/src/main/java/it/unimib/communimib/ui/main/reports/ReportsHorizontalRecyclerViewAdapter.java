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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.model.Report;

public class ReportsHorizontalRecyclerViewAdapter extends RecyclerView.Adapter<ReportsHorizontalRecyclerViewAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onCloseReportClick(Report report);
    }
    private final boolean isUnimibUser;
    private String category;
    private List<Report> reportList;
    private final OnItemClickListener onItemClickListener;
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

    public void setCategory(String category){
        this.category = category;
    }

    public boolean isReportListEmpty(){
        return reportList.isEmpty();
    }

    public void clearReportList(){
        this.reportList = new ArrayList<>();
    }


    public ReportsHorizontalRecyclerViewAdapter(boolean isUnimibUser, OnItemClickListener onItemClickListener,
                                                Context context, int layout){
        reportList = new ArrayList<>();
        this.isUnimibUser = isUnimibUser;
        this.layout = layout;
        this.onItemClickListener = onItemClickListener;
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
            setBuildingImage(report.getBuilding());
            Glide
                    .with(context)
                    .load(Uri.parse(report.getAuthor().getPropic()))
                    .into(propic);

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.reportListItem_closeButton){
                onItemClickListener.onCloseReportClick(reportList.get(getAdapterPosition()));
            }
        }

        public void setBuildingImage(String building){
            switch (building){
                case "U1": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou1_foreground);
                    break;
                }
                case "U2": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou2_foreground);
                    break;
                }
                case "U3": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou3_foreground);
                    break;
                }
                case "U4": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou4_foreground);
                    break;
                }
                case "U5": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou5_foreground);
                    break;
                }
                case "U6": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou6_foreground);
                    break;
                }
                case "U7": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou7_foreground);
                    break;
                }
                case "U9": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou9_foreground);
                    break;
                }
                case "U10": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou10_foreground);
                    break;
                }
                case "U11": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou11_foreground);
                    break;
                }
                case "U14": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou14_foreground);
                    break;
                }
                case "U16": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou16_foreground);
                    break;
                }
                case "U17": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou17_foreground);
                    break;
                }
                case "U19": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou19_foreground);
                    break;
                }
                case "U22": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou22_foreground);
                    break;
                }
                case "U24": {
                    buildingImage.setBackgroundResource(R.mipmap.edificiou24_foreground);
                    break;
                }
                default: {
                    buildingImage.setBackgroundResource(R.mipmap.no_image_foreground);
                }
            }
        }
    }


}
