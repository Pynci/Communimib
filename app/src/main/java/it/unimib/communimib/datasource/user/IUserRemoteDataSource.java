package it.unimib.communimib.datasource.user;

import it.unimib.communimib.Callback;

public interface IUserRemoteDataSource {

    void storeUserParameters(String email, String password, String name, String surname, Callback callback);
    void getUserByEmail(String email);
    void updateNameAndSurname(String name, String surname);
}
