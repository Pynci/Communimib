package it.unimib.communimib.ui.main.reports.dialogs.filters;

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
import it.unimib.communimib.databinding.FragmentFilterDialogBinding;


public class FiltersFragmentDialog extends DialogFragment {

    private FiltersViewModel filtersViewModel;
    private List<String> filters;

    public FiltersFragmentDialog () {

    }

    public FiltersFragmentDialog (FiltersViewModel filtersViewModel, List<String> filters) {
        this.filtersViewModel = filtersViewModel;
        this.filters = filters;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        FragmentFilterDialogBinding binding;
        binding = FragmentFilterDialogBinding.inflate(LayoutInflater.from(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());

        //Gestione della listView
        List<String> listaDati = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.buildings)));

        if (!listaDati.isEmpty())
            listaDati.remove(listaDati.size() - 1);

        FilterReportListViewAdapter filterReportListViewAdapter = new FilterReportListViewAdapter(
                this.getContext(),
                listaDati,
                filters,
                () -> {
                    binding.fragmentFilterCheckboxFavoriteBuildings.setChecked(false);
                    binding.fragmentFilterCheckboxAllBuildings.setChecked(false);
                }
        );
        binding.ListView.setAdapter(filterReportListViewAdapter);
        binding.ListView.setDivider(null);

        if(filters != null){
            if(!filters.isEmpty()){
                if(filters.get(0).equals("filter-by-all"))
                    binding.fragmentFilterCheckboxAllBuildings.setChecked(true);
                if(filters.get(0).equals("filter-by-favorite"))
                    binding.fragmentFilterCheckboxFavoriteBuildings.setChecked(true);
            }
        }
        else{
            binding.fragmentFilterCheckboxFavoriteBuildings.setChecked(true);
        }

        //Gestione delle checkbox
        binding.fragmentFilterCheckboxFavoriteBuildings.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.fragmentFilterCheckboxAllBuildings.setChecked(false);
                filterReportListViewAdapter.setAllItemsUnchecked();
            }
        });

        binding.fragmentFilterCheckboxAllBuildings.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.fragmentFilterCheckboxFavoriteBuildings.setChecked(false);
                filterReportListViewAdapter.setAllItemsUnchecked();
            }
        });

        //Gestione del pulsante di conferma
        binding.confirmButton.setOnClickListener(v -> {
            List<String> selectedBuildings = filterReportListViewAdapter.getCheckedItems();

            if(selectedBuildings.isEmpty()) {
                List<String> checkedBox = new ArrayList<>();

                if(binding.fragmentFilterCheckboxFavoriteBuildings.isChecked()){
                    checkedBox.add("filter-by-favorite");
                    if(filters != null && (filters.isEmpty() || !filters.get(0).equals(checkedBox.get(0))))
                            filtersViewModel.setFilters(checkedBox, this::dismiss);
                }
                else if(binding.fragmentFilterCheckboxAllBuildings.isChecked()){
                    checkedBox.add("filter-by-all");
                    if(filters == null || filters.isEmpty() || !filters.get(0).equals(checkedBox.get(0)))
                        filtersViewModel.setFilters(checkedBox, this::dismiss);
                }

            }
            else{
                if(filters == null || filters.isEmpty() || !filters.equals(selectedBuildings))
                    filtersViewModel.setFilters(selectedBuildings, this::dismiss);
            }

            this.dismiss();
        });

        //Gestione del pulsante di uscita
        binding.roolbackButton.setOnClickListener(v -> this.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return alertDialog;
    }
}
