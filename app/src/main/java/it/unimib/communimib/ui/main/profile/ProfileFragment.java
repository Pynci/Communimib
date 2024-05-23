package it.unimib.communimib.ui.main.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentProfileBinding;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.ui.main.dashboard.CategoriesRecyclerViewAdapter;
import it.unimib.communimib.ui.main.dashboard.DashboardRecyclerViewAdapter;
import it.unimib.communimib.ui.main.dashboard.OnPostClickListener;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private CategoriesRecyclerViewAdapter adapter;

    private DashboardRecyclerViewAdapter dashboardRecyclerViewAdapter;

    public ProfileFragment() {
        //Costruttore volutamente vuoto
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        RecyclerView.LayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        adapter = new CategoriesRecyclerViewAdapter(optionsList, optionsList.get(0),new CategoriesRecyclerViewAdapter.OnCategoryClickListener() {
            @Override
            public void onItemClick(String category) {
                adapter.setCurrentSelection(category);
                if (category.equals("I miei post")){
                    binding.profileRecyclerView.setAdapter(dashboardRecyclerViewAdapter);
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



    }

}