package it.unimib.communimib.repository;

import it.unimib.communimib.Callback;

public interface IUserRepository {
    void signUp(String email, String password, String name, String surname, Callback callback);
    void signIn(String email, String password, Callback callback);
    void signOut(Callback callback);

    void isSessionStillActive(Callback callback);

    void sendEmailVerification(Callback callback);

    void isEmailVerified(Callback callback);

    void updateUserNameAndSurname(String name, String surname, Callback callback);
    void getUserByEmail(String email, Callback callback);
    // void updatePropic(Callback callback) e poi cosa?
    void resetPassword(String email, Callback callback);
}

