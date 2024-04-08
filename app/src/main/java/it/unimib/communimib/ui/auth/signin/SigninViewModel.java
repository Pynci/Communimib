package it.unimib.communimib.ui.auth.signin;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.Validation;

public class SigninViewModel extends ViewModel {

    private final IUserRepository userRepository;
    private MutableLiveData<Result> signInResult;
    private MutableLiveData<Result> emailVerifiedResult;

    public SigninViewModel(IUserRepository userRepository){
        this.userRepository = userRepository;
        this.signInResult = new MutableLiveData<>();
        this.emailVerifiedResult = new MutableLiveData<>();
    }

    public void signIn (String email, String password){
        if(Validation.checkEmail(email).equals("ok") && !password.isEmpty()){
            userRepository.signIn(email, password, signInResult::postValue);
        } else {
            signInResult.setValue(new Result.Error(ErrorMapper.NOT_ACCEPTED_PARAMETERS));
        }
    }
    public LiveData<Result> getSignInResult(){
        return signInResult;
    }

    public void isEmailVerified(){
        userRepository.isEmailVerified(emailVerifiedResult::postValue);
    }
    public LiveData<Result> getEmailVerifiedResult(){
        return emailVerifiedResult;
    }

    public void cleanViewModel(){
        signInResult = new MutableLiveData<>();
        emailVerifiedResult = new MutableLiveData<>();
    }
}
