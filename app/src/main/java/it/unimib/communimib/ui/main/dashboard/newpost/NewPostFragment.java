package it.unimib.communimib.ui.main.dashboard.newpost;

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
import it.unimib.communimib.databinding.FragmentNewPostBinding;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.Validation;

public class NewPostFragment extends Fragment {

    private boolean isTitleOk;
    private boolean isDescriptionOk;
    private boolean isSpinnerOk;
    private boolean isEmailOk;
    private boolean isLinkOk;
    private List<String> selectedUris;
    private FragmentNewPostBinding binding;

    private BottomNavigationBarListener bottomListener;
    private TopNavigationBarListener topListener;

    private NewPostViewModel newPostViewModel;


    public NewPostFragment() {
        selectedUris = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTitleOk = false;
        isDescriptionOk = false;
        isSpinnerOk = false;
        isEmailOk = true;
        isLinkOk = true;
        hideNavigationBars();

        newPostViewModel =
                new ViewModelProvider(this, new NewPostViewModelFactory(this.getContext()))
                        .get(NewPostViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewPostBinding.inflate(getLayoutInflater());
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
                            slideModels.add(new SlideModel(uri.toString(), ScaleTypes.CENTER_CROP));
                        }

                        binding.imageSliderLoadedImages.setImageList(slideModels, ScaleTypes.CENTER_CROP);
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
                getView().clearFocus();
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
                newPostViewModel.createPost(
                        binding.editTextPostTitle.getText().toString(),
                        binding.editTextPostDescription.getText().toString(),
                        binding.categorySpinner.getSelectedItem().toString(),
                        newPostViewModel.getCurrentUser(),
                        binding.editTextEmailAddress.getText().toString(),
                        binding.editTextWebsite.getText().toString(),
                        selectedUris
                );
            }
        });

        //Osservazione del risultato di creazione
        newPostViewModel.getPostCreationResult().observe(getViewLifecycleOwner(), result -> {
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
        binding.mainLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View currentFocus = getActivity().getCurrentFocus();
                if (currentFocus != null) {
                    currentFocus.clearFocus();
                    hideKeyboard(v);
                }
            }
            return true;
        });

        //Validazione della mail
        binding.textViewEmailError.setVisibility(View.INVISIBLE);
        binding.editTextEmailAddress.setOnFocusChangeListener((v, hasFocus) -> {
            String email = binding.editTextEmailAddress.getText().toString();
            if(hasFocus){
                binding.textViewEmailError.setVisibility(View.INVISIBLE);
                isEmailOk = false;
                tryEnableButton();
            }
            else{
                if(email.isEmpty() || Validation.isValidEmail(email)){
                    isEmailOk = true;
                }
                else{
                    binding.textViewEmailError.setVisibility(View.VISIBLE);
                    isEmailOk = false;
                }
                tryEnableButton();
            }
        });

        //Validazione del link
        binding.textViewLinkError.setVisibility(View.INVISIBLE);
        binding.editTextWebsite.setOnFocusChangeListener((v, hasFocus) -> {
            String link = binding.editTextWebsite.getText().toString();
            if(hasFocus){
                binding.textViewLinkError.setVisibility(View.INVISIBLE);
                isLinkOk = false;
                tryEnableButton();
            }
            else{
                if(link.isEmpty() || Validation.isValidLink(link)){
                    isLinkOk = true;
                }
                else{
                    binding.textViewLinkError.setVisibility(View.VISIBLE);
                    isLinkOk = false;
                }
                tryEnableButton();
            }
        });

    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void tryEnableButton() {
        binding.buttonConfirm.setEnabled(isTitleOk && isDescriptionOk && isSpinnerOk && isEmailOk && isLinkOk);
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