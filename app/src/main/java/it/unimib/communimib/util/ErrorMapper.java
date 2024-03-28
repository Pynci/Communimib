package it.unimib.communimib.util;

import java.util.HashMap;
import java.util.Map;

public class ErrorMapper {

    private Map<String, Integer> errorMap;
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

    // inserire tutti i codici errore qui sotto

}
