package it.unimib.communimib.ui.auth.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.apache.commons.validator.routines.EmailValidator;

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

    public void signUp(String email, String password, String confirmPassword, String name, String surname) {

        if(Validation.checkEmail(email).equals("ok")
                && Validation.checkPassword(password).equals("ok")
                && Validation.checkConfirmPassword(confirmPassword, password).equals("ok")
                && Validation.checkField(name).equals("ok")
                && Validation.checkField(surname).equals("ok")) {

            iUserRepository.signUp(email, password, name, surname, signUpResult::postValue);
        }
        else{
            signUpResult.setValue(new Result.Error(ErrorMapper.NOT_ACCEPTED_PARAMETERS));
        }
    }

    public LiveData<Result> getSignUpResult() {
        return signUpResult;
    }

    public void cleanViewModel(){
        signUpResult = new MutableLiveData<>();
    }

}
