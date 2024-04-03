package it.unimib.communimib.ui.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.apache.commons.validator.routines.EmailValidator;

import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.util.ErrorMapper;

public class SigninViewModel extends ViewModel {

    private final IUserRepository userRepository;
    MutableLiveData<Result> signInResult;
    MutableLiveData<Result> emailVerifiedResult;

    public SigninViewModel(IUserRepository userRepository){
        this.userRepository = userRepository;
        this.signInResult = new MutableLiveData<>();
        this.emailVerifiedResult = new MutableLiveData<>();
    }

    public void signIn (String email, String password){
        if(checkEmail(email).equals("ok") && !password.isEmpty()){
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

    public String checkEmail(String email){
        if(email.isEmpty())
            return ErrorMapper.EMPTY_FIELD;
        if(!EmailValidator.getInstance().isValid(email))
            return ErrorMapper.INVALID_FIELD;
        if (!(email.substring(email.indexOf('@'))).equals("@campus.unimib.it") && !(email.substring(email.indexOf('@'))).equals("@unimib.it"))
            return ErrorMapper.NOT_UNIVERSITY_EMAIL;

        return "ok";
    }

    public void cleanViewModel(){
        signInResult = new MutableLiveData<>();
        emailVerifiedResult = new MutableLiveData<>();
    }
}
