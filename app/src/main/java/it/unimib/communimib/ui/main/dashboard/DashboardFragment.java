package it.unimib.communimib.ui.main.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.logging.ErrorManager;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentDashboardBinding;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.ui.main.reports.detailedreport.DashboardViewModelFactory;
import it.unimib.communimib.util.NavigationHelper;
import it.unimib.communimib.util.ErrorMapper;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    FragmentDashboardBinding fragmentDashboardBinding;
    private DashboardRecyclerViewAdapter dashboardRecyclerViewAdapter;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentDashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false);
        return fragmentDashboardBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        dashboardRecyclerViewAdapter = new DashboardRecyclerViewAdapter(
                new DashboardRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Post post) {

                    }
            }, getContext());

        fragmentDashboardBinding.fragmentDashboardRecyclerView.setLayoutManager(layoutManager);
        fragmentDashboardBinding.fragmentDashboardRecyclerView.setAdapter(dashboardRecyclerViewAdapter);

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

        dashboardViewModel.getPostEditedReadResult().observe(getViewLifecycleOwner(), result -> {
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

        dashboardViewModel.getPostCancelledReadResult().observe(getViewLifecycleOwner(), result ->
            Snackbar.make(requireView(),
                    ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                    BaseTransientBottomBar.LENGTH_SHORT).show()
        );

        dashboardViewModel.readAllPosts();
        fragmentDashboardBinding.buttonNewPost.setOnClickListener(v -> {
            NavigationHelper.navigateTo(
                    getActivity(),
                    v,
                    R.id.action_dashboardFragment_to_newDashboardPostDialog,
                    false);
        });
    }
}