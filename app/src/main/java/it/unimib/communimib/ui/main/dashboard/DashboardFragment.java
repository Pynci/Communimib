package it.unimib.communimib.ui.main.dashboard;

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
import android.widget.SearchView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentDashboardBinding;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.ui.main.dashboard.pictures.PostPicturesFragmentDialog;
import it.unimib.communimib.util.NavigationHelper;
import it.unimib.communimib.util.ErrorMapper;

public class DashboardFragment extends Fragment {

    private int numberNewPost = 0; //numero minimo di nuovi post per triggerare la comparsa del bottone
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private DashboardRecyclerViewAdapter dashboardRecyclerViewAdapter;
    private CategoriesRecyclerViewAdapter categoriesRecyclerViewAdapter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dashboardViewModel = new ViewModelProvider(this,
                new DashboardViewModelFactory())
                .get(DashboardViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] categories = getResources().getStringArray(R.array.posts_categories);
        categories = Arrays.copyOf(categories, categories.length-1);
        List<String> categoryList = Arrays.asList(categories);

        binding.fragmentDashboardSearchView.setOnClickListener(v -> binding.fragmentDashboardSearchView.setIconified(false));

        binding.fragmentDashboardSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dashboardRecyclerViewAdapter.clearPostList();
                dashboardViewModel.readPostsByTitleOrDescription(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        RecyclerView.LayoutManager categoryLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoriesRecyclerViewAdapter = new CategoriesRecyclerViewAdapter(categoryList, "Tutti", this::readPosts);

        binding.buttonNewPost.setOnClickListener(v -> {
            dashboardViewModel.cleanViewModel();
            NavigationHelper.navigateTo(
                    getActivity(),
                    v,
                    R.id.action_dashboardFragment_to_newDashboardPostDialog,
                    false);
        });

        binding.fragmentDashboardCategoriesRecyclerView.setLayoutManager(categoryLayoutManager);
        binding.fragmentDashboardCategoriesRecyclerView.setAdapter(categoriesRecyclerViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        dashboardRecyclerViewAdapter = new DashboardRecyclerViewAdapter(new OnPostClickListener() {
            @Override
            public void onItemClick(Post post) {
                DashboardFragmentDirections.ActionDashboardFragmentToDetailedPostFragment action =
                        DashboardFragmentDirections.actionDashboardFragmentToDetailedPostFragment(post);
                Navigation.findNavController(view).navigate(action);
            }

            @Override
            public void onImageSliderClick(List<String> pictures) {
                PostPicturesFragmentDialog imageDialog = new PostPicturesFragmentDialog(pictures);
                imageDialog.show(getParentFragmentManager(), "Image Dialog");
            }

            @Override
            public void onProfileClick(User postAuthor) {
                DashboardFragmentDirections.ActionDashboardFragmentToOtherUserProfileFragment action =
                        DashboardFragmentDirections.actionDashboardFragmentToOtherUserProfileFragment(postAuthor);
                Navigation.findNavController(view).navigate(action);
            }

        }, getContext());
        dashboardRecyclerViewAdapter.clearPostList();

        binding.fragmentDashboardRecyclerView.setLayoutManager(layoutManager);
        binding.fragmentDashboardRecyclerView.setAdapter(dashboardRecyclerViewAdapter);

        dashboardViewModel.cleanViewModel();
        readPosts(dashboardViewModel.getVisualizedCategory());

        dashboardViewModel.getPostAddedReadResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Post post = ((Result.PostSuccess) result).getPost();
                dashboardRecyclerViewAdapter.addItem(post);

                //((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition() >= 0 perché il primo elemento è contato -1
                //nessuno osi chiedere il perché
                numberNewPost++;
                if (numberNewPost >= 1 && layoutManager.findFirstVisibleItemPosition() >= 0) {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.button_slide_down);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            binding.floatingActionButtonScrollUp.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            numberNewPost = 0;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            //Non deve fare nulla
                        }
                    });
                    binding.floatingActionButtonScrollUp.startAnimation(animation);
                }

            } else {
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        dashboardViewModel.getPostChangedReadResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Post post = ((Result.PostSuccess) result).getPost();
                dashboardRecyclerViewAdapter.editItem(post);
            } else {
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        dashboardViewModel.getPostRemovedReadResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Post post = ((Result.PostSuccess) result).getPost();
                dashboardRecyclerViewAdapter.removeItem(post);
            } else {
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        dashboardViewModel.getReadCancelledResult().observe(getViewLifecycleOwner(), result ->
            Snackbar.make(requireView(),
                    ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                    BaseTransientBottomBar.LENGTH_SHORT).show()
        );


        //Gestione del bottone di scroll verso l'alto
        binding.floatingActionButtonScrollUp.setVisibility(View.GONE);
        binding.floatingActionButtonScrollUp.setOnClickListener(v -> {
            binding.fragmentDashboardRecyclerView.smoothScrollToPosition(0);
            binding.floatingActionButtonScrollUp.setVisibility(View.GONE);
        });

        //Gestione della chiusura della searchbar
        binding.fragmentDashboardSearchView.setOnCloseListener(() -> {
            readPosts(dashboardViewModel.getVisualizedCategory());
            return false;
        });

    }

    public void readPosts(String category){
        if(category.equals("Tutti")){
            categoriesRecyclerViewAdapter.setCurrentSelection(category);
            dashboardRecyclerViewAdapter.clearPostList();
            dashboardViewModel.setVisualizedCategory(category);
            dashboardViewModel.readAllPosts();
        } else {
            categoriesRecyclerViewAdapter.setCurrentSelection(category);
            dashboardRecyclerViewAdapter.clearPostList();
            dashboardViewModel.setVisualizedCategory(category);
            dashboardViewModel.readPostsByCategory(category);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dashboardViewModel.cleanViewModel();
    }
}