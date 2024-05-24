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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentProfileBinding;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.ui.main.dashboard.CategoriesRecyclerViewAdapter;
import it.unimib.communimib.ui.main.dashboard.DashboardRecyclerViewAdapter;
import it.unimib.communimib.ui.main.dashboard.OnPostClickListener;
import it.unimib.communimib.ui.main.dashboard.pictures.PostPicturesFragmentDialog;
import it.unimib.communimib.ui.main.reports.ReportsHorizontalRecyclerViewAdapter;
import it.unimib.communimib.util.ErrorMapper;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private CategoriesRecyclerViewAdapter adapter;
    private DashboardRecyclerViewAdapter dashboardRecyclerViewAdapter;
    private ReportsHorizontalRecyclerViewAdapter reportsRecyclerViewAdapter;
    private ProfileViewModel profileViewModel;
    private boolean isScrollUpButtonVisible = false;
    private boolean isAnimating = false;

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
                    binding.profileRecyclerView.setAdapter(reportsRecyclerViewAdapter);
                    reportsRecyclerViewAdapter.clearReportList();
                    profileViewModel.readReportsByUser();
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

        boolean isUnimibEmployee = profileViewModel.getCurrentUser().isUnimibEmployee();
        reportsRecyclerViewAdapter = new ReportsHorizontalRecyclerViewAdapter(isUnimibEmployee, new ReportsHorizontalRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onCloseReportClick(Report report) {

            }

            @Override
            public void onCardClick(Report report) {

            }
        },
                getContext(), R.layout.report_item);

        Animation animationSlideLeft = AnimationUtils.loadAnimation(getContext(), R.anim.button_slide_left);
        animationSlideLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.profileScrollUpButton.setVisibility(View.VISIBLE);
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isScrollUpButtonVisible = true;
                isAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //non fa nulla
            }
        });

        Animation animationSlideRight = AnimationUtils.loadAnimation(getContext(), R.anim.button_slide_right);
        animationSlideRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.profileScrollUpButton.setVisibility(View.GONE);
                isAnimating = false;
                isScrollUpButtonVisible = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //non fa nulla
            }
        });

        dashboardRecyclerViewAdapter.clearPostList();
        profileViewModel.cleanViewModel();

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

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

        profileViewModel.getAddedReportResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Report report = ((Result.ReportSuccess) result).getReport();
                reportsRecyclerViewAdapter.addItem(report);
            } else {
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        profileViewModel.getChangedReportResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Report report = ((Result.ReportSuccess) result).getReport();
                reportsRecyclerViewAdapter.editItem(report);
            } else {
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        profileViewModel.getRemovedReportResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Report report = ((Result.ReportSuccess) result).getReport();
                reportsRecyclerViewAdapter.removeItem(report);
            } else {
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        profileViewModel.getCancelledReportResult().observe(getViewLifecycleOwner(), result -> {
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
        });

        binding.profileScrollUpButton.setVisibility(View.GONE);
        binding.profileScrollUpButton.setOnClickListener(r -> {
            binding.profileNestedScrollView.smoothScrollTo(0,1, 800);
            if(isScrollUpButtonVisible && !isAnimating)
            binding.profileScrollUpButton.startAnimation(animationSlideRight);
        });

        binding.profileNestedScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int firstItemVisible = binding.profileNestedScrollView.getScrollY();
            if(firstItemVisible > 400 && !isScrollUpButtonVisible && !isAnimating){
                binding.profileScrollUpButton.startAnimation(animationSlideLeft);
            } else if(firstItemVisible < 400 && isScrollUpButtonVisible && !isAnimating){
                binding.profileScrollUpButton.startAnimation(animationSlideRight);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        profileViewModel.cleanViewModel();
    }
}