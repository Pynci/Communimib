package it.unimib.communimib.ui.main.reports.detailedreport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.repository.IReportRepository;
import it.unimib.communimib.repository.IUserRepository;

public class DetailedReportViewModel extends ViewModel {

    private final MutableLiveData<User> currentUserResult;
    private final MutableLiveData<Result> closeReportResult;
    private final IUserRepository iUserRepository;
    private final IReportRepository iReportRepository;

    public DetailedReportViewModel (IUserRepository iUserRepository, IReportRepository iReportRepository) {

        currentUserResult = new MutableLiveData<>();
        closeReportResult = new MutableLiveData<>();

        this.iReportRepository = iReportRepository;
        this.iUserRepository = iUserRepository;
    }

    protected void getCurrentUser() {
        currentUserResult.setValue(iUserRepository.getCurrentUser());
    }

    protected void closeReport(Report report) {
        iReportRepository.deleteReport(report, closeReportResult::postValue);
    }

    protected LiveData<User> getCurrentUserResult() {
        return currentUserResult;
    }

    protected LiveData<Result> getCloseReportResult() {
        return closeReportResult;
    }
}
