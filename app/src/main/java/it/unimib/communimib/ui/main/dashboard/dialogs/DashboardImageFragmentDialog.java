package it.unimib.communimib.ui.main.dashboard.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.denzcoskun.imageslider.constants.ActionTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.TouchListener;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.databinding.FragmentDashboardImageDialogBinding;
import it.unimib.communimib.databinding.FragmentFavoriteDialogBinding;
import it.unimib.communimib.model.Post;

public class DashboardImageFragmentDialog extends DialogFragment {

    private FragmentDashboardImageDialogBinding fragmentDashboardImageDialogBinding;

    private final Post post;

    public DashboardImageFragmentDialog(Post post){
        this.post = post;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        fragmentDashboardImageDialogBinding = FragmentDashboardImageDialogBinding.inflate(getLayoutInflater());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(fragmentDashboardImageDialogBinding.getRoot());

        List<SlideModel> slideModels = new ArrayList<>();
        for (String picture : post.getPictures()) {
            slideModels.add(new SlideModel(picture, ScaleTypes.CENTER_INSIDE));
        }
        fragmentDashboardImageDialogBinding.imageDialogImageSlider.setImageList(slideModels, ScaleTypes.CENTER_INSIDE);

        fragmentDashboardImageDialogBinding.imageDialogImageSlider.setTouchListener((actionTypes, i) -> {

        });


        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return alertDialog;
    }
}