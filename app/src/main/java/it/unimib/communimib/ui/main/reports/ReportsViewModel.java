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

    private final MutableLiveData<Result> deleteReportResult;
    private final MutableLiveData<Result> reportAddedReadResult;
    private final MutableLiveData<Result> reportChangedReadResult;
    private final MutableLiveData<Result> reportRemovedReadResult;
    private final MutableLiveData<Result> readCancelledResult;
    private final IReportRepository reportRepository;
    private final IUserRepository userRepository;

    public ReportsViewModel(IReportRepository reportRepository, IUserRepository userRepository) {
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

    public LiveData<Result> getDeleteReportResult() {return this.deleteReportResult;}
}
