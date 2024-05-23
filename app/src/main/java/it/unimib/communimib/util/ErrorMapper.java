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
        errorMap.put(SIGNUP_ERROR, R.string.signup_error);
        errorMap.put(SIGNUP_FIREBASE_USER_ERROR, R.string.signup_firebase_user_error);
        errorMap.put(EMAIL_SENDING_ERROR, R.string.email_sending_error);
        errorMap.put(LOCALDB_INSERT_ERROR, R.string.localdb_insert_error);
        errorMap.put(LOCALDB_GET_ERROR, R.string.localdb_get_error);
        errorMap.put(LOCALDB_UPDATE_ERROR, R.string.localdb_update_error);
        errorMap.put(REMOTEDB_GET_ERROR, R.string.remotedb_get_error);
        errorMap.put(REMOTEDB_INSERT_ERROR, R.string.remotedb_insert_error);
        errorMap.put(REMOTEDB_UPDATE_ERROR, R.string.remotedb_update_error);
        errorMap.put(USER_NOT_FOUND_ERROR, R.string.user_not_found_error);
        errorMap.put(NOT_ACCEPTED_PARAMETERS, R.string.not_accepted_parameters);
        errorMap.put(REPORT_CREATION_ERROR, R.string.report_creation_error);
        errorMap.put(REPORT_DELETING_ERROR, R.string.report_deleting_error);
        errorMap.put(REMOTE_SAVE_USER_FAVORITE_BUILDINGS_ERROR, R.string.remote_save_user_favorite_buildings_error);
        errorMap.put(LOCAL_SAVE_USER_FAVORITE_BUILDINGS_ERROR, R.string.local_save_user_favorite_buildings_error);
        errorMap.put(LOCAL_READ_USER_FAVORITE_BUILDINGS_ERROR, R.string.local_read_user_favorite_buildings_error);
        errorMap.put(REMOTE_READ_USER_FAVORITE_BUILDINGS_ERROR, R.string.remote_read_user_favorite_buildings_error);
        errorMap.put(POST_CREATION_ERROR, R.string.post_creation_error);
        errorMap.put(POST_DELETING_ERROR, R.string.post_deleting_error);
        errorMap.put(COMMENT_READ_ERROR, R.string.comment_read_error);
        errorMap.put(COMMENT_CREATION_ERROR, R.string.comment_creation_error);
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
        return R.string.system_error;
    }

    public static final String SIGNUP_ERROR = "signup_error";
    public static final String SIGNUP_FIREBASE_USER_ERROR = "signup_firebase_user_error";
    public static final String SIGNIN_ERROR = "signin_error";
    public static final String EMAIL_SENDING_ERROR = "email_sending_error";
    public static final String USER_NOT_AUTHENTICATED_ERROR = "user_not_authenticated_error";
    public static final String LOCALDB_INSERT_ERROR = "localdb_insert_error";
    public static final String LOCALDB_GET_ERROR = "localdb_get_error";
    public static final String LOCALDB_UPDATE_ERROR = "locadb_update_error";
    public static final String REMOTEDB_GET_ERROR = "remotedb_get_error";
    public static final String REMOTEDB_INSERT_ERROR = "remotedb_insert_error";
    public static final String REMOTEDB_UPDATE_ERROR = "remotedb_update_error";
    public static final String USER_NOT_FOUND_ERROR = "user_not_found_error";
    public static final String REMOTE_SAVE_USER_FAVORITE_BUILDINGS_ERROR = "remote_save_user_interest_error";
    public static final String LOCAL_SAVE_USER_FAVORITE_BUILDINGS_ERROR = "local_save_user_interest_error";

    public static final String REMOTE_READ_USER_FAVORITE_BUILDINGS_ERROR = "remote_read_user_favorite_buildings";
    public static final String LOCAL_READ_USER_FAVORITE_BUILDINGS_ERROR = "local_read_user_favorite_buildings";


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
    public static final String NOT_ACCEPTED_PARAMETERS = "not_accepted_parameters";

    // Errori gestione reports
    public static final String REPORT_CREATION_ERROR = "report_creation_error";
    public static final String REPORT_DELETING_ERROR = "report_deleting_error";

    //Errori gestione posts
    public static final String POST_CREATION_ERROR = "post_creation_error";
    public static final String POST_DELETING_ERROR = "post_deleting_error";

    //ERrori gestione commenti
    public static final String COMMENT_READ_ERROR = "comment_read_error";
    public static final String COMMENT_CREATION_ERROR = "comment_creation_error";
}
