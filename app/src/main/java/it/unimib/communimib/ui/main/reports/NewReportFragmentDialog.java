package it.unimib.communimib.ui.main.reports;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Arrays;
import java.util.Objects;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentNewReportDialogBinding;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.Validation;

public class NewReportFragmentDialog extends DialogFragment {

    private FragmentNewReportDialogBinding binding;

    private final ReportsViewModel reportsViewModel;

    public NewReportFragmentDialog(ReportsViewModel reportsViewModel) {
        this.reportsViewModel = reportsViewModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Utilizza il binding per inflare il layout
        binding = FragmentNewReportDialogBinding.inflate(getLayoutInflater());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());

        //Gestione spinner edifici
        binding.buildingsSpinner.setPrompt("Edificio");
        ArrayAdapter<String> adapterEdificio = getBuildingsAdapter();
        binding.buildingsSpinner.setAdapter(adapterEdificio);
        binding.buildingsSpinner.setSelection(adapterEdificio.getCount());
        binding.buildingsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.newReportErrorSpinnerBuildings.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Non deve fare niente
            }
        });

        //Gestione spinner categorie
        binding.categoriesSpinner.setPrompt("Categoria");
        ArrayAdapter<String> adapterCategorie = getCategoriesAdapter();
        binding.categoriesSpinner.setAdapter(adapterCategorie);
        binding.categoriesSpinner.setSelection(adapterCategorie.getCount());
        binding.categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.newReportErrorSpinnerCategories.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Non deve fare niente
            }
        });

        AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        //Gestione del pulsante di conferma
        binding.confirmNewReport.setOnClickListener(v -> {

            String buildingSpinnerSelectedItem = binding.buildingsSpinner.getSelectedItem().toString();
            String categoriesSpinnerSelectedItem =  binding.categoriesSpinner.getSelectedItem().toString();

            String resultBuildingsValidation = Validation.checkBuildingsSpinner(buildingSpinnerSelectedItem);
            String resultCategoriesValidation = Validation.checkCategoriesSpinner(categoriesSpinnerSelectedItem);

            if(!resultBuildingsValidation.equals("ok")){
                binding.newReportErrorSpinnerBuildings.setVisibility(View.VISIBLE);
                binding.newReportErrorSpinnerBuildings.setText(ErrorMapper.getInstance().getErrorMessage(resultCategoriesValidation));
            }
            if(!resultCategoriesValidation.equals("ok")){
                binding.newReportErrorSpinnerCategories.setVisibility(View.VISIBLE);
                binding.newReportErrorSpinnerCategories.setText(ErrorMapper.getInstance().getErrorMessage(resultCategoriesValidation));
            }
            if(resultBuildingsValidation.equals("ok") && resultCategoriesValidation.equals("ok"))
                reportsViewModel.createReport(
                        binding.editTextReportTitle.getText().toString(),
                        binding.editTextReportDescription.getText().toString(),
                        buildingSpinnerSelectedItem,
                        categoriesSpinnerSelectedItem);

        });

        //Gestione del pulstante di annullamento
        binding.rollbackNewReport.setOnClickListener(v -> this.dismiss());

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
                return super.getCount() - 1; // you dont display last item. It is used as hint.
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
                return super.getCount() - 1; // you dont display last item. It is used as hint.
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