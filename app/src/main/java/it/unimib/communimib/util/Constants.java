package it.unimib.communimib.util;

public class Constants {
    private Constants(){}
    public static final String DATABASE = "https://communimib-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String USERS_PATH = "users";
    public static final String REPORTS_PATH = "reports";
    public static final String USERSREPORTS_PATH = "usersreports";
    public static final String USER_FAVORITE_BUILDINGS_PATH = "userfavoritebuildings";
    public static final String STORAGE_USERSPROPICS = "user_propics";

    public static final int FAVORITE_BUILDINGS_TIMEOUT = 1000 * 60 * 15; // 15 minuti espressi in millisecondi

    public static final String EMAIL_ERROR = "email_error";
    public static final String PASSWORD_ERROR = "password_error";
    public static final String CONFIRM_PASSWORD_ERROR = "confirm_password_error";
    public static final String NAME_ERROR = "name_error";
    public static final String SURNAME_ERROR = "surname_error";
}
