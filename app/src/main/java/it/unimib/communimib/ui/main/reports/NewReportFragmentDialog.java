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

    private final ReportsCreationViewModel reportsCreationViewModel;

    public NewReportFragmentDialog(ReportsCreationViewModel reportsCreationViewModel) {
        this.reportsCreationViewModel = reportsCreationViewModel;
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

            String chosenTitle = binding.editTextReportTitle.getText().toString();
            String chosenDescription = binding.editTextReportDescription.getText().toString();
            String buildingSpinnerSelectedItem = binding.buildingsSpinner.getSelectedItem().toString();
            String categoriesSpinnerSelectedItem =  binding.categoriesSpinner.getSelectedItem().toString();

            String resultBuildingsValidation = Validation.checkBuildingsSpinner(buildingSpinnerSelectedItem);
            String resultCategoriesValidation = Validation.checkCategoriesSpinner(categoriesSpinnerSelectedItem);
            String resultValidationTitle = Validation.checkEmptyField(chosenTitle);
            String resultValidationDescription = Validation.checkEmptyField(chosenTitle);

            /*
            * Ricorda: tutto sto casino nasce dal fatto che:
            * 1) Lo spinner non ha un errore integrato e quindi si deve usare la label
            * 2) Utilizzare l'observer qui dentro è impossibile (l'applicativo va in crash)
            * 3) non puoi stamapre il messaggio di errore con la snackbar
            *
            * Ore perse per cercare di ottimizzare questa struttura: 3
             */


            checkAndSetErrors(resultBuildingsValidation, resultCategoriesValidation, resultValidationTitle, resultValidationDescription);

            v.clearFocus();

            reportsCreationViewModel.createReport(
                    chosenTitle,
                    chosenDescription,
                    buildingSpinnerSelectedItem,
                    categoriesSpinnerSelectedItem,
                    this::dismiss);
        });

        //Gestione del pulstante di annullamento
        binding.rollbackNewReport.setOnClickListener(v -> this.dismiss());

        //Gestione dell'errore del titolo
        binding.editTextReportTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.newReportTitleError.setVisibility(View.INVISIBLE);
            }
            else{
                String text = binding.editTextReportTitle.getText().toString();
                String validationResult = Validation.checkEmptyField(text);
                if (validationResult.equals("ok"))
                    binding.newReportTitleError.setVisibility(View.INVISIBLE);
                else{
                    binding.newReportTitleError.setText(ErrorMapper.getInstance().getErrorMessage(validationResult));
                    binding.newReportTitleError.setVisibility(View.VISIBLE);
                }
            }
        });

        //Gestione dell'errore della descrizione
        binding.editTextReportDescription.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.newReportDescriptionError.setVisibility(View.INVISIBLE);
            }
            else{
                String text = binding.editTextReportDescription.getText().toString();
                String validationResult = Validation.checkEmptyField(text);
                if (validationResult.equals("ok"))
                    binding.newReportDescriptionError.setVisibility(View.INVISIBLE);
                else{
                    binding.newReportDescriptionError.setText(ErrorMapper.getInstance().getErrorMessage(validationResult));
                    binding.newReportDescriptionError.setVisibility(View.VISIBLE);
                }
            }
        });

        return alertDialog;
    }

    private void checkAndSetErrors(String resultBuildingsValidation, String resultCategoriesValidation, String resultValidationTitle, String resultValidationDescription) {
        if(!resultBuildingsValidation.equals("ok")){
            binding.newReportErrorSpinnerBuildings.setVisibility(View.VISIBLE);
            binding.newReportErrorSpinnerBuildings.setText(ErrorMapper.getInstance().getErrorMessage(resultBuildingsValidation));
        }
        if(!resultCategoriesValidation.equals("ok")){
            binding.newReportErrorSpinnerCategories.setVisibility(View.VISIBLE);
            binding.newReportErrorSpinnerCategories.setText(ErrorMapper.getInstance().getErrorMessage(resultCategoriesValidation));
        }
        if(!resultValidationTitle.equals("ok")) {
            binding.newReportTitleError.setVisibility(View.VISIBLE);
            binding.newReportTitleError.setText(ErrorMapper.getInstance().getErrorMessage(resultValidationTitle));
        }
        if(!resultValidationDescription.equals("ok")) {
            binding.newReportDescriptionError.setVisibility(View.VISIBLE);
            binding.newReportDescriptionError.setText(ErrorMapper.getInstance().getErrorMessage(resultValidationDescription));
        }
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