package it.unimib.communimib.ui.main.reports;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.ErrorMapper;

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
        reportList = new ArrayList<>();

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

        reportsViewModel.getReportAddedReadResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Report report = ((Result.ReportSuccess) result).getReport();
                reportsRecyclerViewAdapter.addItem(report);
                Log.d("vaffanculo", "dentro");
            }
            else{
                Snackbar
                        .make(view, ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        });

        reportsViewModel.getReportChangedReadResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Report report = ((Result.ReportSuccess) result).getReport();
                reportsRecyclerViewAdapter.editItem(report);
            }
            else{
                Snackbar
                        .make(view, ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        });

        reportsViewModel.getReportRemovedReadResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Report report = ((Result.ReportSuccess) result).getReport();
                reportsRecyclerViewAdapter.removeItem(report);
            }
            else{
                Snackbar
                        .make(view, ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        });

        reportsViewModel.getReadCancelledResult().observe(getViewLifecycleOwner(), result -> {
            Snackbar
                    .make(view, ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT)
                    .show();
        });


//        reportList.add(new Report("cesso rotto", "il bagno non va", "U14", "Guasto",
//                new User("uid1", "giulia@unimib.it", "Giulia Raffaella Giulia", "Vitale")));
//        reportList.add(new Report("finestra rotto", "la finestra non si apre", "U7", "Guasto",
//                new User("uid2", "luca@unimib.it", "Luca", "Pincincincinciroli")));

        RecyclerView recyclerViewReports = fragmentReportsBinding.fragmentReportRecyclerView;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        reportsRecyclerViewAdapter = new ReportsRecyclerViewAdapter(
                true,
                R.layout.report_horizontal_item, report -> reportsViewModel.deleteReport(report));

        recyclerViewReports.setLayoutManager(layoutManager);
        recyclerViewReports.setAdapter(reportsRecyclerViewAdapter);

        reportsViewModel.readAllReports();
    }
}