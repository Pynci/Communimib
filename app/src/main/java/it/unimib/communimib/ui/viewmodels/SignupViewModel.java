package it.unimib.communimib.ui.viewmodels;

import static it.unimib.communimib.util.ErrorMapper.CAPITAL_CASE_MISSING;
import static it.unimib.communimib.util.ErrorMapper.EMPTY_FIELD;
import static it.unimib.communimib.util.ErrorMapper.INVALID_FIELD;
import static it.unimib.communimib.util.ErrorMapper.NOT_EQUAL_PASSWORD;
import static it.unimib.communimib.util.ErrorMapper.NOT_UNIVERSITY_EMAIL;
import static it.unimib.communimib.util.ErrorMapper.NUMBER_MISSING;
import static it.unimib.communimib.util.ErrorMapper.NUMBER_NOT_ALLOWED;
import static it.unimib.communimib.util.ErrorMapper.SPECIAL_CHAR_MISSING;
import static it.unimib.communimib.util.ErrorMapper.SPECIAL_CHAR_NOT_ALLOWED;
import static it.unimib.communimib.util.ErrorMapper.TOO_SHORT_FIELD;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.apache.commons.validator.routines.EmailValidator;

import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IUserRepository;

public class SignupViewModel extends ViewModel {

    private MutableLiveData<Result> signUpResult;
    private final IUserRepository iUserRepository;

    public SignupViewModel(IUserRepository iUserRepository){
        signUpResult = new MutableLiveData<>();
        this.iUserRepository = iUserRepository;
    }

    public void signUn(String email, String password, String name, String surname) {
        iUserRepository.signUp(email, password, name, surname, result -> {

        });
    }

    public String checkEmail(String email){
        if (email.isEmpty())
            return EMPTY_FIELD;
        if (!EmailValidator.getInstance().isValid(email))
            return INVALID_FIELD;
        if (email.substring(email.indexOf("@")).equals("@campus.unimib.it") || email.substring(email.indexOf("@")).equals("@unimib.it"))
            return NOT_UNIVERSITY_EMAIL;

        return "ok";
    }
    
    public String checkPassword(String password){
        boolean number = false;
        boolean capitalCase = false;
        boolean specialChar = false;

        if(password.length() < 8)
            return TOO_SHORT_FIELD;

        for(int i = 0; i < password.length(); i++){
            if(password.charAt(i) >= '0' && password.charAt(i) <= '9')
                number = true;
            if(password.charAt(i) >= 'A' && password.charAt(i) <= 'Z')
                capitalCase = true;
            if(password.charAt(i) >= '!' && password.charAt(i) <= '/')
                specialChar = true;
        }

        if(!number)
            return NUMBER_MISSING;
        if(!capitalCase)
            return CAPITAL_CASE_MISSING;
        if(!specialChar)
            return SPECIAL_CHAR_MISSING;

        return "ok";
    }

    public String checkConfirmPassword(String confirmPassword, String password){
        if(!password.equals(confirmPassword))
            return NOT_EQUAL_PASSWORD;
        if(confirmPassword.isEmpty())
            return EMPTY_FIELD;

        return "ok";

    }

    public String checkField(String field){
        if(field.isEmpty())
            return EMPTY_FIELD;

        boolean number = false;
        boolean specialChar = false;

        for(int i = 0; i < field.length(); i++){
            if(field.charAt(i) >= '0' && field.charAt(i) <= '9')
                number = true;
            if(field.charAt(i) >= '!' && field.charAt(i) <= '/')
                specialChar = true;
        }

        if(number)
            return NUMBER_NOT_ALLOWED;
        if(specialChar)
            return SPECIAL_CHAR_NOT_ALLOWED;

        return "ok";
    }

}
