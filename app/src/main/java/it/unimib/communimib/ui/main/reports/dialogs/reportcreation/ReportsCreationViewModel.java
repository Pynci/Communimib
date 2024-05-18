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
    private MutableLiveData<Result> tokenResult;
    private final List<Token> tokenList;

    public ReportsCreationViewModel(IReportRepository reportRepository, IUserRepository userRepository, ITokenRepository tokenRepository){
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;

        createReportResult = new MutableLiveData<>();
        tokenResult = new MutableLiveData<>();
        tokenList = new ArrayList<>();
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
                addedToken -> {
                    tokenList.add(((Result.TokenSuccess) addedToken).getToken());
                },
                changedToken -> {
                    Token newToken = ((Result.TokenSuccess) changedToken).getToken();
                    Token oldToken = findOldToken(newToken);
                    int index = tokenList.indexOf(oldToken);
                    tokenList.set(index, newToken);
                },
                removedToken -> {
                    tokenList.remove(((Result.TokenSuccess) removedToken).getToken());
                },
                cancelledError -> {
                    //todo implementare questo
                }
                );
    }

    public Token findOldToken(Token newToken){
        for (Token token: tokenList) {
            if(token.getTid().equals(newToken.getTid())){
                return token;
            }
        }
        return newToken;
    }

    public LiveData<Result> getCreateReportResult() {
        return this.createReportResult;
    }

    public void cleanViewModel(){
        createReportResult = new MutableLiveData<>();
    }
}
