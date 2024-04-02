package it.unimib.communimib.datasource.user;

import it.unimib.communimib.Callback;

public interface IAuthDataSource {

    boolean isSessionStillActive();

    String getCurrentUserEmail();

    void signUp(String email, String password, Callback callback);
    void sendEmailVerification(Callback callback);
    void signIn(String email, String password, Callback callback);

    void isEmailVerified(Callback callback);

    void signOut(Callback callback);

    void resetPassword(Callback callback);
}
