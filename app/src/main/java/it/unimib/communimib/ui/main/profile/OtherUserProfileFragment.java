package it.unimib.communimib.ui.main.profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

import it.unimib.communimib.BottomNavigationBarListener;
import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentProfileBinding;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.ui.main.dashboard.CategoriesRecyclerViewAdapter;
import it.unimib.communimib.ui.main.dashboard.DashboardRecyclerViewAdapter;
import it.unimib.communimib.ui.main.dashboard.OnPostClickListener;
import it.unimib.communimib.ui.main.dashboard.pictures.PostPicturesFragmentDialog;
import it.unimib.communimib.ui.main.reports.ReportsHorizontalRecyclerViewAdapter;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.TopbarHelper;


public class OtherUserProfileFragment extends Fragment {

    User displayedUser;
    private FragmentProfileBinding binding;
    private OtherUserProfileViewModel otherUserProfileViewModel;
    private CategoriesRecyclerViewAdapter adapter;
    private DashboardRecyclerViewAdapter dashboardRecyclerViewAdapter;
    private ReportsHorizontalRecyclerViewAdapter reportsRecyclerViewAdapter;
    private boolean isScrollUpButtonVisible = false;
    private boolean isAnimating = false;
    private BottomNavigationBarListener mListener;

    public OtherUserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        otherUserProfileViewModel = new ViewModelProvider(this,
                new OtherUserProfileViewModelFactory()).get(OtherUserProfileViewModel.class);

        TopbarHelper.handleTopbar((AppCompatActivity) getActivity());
        hideBottomNavigationBar();
        try {
            OtherUserProfileFragmentArgs args = OtherUserProfileFragmentArgs.fromBundle(getArguments());
            this.displayedUser = args.getUser();
        }
        catch (Exception e) {
            displayedUser = null;
        }
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

        // disabilita i pulsanti di logout e modifica profilo
        binding.fragmentProfileImageButtonEditProfile.setVisibility(View.GONE);
        binding.fragmentProfileImageButtonLogout.setVisibility(View.GONE);
        initPropicCardComponents();


        //Gestione dei contenuti della schermata (recyler view)
        String[] options = getResources().getStringArray(R.array.other_user_profile_options);
        List<String> optionsList = Arrays.asList(options);

        RecyclerView.LayoutManager horizontalLayoutManager = new CustomLinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        adapter = new CategoriesRecyclerViewAdapter(optionsList, optionsList.get(0), category -> {
            adapter.setCurrentSelection(category);
            if (category.equals("Post pubblicati")){
                binding.profileRecyclerView.setAdapter(dashboardRecyclerViewAdapter);
                dashboardRecyclerViewAdapter.clearPostList();
                otherUserProfileViewModel.readPostsByUser(displayedUser.getUid());
            } else {
                binding.profileRecyclerView.setAdapter(reportsRecyclerViewAdapter);
                reportsRecyclerViewAdapter.clearReportList();
                otherUserProfileViewModel.readReportsByUser(displayedUser.getUid());
            }
        });

        binding.profileDoubleItemRecyclerView.setLayoutManager(horizontalLayoutManager);
        binding.profileDoubleItemRecyclerView.setAdapter(adapter);

        dashboardRecyclerViewAdapter = new DashboardRecyclerViewAdapter(new OnPostClickListener() {
            @Override
            public void onItemClick(Post post) {
                OtherUserProfileFragmentDirections.ActionOtherUserProfileFragmentToDetailedPostFragment action =
                        OtherUserProfileFragmentDirections.actionOtherUserProfileFragmentToDetailedPostFragment(post);

                Navigation.findNavController(view).navigate(action);
            }

            @Override
            public void onImageSliderClick(List<String> pictures) {
                PostPicturesFragmentDialog imageDialog = new PostPicturesFragmentDialog(pictures);
                imageDialog.show(getParentFragmentManager(), "Image Dialog");
            }

            @Override
            public void onProfileClick(User postAuthor) {
                // non deve fare niente
            }

        }, getContext());

        boolean isUnimibEmployee = displayedUser.isUnimibEmployee();
        reportsRecyclerViewAdapter = new ReportsHorizontalRecyclerViewAdapter(isUnimibEmployee, new ReportsHorizontalRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onCloseReportClick(Report report) {

            }

            @Override
            public void onCardClick(Report report) {

            }
        }, getContext(), R.layout.report_item);

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
        otherUserProfileViewModel.cleanViewModel();

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.profileRecyclerView.setLayoutManager(verticalLayoutManager);
        binding.profileRecyclerView.setAdapter(dashboardRecyclerViewAdapter);

        otherUserProfileViewModel.readPostsByUser(displayedUser.getUid());
        initPostsListeners();
        initReportsListeners();

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
    public void onDestroy() {
        super.onDestroy();
        showBottomNavigationBar();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BottomNavigationBarListener) {
            mListener = (BottomNavigationBarListener) context;
        } else {
            throw new RuntimeException(context + " must implement BottomNavigationBarListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void hideBottomNavigationBar() {
        if (mListener != null) {
            mListener.hideBottomNavigationBar();
        }
    }

    private void showBottomNavigationBar() {
        if (mListener != null) {
            mListener.showBottomNavigationBar();
        }
    }


    private void initPropicCardComponents() {
        if(!displayedUser.getName().isEmpty())
            binding.fragmentProfileTextViewName.setText(displayedUser.getName());

        if(!displayedUser.getSurname().isEmpty())
            binding.fragmentProfileTextViewSurname.setText(displayedUser.getSurname());

        if(displayedUser.getPropic() != null && !displayedUser.getPropic().isEmpty()){
            RequestOptions requestOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.user_filled)
                    .error(R.drawable.user_filled);
            Glide.with(this)
                    .load(Uri.parse(displayedUser.getPropic()))
                    .apply(requestOptions)
                    .into(binding.fragmentProfileImageViewProfileImage);
        }

        binding.fragmentProfileTextViewName.setFocusable(false);
        binding.fragmentProfileTextViewSurname.setFocusable(false);
        binding.fragmentProfileCardViewPropic.setClickable(false);
    }


    private void initReportsListeners() {
        otherUserProfileViewModel.getAddedReportResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Report report = ((Result.ReportSuccess) result).getReport();
                reportsRecyclerViewAdapter.addItem(report);
            } else {
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        otherUserProfileViewModel.getChangedReportResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Report report = ((Result.ReportSuccess) result).getReport();
                reportsRecyclerViewAdapter.editItem(report);
            } else {
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        otherUserProfileViewModel.getRemovedReportResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Report report = ((Result.ReportSuccess) result).getReport();
                reportsRecyclerViewAdapter.removeItem(report);
            } else {
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        otherUserProfileViewModel.getCancelledReportResult().observe(getViewLifecycleOwner(), result ->
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show());
    }

    private void initPostsListeners() {
        otherUserProfileViewModel.getAddedPostResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Post post = ((Result.PostSuccess) result).getPost();
                dashboardRecyclerViewAdapter.addItem(post);
            } else {
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        otherUserProfileViewModel.getChangedPostResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Post post = ((Result.PostSuccess) result).getPost();
                dashboardRecyclerViewAdapter.editItem(post);
            } else {
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        otherUserProfileViewModel.getRemovedPostResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Post post = ((Result.PostSuccess) result).getPost();
                dashboardRecyclerViewAdapter.removeItem(post);
            }
            else{
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        otherUserProfileViewModel.getCancelledPostResult().observe(getViewLifecycleOwner(), result ->
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show());
    }
}