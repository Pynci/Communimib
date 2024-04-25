package it.unimib.communimib.ui.main.reports;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.repository.IReportRepository;
import it.unimib.communimib.repository.IUserRepository;

public class ReportsViewModel extends ViewModel {

    private MutableLiveData<Result> deleteReportResult;
    private MutableLiveData<Result> reportAddedReadResult;
    private MutableLiveData<Result> reportChangedReadResult;
    private MutableLiveData<Result> reportRemovedReadResult;
    private MutableLiveData<Result> readCancelledResult;
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

    public User getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public void readAllReports(){
        reportRepository.readAllReports(
                result -> reportAddedReadResult.setValue(result),
                result -> reportChangedReadResult.setValue(result),
                result -> reportRemovedReadResult.setValue(result),
                result -> readCancelledResult.setValue(result)
        );
    }

    public void readReportsByBuildings(String[] buildings){
        reportRepository.readReportsByBuildings(buildings,
                result -> reportAddedReadResult.setValue(result),
                result -> reportChangedReadResult.setValue(result),
                result -> reportRemovedReadResult.setValue(result),
                result -> readCancelledResult.setValue(result));
    }

    public void readReportsByTitleAndDescription(String keyword){
        reportRepository.readReportsByTitleAndDescription(keyword,
                result -> reportAddedReadResult.setValue(result),
                result -> reportChangedReadResult.setValue(result),
                result -> reportRemovedReadResult.setValue(result),
                result -> readCancelledResult.setValue(result));
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

    public void cleanViewModel(){
        deleteReportResult = new MutableLiveData<>();
        reportAddedReadResult = new MutableLiveData<>();
        reportChangedReadResult = new MutableLiveData<>();
        reportRemovedReadResult = new MutableLiveData<>();
        readCancelledResult = new MutableLiveData<>();
    }
}
