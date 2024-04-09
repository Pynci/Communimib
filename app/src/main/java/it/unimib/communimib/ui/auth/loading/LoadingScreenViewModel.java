package it.unimib.communimib.ui.auth.loading;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IUserRepository;

public class LoadingScreenViewModel extends ViewModel {

    private final MutableLiveData<Result> sessionResult;
    private final MutableLiveData<Result> emailCheckResult;
    private final MutableLiveData<Boolean> areAllDataAvaible;
    private final IUserRepository iUserRepository;

    public LoadingScreenViewModel(IUserRepository iUserRepository) {
        sessionResult = new MutableLiveData<>();
        emailCheckResult = new MutableLiveData<>();
        areAllDataAvaible = new MutableLiveData<>(false);
        this.iUserRepository = iUserRepository;
    }

    public void checkSession() {
        iUserRepository.isSessionStillActive(sessionResult::postValue);
    }

    public void checkEmailVerified() {
        iUserRepository.isEmailVerified(emailCheckResult::postValue);
    }

    public void setAreAllDataAvaible() throws InterruptedException {
        Thread.sleep(2000);
        areAllDataAvaible.setValue(true);
    }

    public LiveData<Result> getSessionResult() {
        return sessionResult;
    }

    public LiveData<Result> getEmailCheckResult() {
        return emailCheckResult;
    }

    public LiveData<Boolean> getAreAllDataAvaible() {
        return areAllDataAvaible;
    }
}
