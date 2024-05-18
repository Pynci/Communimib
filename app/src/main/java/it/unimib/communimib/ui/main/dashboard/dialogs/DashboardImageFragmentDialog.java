package it.unimib.communimib.ui.main.dashboard.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import it.unimib.communimib.databinding.FragmentDashboardImageDialogBinding;
import it.unimib.communimib.model.Post;

public class DashboardImageFragmentDialog extends DialogFragment {

    private final Post post;

    public DashboardImageFragmentDialog(Post post){
        this.post = post;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        FragmentDashboardImageDialogBinding fragmentDashboardImageDialogBinding = FragmentDashboardImageDialogBinding.inflate(getLayoutInflater());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(fragmentDashboardImageDialogBinding.getRoot());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        DashboardImageRecyclerViewAdapter dashboardImageRecyclerViewAdapter = new DashboardImageRecyclerViewAdapter(post.getPictures(), getContext());
        fragmentDashboardImageDialogBinding.dashboardImageDialogRecyclerView.setLayoutManager(layoutManager);
        fragmentDashboardImageDialogBinding.dashboardImageDialogRecyclerView.setAdapter(dashboardImageRecyclerViewAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(fragmentDashboardImageDialogBinding.dashboardImageDialogRecyclerView);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return alertDialog;
    }


}