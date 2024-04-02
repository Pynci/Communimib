package it.unimib.communimib.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IUserRepository;

public class EmailManagementViewModel extends ViewModel  {

    private final IUserRepository userRepository;
    MutableLiveData<Result> emailVerificationResult;
    MutableLiveData<Result> emailVerificationSendingResult;
    MutableLiveData<Result> passwordResetSendingResult;

    public EmailManagementViewModel(IUserRepository userRepository){
        this.userRepository = userRepository;
        emailVerificationResult = new MutableLiveData<>();
        emailVerificationSendingResult = new MutableLiveData<>();
        passwordResetSendingResult = new MutableLiveData<>();
    }

    public void sendEmailVerification(){
        userRepository.sendEmailVerification(result -> emailVerificationSendingResult.postValue(result));
    }

    public void startEmailPolling(){
        userRepository.startEmailPolling(result -> {
            stopEmailPolling();
            emailVerificationResult.postValue(result);
        });
    }

    public void stopEmailPolling(){
        userRepository.stopEmailPolling();
    }

    public LiveData<Result> getEmailVerificationResult() {
        return emailVerificationResult;
    }

    public LiveData<Result> getEmailVerificationSendingResult(){
        return emailVerificationSendingResult;
    }
}
