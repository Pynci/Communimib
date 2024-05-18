package it.unimib.communimib.ui.main.reports.dialogs.reportcreation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.DialogCallback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.Token;
import it.unimib.communimib.repository.IReportRepository;
import it.unimib.communimib.repository.ITokenRepository;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.service.NotificationService;
import it.unimib.communimib.util.Validation;

public class ReportsCreationViewModel extends ViewModel {

    private final IReportRepository reportRepository;
    private final IUserRepository userRepository;
    private final ITokenRepository tokenRepository;
    private MutableLiveData<Result> createReportResult;
    private final MutableLiveData<Result> addedTokenResult;
    private final MutableLiveData<Result> changedTokenResult;
    private final MutableLiveData<Result> removedTokenResult;
    private final MutableLiveData<Result> cancelledTokenResult;

    public ReportsCreationViewModel(IReportRepository reportRepository, IUserRepository userRepository, ITokenRepository tokenRepository){
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;

        createReportResult = new MutableLiveData<>();
        addedTokenResult = new MutableLiveData<>();
        changedTokenResult = new MutableLiveData<>();
        removedTokenResult = new MutableLiveData<>();
        cancelledTokenResult= new MutableLiveData<>();
    }

    public void createReport(String title, String description, String building, String category, DialogCallback callback) {
        String validationResult = Validation.validateNewReport(title, description, building, category);
        if(validationResult.equals("ok")) {
            reportRepository.createReport(title, description, building, category, userRepository.getCurrentUser(), result -> {
                createReportResult.postValue(result);
                callback.onComplete();
            });
        }
    }

    public void getAllToken(){
        tokenRepository.getAllToken(
                addedToken -> addedTokenResult.setValue(addedToken),
                changedToken -> changedTokenResult.setValue(changedToken),
                removedToken -> removedTokenResult.setValue(removedToken),
                cancelledError -> cancelledTokenResult.setValue(cancelledError)
                );
    }

    public LiveData<Result> getCreateReportResult() {
        return this.createReportResult;
    }

    public LiveData<Result> getAddedTokenResult() {
        return addedTokenResult;
    }

    public LiveData<Result> getChangedTokenResult() {
        return changedTokenResult;
    }

    public LiveData<Result> getRemovedTokenResult() {
        return removedTokenResult;
    }

    public LiveData<Result> getCancelledTokenResult() {
        return cancelledTokenResult;
    }

    public void cleanViewModel(){
        createReportResult = new MutableLiveData<>();
    }
}
