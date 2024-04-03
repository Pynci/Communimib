package it.unimib.communimib.util;

import java.util.HashMap;
import java.util.Map;

import it.unimib.communimib.R;

public class ErrorMapper {

    private final Map<String, Integer> errorMap;
    private static ErrorMapper instance;

    private ErrorMapper(){
        errorMap = new HashMap<>();
        errorMap.put(EMPTY_FIELD, R.string.empty_field);
        errorMap.put(INVALID_FIELD, R.string.invalid_field);
        errorMap.put(NOT_UNIVERSITY_EMAIL, R.string.not_university_email);
        errorMap.put(TOO_SHORT_FIELD, R.string.too_short_field);
        errorMap.put(NUMBER_MISSING, R.string.number_missing);
        errorMap.put(CAPITAL_CASE_MISSING, R.string.capital_case_missing);
        errorMap.put(SPECIAL_CHAR_MISSING, R.string.special_char_missing);
        errorMap.put(NUMBER_NOT_ALLOWED, R.string.number_not_allowed);
        errorMap.put(SPECIAL_CHAR_NOT_ALLOWED, R.string.special_char_not_allowed);
        errorMap.put(NOT_EQUAL_PASSWORD, R.string.not_equal_password);
        errorMap.put(USER_NOT_AUTHENTICATED_ERROR, R.string.user_not_authenticated_error);
        errorMap.put(SIGNIN_ERROR, R.string.signin_error);

    }

    public static ErrorMapper getInstance() {
        if (instance == null) {
            instance = new ErrorMapper();
        }
        return instance;
    }

    public int getErrorMessage(String errorCode){
        if(errorMap.containsKey(errorCode)){
            return errorMap.get(errorCode);
        }
        return 0;
    }

    public static final String SIGNUP_ERROR = "signup_error";
    public static final String SIGNUP_FIREBASE_USER_ERROR = "signup_firebase_user_error";
    public static final String SIGNIN_ERROR = "signin_error";
    public static final String EMAIL_SENDING_ERROR = "email_sending_error";
    public static final String USER_NOT_AUTHENTICATED_ERROR = "user_not_authenticated_error";
    public static final String LOCALDB_INSERT_ERROR = "localdb_insert_error";
    public static final String LOCALDB_GET_ERROR = "localdb_get_error";
    public static final String REMOTEDB_GET_ERROR = "remotedb_get_error";
    public static final String REMOTEDB_INSERT_ERROR = "remotedb_insert_error";
    public static final String USER_NOT_FOUND_ERROR = "user_not_found_error";


    //Signup controls errors
    public static final String EMPTY_FIELD = "empty_field";
    public static final String INVALID_FIELD = "invalid_field";
    public static final String NOT_UNIVERSITY_EMAIL = "not_university_email";
    public static final String TOO_SHORT_FIELD = "too_short_field";
    public static final String NUMBER_MISSING = "number_missing";
    public static final String CAPITAL_CASE_MISSING = "capital_case_missing";
    public static final String SPECIAL_CHAR_MISSING = "special_char_missing";
    public static final String NUMBER_NOT_ALLOWED = "number_not_allowed";
    public static final String SPECIAL_CHAR_NOT_ALLOWED = "special_char_not_allowed";
    public static final String NOT_EQUAL_PASSWORD = "not_equal_password";

}
