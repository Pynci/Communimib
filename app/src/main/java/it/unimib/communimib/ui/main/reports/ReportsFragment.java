package it.unimib.communimib.ui.main.reports;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentReportsBinding;
import it.unimib.communimib.model.Report;

public class ReportsFragment extends Fragment {

    private FragmentReportsBinding fragmentReportsBinding;
    private ReportsViewModel reportsViewModel;
    private List<Report> reportList;
    private ReportsRecyclerViewAdapter reportsRecyclerViewAdapter;

    public ReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportsViewModel = new ViewModelProvider(
                this,
                new ReportsViewModelFactory(this.getContext()))
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

        //Gestione osservazione creazione
        reportsViewModel.getCreateReportResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()) {
                Snackbar.make(view, "La segnalazione è stata creata con successo", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        reportList = new ArrayList<>();
        reportList.add(new Report("cesso rotto", "il bagno non va", "U14", "Guasto", "giu"));
        reportList.add(new Report("finestra rotto", "la finestra non si apre", "U7", "Guasto", "luca"));

        RecyclerView recyclerViewReports = fragmentReportsBinding.fragmentReportRecyclerView;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);

        reportsRecyclerViewAdapter = new ReportsRecyclerViewAdapter(reportList, getContext(), true, R.layout.fragment_reports, new ReportsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onCloseReportClick(Report report) {
                reportsViewModel.deleteReport(report);
            }
        });

        recyclerViewReports.setLayoutManager(layoutManager);
        recyclerViewReports.setAdapter(reportsRecyclerViewAdapter);
    }
}