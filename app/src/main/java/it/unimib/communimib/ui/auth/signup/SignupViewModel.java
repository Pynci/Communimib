package it.unimib.communimib.ui.auth.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.security.interfaces.RSAKey;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.Token;
import it.unimib.communimib.model.User;
import it.unimib.communimib.repository.ITokenRepository;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.service.NotificationService;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.Validation;

public class SignupViewModel extends ViewModel {

    private MutableLiveData<Result> signUpResult;
    private MutableLiveData<Result> tokenResult;
    private final IUserRepository iUserRepository;
    private final ITokenRepository iTokenRepository;

    public SignupViewModel(IUserRepository iUserRepository, ITokenRepository iTokenRepository){
        signUpResult = new MutableLiveData<>();
        tokenResult = new MutableLiveData<>();
        this.iTokenRepository = iTokenRepository;
        this.iUserRepository = iUserRepository;
    }

    public void signUp(String email, String password, String name, String surname) {
        iUserRepository.signUp(email, password, name, surname, signUpResult::postValue);
    }

    public User getCurrentUser(){
        return iUserRepository.getCurrentUser();
    }

    public void addToken(){
        NotificationService.getTokenFromFirebaseMessaging(result -> {
            if(result.isSuccessful()){
                String tokenValue = ((Result.TokenValueSuccess) result).getTokenValue();
                User user = getCurrentUser();
                Token token = new Token(tokenValue, user.getUid());
                iTokenRepository.sendRegistrationToServer(token, tokenResult::postValue);
            }
        });
    }

    public LiveData<Result> getSignUpResult() {
        return signUpResult;
    }

    public LiveData<Result> getTokenResult() {
        return tokenResult;
    }

    public void cleanViewModel(){
        signUpResult = new MutableLiveData<>();
    }

}
