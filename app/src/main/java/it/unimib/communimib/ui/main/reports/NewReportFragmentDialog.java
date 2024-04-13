package it.unimib.communimib.ui.main.reports;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Arrays;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentNewReportDialogBinding;

public class NewReportFragmentDialog extends DialogFragment {

    private FragmentNewReportDialogBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Utilizza il binding per inflare il layout
        binding = FragmentNewReportDialogBinding.inflate(LayoutInflater.from(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());

        binding.rollbackNewReport.setOnClickListener(v -> {
            this.dismiss();
        });

        //Gestione spinner edifici
        binding.spinnerEdifici.setPrompt("Edificio");
        ArrayAdapter<String> adapterEdificio = getBuildingsAdapter();
        binding.spinnerEdifici.setAdapter(adapterEdificio);
        binding.spinnerEdifici.setSelection(adapterEdificio.getCount());

        //Gestione spinner categorie
        binding.spinnerCategorie.setPrompt("Categoria");
        ArrayAdapter<String> adapterCategorie = getCategoriesAdapter();
        binding.spinnerCategorie.setAdapter(adapterCategorie);
        binding.spinnerCategorie.setSelection(adapterCategorie.getCount());

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return alertDialog;
    }

    @NonNull
    private ArrayAdapter<String> getBuildingsAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }

        };

        String[] buildingsArray = getResources().getStringArray(R.array.buildings);
        adapter.addAll(Arrays.asList(buildingsArray));
        return adapter;
    }

    @NonNull
    private ArrayAdapter<String> getCategoriesAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }

        };

        String[] categoriesArray = getResources().getStringArray(R.array.reports_categories);
        adapter.addAll(Arrays.asList(categoriesArray));
        return adapter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Pulisci il binding quando la view viene distrutta
    }

}