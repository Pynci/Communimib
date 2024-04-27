package it.unimib.communimib.ui.main.reports.dialogs.reportcreation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.communimib.DialogCallback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IReportRepository;
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

    public void createReport(String titolo, String descrizione, String edificio, String categoria, DialogCallback callback) {
        String validationResult = Validation.validateNewReport(titolo, descrizione, edificio, categoria);
        if(validationResult.equals("ok")) {
            reportRepository.createReport(titolo, descrizione, edificio, categoria, userRepository.getCurrentUser(), result -> {
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
