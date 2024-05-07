package it.unimib.communimib.ui.main.dashboard.newdashboardpost;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.Arrays;

import it.unimib.communimib.BottomNavigationBarListener;
import it.unimib.communimib.R;
import it.unimib.communimib.TopNavigationBarListener;
import it.unimib.communimib.databinding.FragmentNewDashboardPostDialogBinding;

public class NewDashboardPostDialog extends Fragment {

    private boolean isTitleOk;
    private boolean isDescriptionOk;
    private boolean isSpinnerOk;
    private FragmentNewDashboardPostDialogBinding binding;

    private BottomNavigationBarListener bottomListener;
    private TopNavigationBarListener topListener;
    public NewDashboardPostDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTitleOk = false;
        isDescriptionOk = false;
        isSpinnerOk = false;
        hideNavigationBars();
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

        //Gestore del caricamento delle imamgigni
        ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
                registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
                    if (!uris.isEmpty()) {
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
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
            showNavigationBars();
        });

        //Gestione del pulsante per caricare le foto
        binding.iamgeButtonAddImages.setOnClickListener(v -> pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
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

        binding.editTextPostDescription.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                String text = binding.editTextPostDescription.getText().toString();
                if(!text.isEmpty())
                    isDescriptionOk = true;

                tryEnableButton();
            }
        });

        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isSpinnerOk = true;
                tryEnableButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isSpinnerOk = false;
                tryEnableButton();
            }
        });
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
        adapter.addAll(Arrays.asList(categoriesArray));
        return adapter;
    }

}