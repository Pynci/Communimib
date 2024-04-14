package it.unimib.communimib.ui.main.reports;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import it.unimib.communimib.databinding.FragmentReportsBinding;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.ErrorMapper;

public class ReportsFragment extends Fragment {

    private FragmentReportsBinding fragmentReportsBinding;
    private ReportsViewModel reportsViewModel;

    public ReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportsViewModel = new ViewModelProvider(
                this,
                new ReportsViewModelFactory())
                .get(ReportsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentReportsBinding = FragmentReportsBinding.inflate(inflater, container, false);
        return fragmentReportsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentReportsBinding.addNewReportButton.setOnClickListener(v -> {
            NewReportFragmentDialog dialog = new NewReportFragmentDialog(reportsViewModel);
            dialog.show(getParentFragmentManager(), "New Report Fragment Dialog");
        });
    }
}