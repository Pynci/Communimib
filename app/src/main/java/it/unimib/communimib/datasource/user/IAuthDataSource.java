package it.unimib.communimib.datasource.user;

import it.unimib.communimib.Callback;

public interface IAuthDataSource {

    void signUp(String email, String password, Callback callback);
    void sendEmailVerification(Callback callback);
    void signIn(String email, String password, Callback callback);

    void signOut(Callback callback);
}
