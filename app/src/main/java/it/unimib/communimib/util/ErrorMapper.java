package it.unimib.communimib.util;

import java.util.HashMap;
import java.util.Map;

public class ErrorMapper {

    private final Map<String, Integer> errorMap;
    private static ErrorMapper instance;

    private ErrorMapper(){
        errorMap = new HashMap<>();
        // inserire mappatura tra codice errore e stringa di errore
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

}
