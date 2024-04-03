package it.unimib.communimib.ui.auth.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.apache.commons.validator.routines.EmailValidator;

import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.util.ErrorMapper;

public class SignupViewModel extends ViewModel {

    private MutableLiveData<Result> signUpResult;
    private final IUserRepository iUserRepository;

    public SignupViewModel(IUserRepository iUserRepository){
        signUpResult = new MutableLiveData<>();
        this.iUserRepository = iUserRepository;
    }

    public void signUp(String email, String password, String confirmPassword, String name, String surname) {

        if(checkEmail(email).equals("ok")
                && checkPassword(password).equals("ok")
                && checkConfirmPassword(confirmPassword, password).equals("ok")
                && checkField(name).equals("ok")
                && checkField(surname).equals("ok")) {

            iUserRepository.signUp(email, password, name, surname, signUpResult::postValue);
        }
        else{
            signUpResult.setValue(new Result.Error(ErrorMapper.NOT_ACCEPTED_PARAMETERS));
        }
    }

    public LiveData<Result> getSignUpResult() {
        return signUpResult;
    }

    public String checkEmail(String email){
        if (email.isEmpty())
            return ErrorMapper.EMPTY_FIELD;
        if (!EmailValidator.getInstance().isValid(email))
            return ErrorMapper.INVALID_FIELD;
        if (!email.substring(email.indexOf("@")).equals("@campus.unimib.it") && !email.substring(email.indexOf("@")).equals("@unimib.it"))
            return ErrorMapper.NOT_UNIVERSITY_EMAIL;

        return "ok";
    }
    
    public String checkPassword(String password){
        boolean number = false;
        boolean capitalCase = false;
        boolean specialChar = false;

        if(password.length() < 8)
            return ErrorMapper.TOO_SHORT_FIELD;

        for(int i = 0; i < password.length(); i++){
            if(password.charAt(i) >= '0' && password.charAt(i) <= '9')
                number = true;
            if(password.charAt(i) >= 'A' && password.charAt(i) <= 'Z')
                capitalCase = true;
            if(password.charAt(i) >= '!' && password.charAt(i) <= '/')
                specialChar = true;
        }

        if(!number)
            return ErrorMapper.NUMBER_MISSING;
        if(!capitalCase)
            return ErrorMapper.CAPITAL_CASE_MISSING;
        if(!specialChar)
            return ErrorMapper.SPECIAL_CHAR_MISSING;

        return "ok";
    }

    public String checkConfirmPassword(String confirmPassword, String password){
        if(!password.equals(confirmPassword))
            return ErrorMapper.NOT_EQUAL_PASSWORD;
        if(confirmPassword.isEmpty())
            return ErrorMapper.EMPTY_FIELD;

        return "ok";

    }

    public String checkField(String field){
        if(field.isEmpty())
            return ErrorMapper.EMPTY_FIELD;

        boolean number = false;
        boolean specialChar = false;

        for(int i = 0; i < field.length(); i++){
            if(field.charAt(i) >= '0' && field.charAt(i) <= '9')
                number = true;
            if(field.charAt(i) >= '!' && field.charAt(i) <= '/')
                specialChar = true;
        }

        if(number)
            return ErrorMapper.NUMBER_NOT_ALLOWED;
        if(specialChar)
            return ErrorMapper.SPECIAL_CHAR_NOT_ALLOWED;

        return "ok";
    }

    public void cleanViewModel(){
        signUpResult = new MutableLiveData<>();
    }

}
