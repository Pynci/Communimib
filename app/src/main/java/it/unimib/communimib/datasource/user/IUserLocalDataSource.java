package it.unimib.communimib.datasource.user;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.User;

public interface IUserLocalDataSource {

    void getUser(Callback callback);
    void insertUser(User user, Callback callback);
    void updateUser(User user, Callback callback);
    void deleteUser(Callback callback);
}
