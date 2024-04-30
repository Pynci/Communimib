package it.unimib.communimib.ui.auth.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.Validation;

public class SignupViewModel extends ViewModel {

    private MutableLiveData<Result> signUpResult;
    private final IUserRepository iUserRepository;

    public SignupViewModel(IUserRepository iUserRepository){
        signUpResult = new MutableLiveData<>();
        this.iUserRepository = iUserRepository;
    }

    public void signUp(String email, String password, String name, String surname) {
        iUserRepository.signUp(email, password, name, surname, signUpResult::postValue);
    }

    public LiveData<Result> getSignUpResult() {
        return signUpResult;
    }

    public void cleanViewModel(){
        signUpResult = new MutableLiveData<>();
    }

}
