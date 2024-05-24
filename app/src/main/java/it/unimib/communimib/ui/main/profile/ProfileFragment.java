package it.unimib.communimib.ui.main.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentProfileBinding;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.ui.main.dashboard.CategoriesRecyclerViewAdapter;
import it.unimib.communimib.ui.main.dashboard.DashboardRecyclerViewAdapter;
import it.unimib.communimib.ui.main.dashboard.OnPostClickListener;
import it.unimib.communimib.util.ErrorMapper;

public class ProfileFragment extends Fragment {

    private boolean isInEditMode = false;
    private Uri selectedImage;
    private FragmentProfileBinding binding;
    private CategoriesRecyclerViewAdapter adapter;
    private DashboardRecyclerViewAdapter dashboardRecyclerViewAdapter;
    private ProfileViewModel profileViewModel;

    public ProfileFragment() {
        //Costruttore volutamente vuoto
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileViewModel = new ViewModelProvider(this,
                new ProfileViewModelFactory(getContext()))
                .get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Gestione del pulsante di modifica del profilo
        binding.fragmentProfileImageButtonEditProfile.setOnClickListener(v -> {
            onImageButtonClickManagement();
        });

        //Gestione del recupero dell'immagine editata
        ActivityResultLauncher<Intent> cropImageLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::cropImageResultManagement);

        //Gestione recupero immagine selezionata
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), imageToEdit -> {
                    mediaPickResultManagement(imageToEdit, cropImageLauncher);
                });

        //Gestione del click sull'immagine per caricare una nuova foto
        binding.fragmentProfileCardViewPropic.setOnClickListener(v -> {
            if(isInEditMode)
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
        });


        //Gestione dei contenuti della schermata (recyler view)
        String[] options = getResources().getStringArray(R.array.profile_options);
        List<String> optionsList = Arrays.asList(options);

        RecyclerView.LayoutManager horizontalLayoutManager = new CustomLinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        adapter = new CategoriesRecyclerViewAdapter(optionsList, optionsList.get(0),new CategoriesRecyclerViewAdapter.OnCategoryClickListener() {
            @Override
            public void onItemClick(String category) {
                adapter.setCurrentSelection(category);
                if (category.equals("I miei post")){
                    binding.profileRecyclerView.setAdapter(dashboardRecyclerViewAdapter);
                    dashboardRecyclerViewAdapter.clearPostList();
                    profileViewModel.readPostsByUser();
                } else {
                    //settare adapter segnalazioni
                }
            }
        });

        binding.profileDoubleItemRecyclerView.setLayoutManager(horizontalLayoutManager);
        binding.profileDoubleItemRecyclerView.setAdapter(adapter);

        dashboardRecyclerViewAdapter = new DashboardRecyclerViewAdapter(new OnPostClickListener() {
            @Override
            public void onItemClick(Post post) {

            }

            @Override
            public void onImageSliderClick(List<String> pictures) {

            }
        }, getContext());

        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        binding.profileRecyclerView.setLayoutManager(verticalLayoutManager);

        binding.profileRecyclerView.setAdapter(dashboardRecyclerViewAdapter);

        profileViewModel.readPostsByUser();

        profileViewModel.getAddedPostResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Post post = ((Result.PostSuccess) result).getPost();
                dashboardRecyclerViewAdapter.addItem(post);
            } else {
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        profileViewModel.getChangedPostResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Post post = ((Result.PostSuccess) result).getPost();
                dashboardRecyclerViewAdapter.editItem(post);
            } else {
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        profileViewModel.getRemovedPostResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Post post = ((Result.PostSuccess) result).getPost();
                dashboardRecyclerViewAdapter.removeItem(post);
            } else {
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        profileViewModel.getCancelledPostResult().observe(getViewLifecycleOwner(), result -> {
            Snackbar.make(requireView(),
                    ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                    BaseTransientBottomBar.LENGTH_SHORT).show();
        });
    }

    private void mediaPickResultManagement(Uri imageToEdit, ActivityResultLauncher<Intent> cropImageLauncher) {
        if (imageToEdit != null) {

            Uri destinationUri = Uri.fromFile(new File(requireContext().getCacheDir(), "IMG_" + System.currentTimeMillis()));

            UCrop.Options options = new UCrop.Options();
            options.setCircleDimmedLayer(true); // Abilita il ritaglio circolare
            options.setShowCropFrame(false); // Nasconde il rettangolo di ritaglio
            options.setShowCropGrid(false); // Nasconde la griglia di ritaglio

            Intent cropIntent = UCrop.of(imageToEdit, destinationUri)
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(500, 500)
                    .withOptions(options)
                    .getIntent(requireContext());

            cropImageLauncher.launch(cropIntent);
        } else {
            Log.d("Pizza", "No media selected");
        }
    }
    private void cropImageResultManagement(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            final Uri resultUri = UCrop.getOutput(result.getData());
            if (resultUri != null) {
                loadImageIntoImageView(resultUri);
            }
        } else if (result.getResultCode() == UCrop.RESULT_ERROR && result.getData() != null) {
            final Throwable cropError = UCrop.getError(result.getData());
            throw new UCropException(cropError);
        }
    }

    private void loadImageIntoImageView(Uri resultUri) {
        selectedImage = resultUri;
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.user_filled)
                .error(R.drawable.user_filled);

        // Caricamento dell'immagine con Glide
        Glide.with(this)
                .load(resultUri)
                .apply(requestOptions)
                .into(binding.fragmentProfileImageViewProfileImage);
    }

    private void onImageButtonClickManagement() {
        isInEditMode = !isInEditMode;
        propicCardsComponentsManagement(isInEditMode);

        if(isInEditMode) 
            binding.fragmentProfileImageButtonEditProfile.setImageResource(R.drawable.confirm_edits);
        else
            binding.fragmentProfileImageButtonEditProfile.setImageResource(R.drawable.pencil_edit);
    }

    private void propicCardsComponentsManagement(boolean mode) {
        //Modifico il nome
        binding.fragmentProfileTextViewName.setEnabled(mode);

        //Modifico il cognome
        binding.fragmentProfileTextViewSurname.setEnabled(mode);

        //Modifico l'immagine
        binding.fragmentProfileCardViewPropic.setClickable(mode);
    }

}