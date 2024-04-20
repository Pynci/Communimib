package it.unimib.communimib.ui.main.reports.dialogs.favourites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import it.unimib.communimib.databinding.FragmentFavoriteDialogBinding;
public class FavoriteFragmentDialog extends DialogFragment {

    private FragmentFavoriteDialogBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Utilizza il binding per inflare il layout
        binding = FragmentFavoriteDialogBinding.inflate(LayoutInflater.from(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return alertDialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Pulisci il binding quando la view viene distrutta
    }
}