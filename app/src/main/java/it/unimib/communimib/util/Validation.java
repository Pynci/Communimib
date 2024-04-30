package it.unimib.communimib.util;

import org.apache.commons.validator.routines.EmailValidator;

public class Validation {

    private Validation(){

    }

    public static String checkEmail(String email){
        if (email.isEmpty())
            return ErrorMapper.EMPTY_FIELD;
        if (!EmailValidator.getInstance().isValid(email))
            return ErrorMapper.INVALID_FIELD;
        if (!email.substring(email.indexOf("@")).equals("@campus.unimib.it") && !email.substring(email.indexOf("@")).equals("@unimib.it"))
            return ErrorMapper.NOT_UNIVERSITY_EMAIL;

        return "ok";
    }

    public static String checkPassword(String password){
        boolean number = false;
        boolean capitalCase = false;
        boolean specialChar = false;

        if(password.isEmpty())
            return ErrorMapper.EMPTY_FIELD;

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

    public static String checkConfirmPassword(String confirmPassword, String password){

        if(confirmPassword.isEmpty())
            return ErrorMapper.EMPTY_FIELD;
        if(!password.equals(confirmPassword))
            return ErrorMapper.NOT_EQUAL_PASSWORD;

        return "ok";

    }

    public static String checkField(String field){
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

    public static String validateNewReport(String title, String description, String building, String category) {

        String error = ErrorMapper.NOT_ACCEPTED_PARAMETERS;

        if(!checkEmptyField(title).equals("ok") || !checkEmptyField(description).equals("ok")
                || !checkBuildingsSpinner(building).equals("ok") || !checkCategoriesSpinner(category).equals("ok")) {
            return error;
        }
        else{
            return "ok";
        }
    }

    public static String checkBuildingsSpinner (String building) {

        if(building.equals("Edificio"))
            return ErrorMapper.EMPTY_FIELD;
        else
            return "ok";
    }

    public static String checkCategoriesSpinner (String category) {

        if(category.equals("Categoria"))
            return ErrorMapper.EMPTY_FIELD;
        else
            return "ok";
    }

    public static String checkEmptyField (String field) {

        if(field.isEmpty()) {
            return ErrorMapper.EMPTY_FIELD;
        }
        else{
            return "ok";
        }
    }
}
