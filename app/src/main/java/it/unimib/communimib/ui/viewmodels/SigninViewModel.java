package it.unimib.communimib.ui.viewmodels;

import static it.unimib.communimib.util.ErrorMapper.EMPTY_FIELD;
import static it.unimib.communimib.util.ErrorMapper.INVALID_FIELD;
import static it.unimib.communimib.util.ErrorMapper.NOT_UNIVERSITY_EMAIL;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.apache.commons.validator.routines.EmailValidator;

import it.unimib.communimib.Callback;
import it.unimib.communimib.R;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IUserRepository;

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
        userRepository.signIn(email, password, result -> {
            signInResult.postValue(result);
        });
    }
    public LiveData<Result> getSignInResult(){
        return signInResult;
    }

    public void isEmailVerified(){
        userRepository.isEmailVerified(result -> {
            emailVerifiedResult.postValue(result);
        });
    }
    public LiveData<Result> getEmailVerifiedResult(){
        return emailVerifiedResult;
    }

    public String checkEmail(String email){
        if(email.isEmpty())
            return EMPTY_FIELD;
        if(!EmailValidator.getInstance().isValid(email))
            return INVALID_FIELD;
        if (!(email.substring(email.indexOf('@'))).equals("@campus.unimib.it") && !(email.substring(email.indexOf('@'))).equals("@unimib.it"))
            return NOT_UNIVERSITY_EMAIL;

        return "ok";
    }
}
