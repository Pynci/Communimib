package it.unimib.communimib.ui.main.reports.dialogs.reportcreation;

import android.content.Context;

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
    private MutableLiveData<Result> createReportResult;

    public ReportsCreationViewModel(IReportRepository reportRepository, IUserRepository userRepository){
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;

        createReportResult = new MutableLiveData<>();
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

    public LiveData<Result> getCreateReportResult() {
        return this.createReportResult;
    }


    public void cleanViewModel(){
        createReportResult = new MutableLiveData<>();
    }
}
