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
import it.unimib.communimib.ui.main.dashboard.CategoriesRecyclerViewAdapter;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private CategoriesRecyclerViewAdapter adapter;

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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        adapter = new CategoriesRecyclerViewAdapter(optionsList, optionsList.get(0),new CategoriesRecyclerViewAdapter.OnCategoryClickListener() {
            @Override
            public void onItemClick(String category) {
                adapter.setCurrentSelection(category);
            }
        });

        binding.profileDoubleItemRecyclerView.setLayoutManager(layoutManager);
        binding.profileDoubleItemRecyclerView.setAdapter(adapter);

    }

}