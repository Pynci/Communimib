package it.unimib.communimib.ui.main.reports;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.communimib.Callback;
import it.unimib.communimib.DialogCallback;
import it.unimib.communimib.R;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IReportRepository;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.util.Validation;

public class ReportsViewModel extends ViewModel {

    private final MutableLiveData<Result> createReportResult;
    private final MutableLiveData<Result> deleteReportResult;
    private final MutableLiveData<Result> reportAddedReadResult;
    private final MutableLiveData<Result> reportChangedReadResult;
    private final MutableLiveData<Result> reportRemovedReadResult;
    private final MutableLiveData<Result> readCancelledResult;
    private final IReportRepository reportRepository;
    private final IUserRepository userRepository;

    public ReportsViewModel(IReportRepository reportRepository, IUserRepository userRepository) {
        createReportResult = new MutableLiveData<>();
        deleteReportResult = new MutableLiveData<>();
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;

        reportAddedReadResult = new MutableLiveData<>();
        reportChangedReadResult = new MutableLiveData<>();
        reportRemovedReadResult = new MutableLiveData<>();
        readCancelledResult = new MutableLiveData<>();
    }

    public void readAllReports(){
        reportRepository.readAllReports(
                reportAddedReadResult :: postValue,
                reportChangedReadResult :: postValue,
                reportRemovedReadResult :: postValue,
                readCancelledResult :: postValue);
    }

    public void createReport(String titolo, String descrizione, String edificio, String categoria, DialogCallback callback) {
        String validationResult = Validation.validateNewReport(titolo, descrizione, edificio, categoria);
        if(validationResult.equals("ok")) {
            reportRepository.createReport(titolo, descrizione, edificio, categoria, userRepository.getCurrentUser(), result -> {
                createReportResult.postValue(result);
                callback.onComplete();
            });
        }
    }

    public void deleteReport(Report report){
        reportRepository.deleteReport(report, deleteReportResult::postValue);
    }

    public LiveData<Result> getReportAddedReadResult() {
        return reportAddedReadResult;
    }

    public LiveData<Result> getReportChangedReadResult() {
        return reportChangedReadResult;
    }

    public LiveData<Result> getReportRemovedReadResult() {
        return reportRemovedReadResult;
    }

    public LiveData<Result> getReadCancelledResult() {
        return readCancelledResult;
    }

    public LiveData<Result> getCreateReportResult() {
        return this.createReportResult;
    }

    public LiveData<Result> getDeleteReportResult() {return this.deleteReportResult;}
}
