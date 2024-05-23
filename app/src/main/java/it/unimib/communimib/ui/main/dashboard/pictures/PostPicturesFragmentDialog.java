package it.unimib.communimib.ui.main.dashboard.pictures;

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

import java.util.List;

import it.unimib.communimib.databinding.FragmentPostPicturesBinding;

public class PostPicturesFragmentDialog extends DialogFragment {

    private final List<String> pictures;

    public PostPicturesFragmentDialog(List<String> pictures){
        this.pictures = pictures;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        FragmentPostPicturesBinding fragmentDashboardImageDialogBinding = FragmentPostPicturesBinding.inflate(getLayoutInflater());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(fragmentDashboardImageDialogBinding.getRoot());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        PostPicturesRecyclerViewAdapter postPicturesRecyclerViewAdapter = new PostPicturesRecyclerViewAdapter(pictures, getContext());
        fragmentDashboardImageDialogBinding.dashboardImageDialogRecyclerView.setLayoutManager(layoutManager);
        fragmentDashboardImageDialogBinding.dashboardImageDialogRecyclerView.setAdapter(postPicturesRecyclerViewAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(fragmentDashboardImageDialogBinding.dashboardImageDialogRecyclerView);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return alertDialog;
    }


}