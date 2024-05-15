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
import android.widget.SearchView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentDashboardBinding;
import it.unimib.communimib.databinding.FragmentDashboardImageDialogBinding;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.ui.main.dashboard.dialogs.DashboardImageFragmentDialog;
import it.unimib.communimib.ui.main.reports.detailedreport.DashboardViewModelFactory;
import it.unimib.communimib.util.NavigationHelper;
import it.unimib.communimib.util.ErrorMapper;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    FragmentDashboardBinding fragmentDashboardBinding;
    private DashboardRecyclerViewAdapter dashboardRecyclerViewAdapter;
    private CategoriesRecyclerViewAdapter categoriesRecyclerViewAdapter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dashboardViewModel = new ViewModelProvider(this,
                new DashboardViewModelFactory(this.getContext()))
                .get(DashboardViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentDashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false);
        return fragmentDashboardBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] categories = getResources().getStringArray(R.array.posts_categories);
        categories = Arrays.copyOf(categories, categories.length-1);
        List<String> categoryList = Arrays.asList(categories);

        fragmentDashboardBinding.fragmentDashboardSearchView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fragmentDashboardBinding.fragmentDashboardSearchView.setIconified(false);
            }
        });

        fragmentDashboardBinding.fragmentDashboardSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        categoriesRecyclerViewAdapter = new CategoriesRecyclerViewAdapter(categoryList, this::readPosts);

        fragmentDashboardBinding.buttonNewPost.setOnClickListener(v -> {
            dashboardViewModel.cleanViewModel();
            NavigationHelper.navigateTo(
                    getActivity(),
                    v,
                    R.id.action_dashboardFragment_to_newDashboardPostDialog,
                    false);
        });

        fragmentDashboardBinding.fragmentDashboardCategoriesRecyclerView.setLayoutManager(categoryLayoutManager);
        fragmentDashboardBinding.fragmentDashboardCategoriesRecyclerView.setAdapter(categoriesRecyclerViewAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        dashboardRecyclerViewAdapter = new DashboardRecyclerViewAdapter(new DashboardRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post post) {

                DashboardFragmentDirections.ActionDashboardFragmentToDetailedPostFragment action =
                        DashboardFragmentDirections.actionDashboardFragmentToDetailedPostFragment(post);

                Navigation.findNavController(view).navigate(action);
            }

            @Override
            public void onImageSliderClick(Post post) {
                DashboardImageFragmentDialog imageDialog = new DashboardImageFragmentDialog(post);
                imageDialog.show(getParentFragmentManager(), "Image Dialog");
            }
        }, getContext());

        fragmentDashboardBinding.fragmentDashboardRecyclerView.setLayoutManager(layoutManager);
        fragmentDashboardBinding.fragmentDashboardRecyclerView.setAdapter(dashboardRecyclerViewAdapter);

        dashboardViewModel.cleanViewModel();
        readPosts(dashboardViewModel.getVisualizedCategory());

        dashboardViewModel.getPostAddedReadResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Post post = ((Result.PostSuccess) result).getPost();
                dashboardRecyclerViewAdapter.addItem(post);
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


    }

    public void readPosts(String category){
        if(category.equals("Tutti")){
            categoriesRecyclerViewAdapter.setCurrentCategory(category);
            dashboardRecyclerViewAdapter.clearPostList();
            dashboardViewModel.setVisualizedCategory(category);
            dashboardViewModel.readAllPosts();
        } else {
            categoriesRecyclerViewAdapter.setCurrentCategory(category);
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