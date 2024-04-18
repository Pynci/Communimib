package it.unimib.communimib.datasource.user;

import it.unimib.communimib.Callback;

public interface IUserRemoteDataSource {

    void storeUserParameters(String uid, String email, String name, String surname, Callback callback);
    void getUserByEmail(String email, Callback callback);
    void updateNameAndSurname(String uid, String name, String surname, Callback callback);
}
