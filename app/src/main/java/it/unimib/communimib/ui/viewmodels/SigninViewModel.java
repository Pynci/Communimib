package it.unimib.communimib.ui.viewmodels;

import static it.unimib.communimib.util.ErrorMapper.EMPTY_FIELD;
import static it.unimib.communimib.util.ErrorMapper.INVALID_FIELD;
import static it.unimib.communimib.util.ErrorMapper.NOT_UNIVERSITY_EMAIL;

import androidx.lifecycle.ViewModel;

import org.apache.commons.validator.routines.EmailValidator;

public class SigninViewModel extends ViewModel {

    public SigninViewModel(){

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
