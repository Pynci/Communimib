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
    private MutableLiveData<Result> addedTokenResult;
    private MutableLiveData<Result> changedTokenResult;
    private MutableLiveData<Result> removedTokenResult;
    private MutableLiveData<Result> cancelledTokenResult;
    private List<Token> tokenList;

    public ReportsCreationViewModel(IReportRepository reportRepository, IUserRepository userRepository, ITokenRepository tokenRepository){
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;

        createReportResult = new MutableLiveData<>();
        addedTokenResult = new MutableLiveData<>();
        changedTokenResult = new MutableLiveData<>();
        removedTokenResult = new MutableLiveData<>();
        cancelledTokenResult= new MutableLiveData<>();
        tokenList = new ArrayList<>();
    }

    public void createReport(String title, String description, String building, String category, DialogCallback callback) {
        String validationResult = Validation.validateNewReport(title, description, building, category);
        if(validationResult.equals("ok")) {
            reportRepository.createReport(title, description, building, category, userRepository.getCurrentUser(), result -> {
                createReportResult.postValue(result);
                if(result.isSuccessful()){
                    String messageBody = title+"    "+building;
                    NotificationService.sendNotification(messageBody, tokenList, userRepository.getCurrentUser());
                }
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

    public List<Token> getTokenList() {
        return tokenList;
    }

    public void setTokenList(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public void cleanViewModel(){
        createReportResult = new MutableLiveData<>();
        addedTokenResult = new MutableLiveData<>();
        changedTokenResult = new MutableLiveData<>();
        removedTokenResult = new MutableLiveData<>();
        cancelledTokenResult = new MutableLiveData<>();
    }
}
