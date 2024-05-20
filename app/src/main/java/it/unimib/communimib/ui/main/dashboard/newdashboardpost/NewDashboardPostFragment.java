package it.unimib.communimib.ui.main.dashboard.newdashboardpost;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.unimib.communimib.BottomNavigationBarListener;
import it.unimib.communimib.R;
import it.unimib.communimib.TopNavigationBarListener;
import it.unimib.communimib.databinding.FragmentNewDashboardPostDialogBinding;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.ErrorMapper;

public class NewDashboardPostFragment extends Fragment {

    private boolean isTitleOk;
    private boolean isDescriptionOk;
    private boolean isSpinnerOk;
    private List<String> selectedUris;
    private FragmentNewDashboardPostDialogBinding binding;

    private BottomNavigationBarListener bottomListener;
    private TopNavigationBarListener topListener;

    private NewDashboardPostViewModel newDashboardPostViewModel;


    public NewDashboardPostFragment() {
        selectedUris = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTitleOk = false;
        isDescriptionOk = false;
        isSpinnerOk = false;
        hideNavigationBars();

        newDashboardPostViewModel =
                new ViewModelProvider(this, new NewDashboardPostViewModelFactory(this.getContext()))
                        .get(NewDashboardPostViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewDashboardPostDialogBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cardViewImageSlider.setVisibility(View.GONE);

        //Gestore del caricamento delle immagini
        ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
                registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
                    if (!uris.isEmpty()) {
                        for (Uri uri : uris) {
                            selectedUris.add(uri.toString());
                        }
                        binding.cardViewImageSlider.setVisibility(View.VISIBLE);

                        ArrayList<SlideModel> slideModels = new ArrayList<>();

                        for (Uri uri : uris) {
                            slideModels.add(new SlideModel(uri.toString(), ScaleTypes.FIT));
                        }

                        binding.imageSliderLoadedImages.setImageList(slideModels);
                    }
                });

        //Gestione del pulsante indietro
        binding.buttonBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        //Gestione del pulsante per caricare le foto
        binding.imageButtonAddImages.setOnClickListener(v -> pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                .build()));

        //Gestione spinner categorie
        binding.categorySpinner.setPrompt("Categoria");
        ArrayAdapter<String> adapterCategorie = getCategoriesAdapter();
        binding.categorySpinner.setAdapter(adapterCategorie);
        binding.categorySpinner.setSelection(adapterCategorie.getCount());

        //Gestione titolo
        binding.editTextPostTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                String text = binding.editTextPostTitle.getText().toString();
                if(!text.isEmpty())
                    isTitleOk = true;

                tryEnableButton();
            }
        });

        //Gestione descrizione
        binding.editTextPostDescription.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                String text = binding.editTextPostDescription.getText().toString();
                if(!text.isEmpty())
                    isDescriptionOk = true;

                tryEnableButton();
            }
        });

        //Gestione spinner per attivazione bottone di conferma e creazione post
        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!binding.categorySpinner.getSelectedItem().equals("Categoria")){
                    isSpinnerOk = true;
                }else{
                    isSpinnerOk = false;
                }
                tryEnableButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isSpinnerOk = false;
                tryEnableButton();
            }
        });

        //Gestione del pulsante di conferma
        binding.buttonConfirm.setOnClickListener(v -> {
            if(binding.buttonConfirm.isEnabled()){
                newDashboardPostViewModel.createPost(
                        binding.editTextPostTitle.getText().toString(),
                        binding.editTextPostDescription.getText().toString(),
                        binding.categorySpinner.getSelectedItem().toString(),
                        newDashboardPostViewModel.getCurrentUser(),
                        binding.editTextEmailAddress.getText().toString(),
                        binding.editTextWebsite.getText().toString(),
                        selectedUris
                );
            }
        });

        //Osservazione del risultato di creazione
        newDashboardPostViewModel.getPostCreationResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
                showNavigationBars();
            }
            else{
                Snackbar.make(view, ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        //Gestione del tocco fuori dai campi per rimuovere il focus
        binding.scrollview.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View currentFocus = getActivity().getCurrentFocus();
                if (currentFocus != null) {
                    currentFocus.clearFocus();
                    hideKeyboard(v);
                }
            }
            return true;
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void tryEnableButton() {
        binding.buttonConfirm.setEnabled(isTitleOk && isDescriptionOk && isSpinnerOk);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showNavigationBars();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BottomNavigationBarListener && context instanceof TopNavigationBarListener) {
            bottomListener = (BottomNavigationBarListener) context;
            topListener = (TopNavigationBarListener) context;
        } else {
            throw new RuntimeException(context + " must implement BottomNavigationBarListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bottomListener = null;
        topListener = null;
    }

    private void hideNavigationBars() {
        if (bottomListener != null) {
            bottomListener.hideBottomNavigationBar();
        }

        if(topListener != null) {
            topListener.hideTopNavigationBar();
        }
    }

    private void showNavigationBars() {
        if (bottomListener != null) {
            bottomListener.showBottomNavigationBar();
        }

        if(topListener != null) {
            topListener.showTopNavigationBar();
        }
    }

    @NonNull
    private ArrayAdapter<String> getCategoriesAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

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

        String[] categoriesArray = getResources().getStringArray(R.array.posts_categories);
        categoriesArray = Arrays.copyOfRange(categoriesArray, 1, categoriesArray.length);
        adapter.addAll(Arrays.asList(categoriesArray));
        return adapter;
    }

}