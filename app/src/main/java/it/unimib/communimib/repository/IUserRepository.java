package it.unimib.communimib.repository;

import android.net.Uri;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.User;

public interface IUserRepository {
    User getCurrentUser();

    void signUp(String email, String password, String name, String surname, Callback callback);
    void signIn(String email, String password, Callback callback);
    void signOut(Callback callback);

    void isSessionStillActive(Callback callback);

    void sendEmailVerification(Callback callback);

    void isEmailVerified(Callback callback);

    void startEmailPolling(Callback callback);

    void stopEmailPolling();

    void updateUserNameAndSurname(String name, String surname, Callback callback);

    void uploadPropic(Uri uri, Callback callback);

    void getUserByEmail(String email, Callback callback);
    // void updatePropic(Callback callback) e poi cosa?
    void resetPassword(String email, Callback callback);
}

