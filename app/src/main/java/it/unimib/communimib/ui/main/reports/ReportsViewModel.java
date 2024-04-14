package it.unimib.communimib.ui.main.reports;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IReportRepository;

public class ReportsViewModel extends ViewModel {

    private final MutableLiveData<Result> createReportResult;
    private IReportRepository reportRepository;

    public ReportsViewModel(IReportRepository reportRepository) {

        createReportResult = new MutableLiveData<>();
        this.reportRepository = reportRepository;
    }

    public void createReport(String titolo, String descrizione, String edificio, String categoria) {

    }
    public LiveData<Result> getCreateReportResult() {
        return this.createReportResult;
    }
}
