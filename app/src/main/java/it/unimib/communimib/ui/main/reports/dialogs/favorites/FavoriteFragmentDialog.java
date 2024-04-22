package it.unimib.communimib.ui.main.reports.dialogs.favorites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.unimib.communimib.R;
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

        //Gestione Listview
        List<String> listaDati = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.buildings)));

        if (!listaDati.isEmpty())
            listaDati.remove(listaDati.size() - 1);

        FavoriteBuildingsAdapter filterReportListViewAdapter = new FavoriteBuildingsAdapter(
                this.getContext(),
                listaDati,
                new ArrayList<>()
        );
        binding.favoriteFragmentListview.setAdapter(filterReportListViewAdapter);
        binding.favoriteFragmentListview.setDivider(null);

        //Gestione pulsante chiusura
        binding.fragmentFavoriteRollbackButton.setOnClickListener(v -> {
            this.dismiss();
        });

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