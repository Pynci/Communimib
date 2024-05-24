package it.unimib.communimib.ui.main.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentProfileBinding;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.ui.main.dashboard.CategoriesRecyclerViewAdapter;
import it.unimib.communimib.ui.main.dashboard.DashboardFragmentDirections;
import it.unimib.communimib.ui.main.dashboard.DashboardRecyclerViewAdapter;
import it.unimib.communimib.ui.main.dashboard.OnPostClickListener;
import it.unimib.communimib.ui.main.dashboard.pictures.PostPicturesFragmentDialog;
import it.unimib.communimib.util.ErrorMapper;

public class ProfileFragment extends Fragment {

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
                ProfileFragmentDirections.ActionProfileFragmentToDetailedPostFragment action =
                        ProfileFragmentDirections.actionProfileFragmentToDetailedPostFragment(post);

                Navigation.findNavController(view).navigate(action);
            }

            @Override
            public void onImageSliderClick(List<String> pictures) {
                PostPicturesFragmentDialog imageDialog = new PostPicturesFragmentDialog(pictures);
                imageDialog.show(getParentFragmentManager(), "Image Dialog");
            }
        }, getContext());

        dashboardRecyclerViewAdapter.clearPostList();
        profileViewModel.cleanViewModel();

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

}