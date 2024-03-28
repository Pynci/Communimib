package it.unimib.communimib.datasource.user;

import it.unimib.communimib.Callback;
import it.unimib.communimib.database.LocalDatabase;
import it.unimib.communimib.database.UserDAO;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;

public class UserLocalDataSource implements IUserLocalDataSource{

    private final UserDAO userDAO;

    public UserLocalDataSource(LocalDatabase localDatabase) {
        userDAO = localDatabase.userDAO();
    }

    @Override
    public void getUser(Callback callback) {

    }

    @Override
    public void insertUser(User user, Callback callback) {
        LocalDatabase.databaseWriteExecutor.execute(() -> {
            long userId = userDAO.insertUser(user);
            if(userId == -1) {
                callback.onComplete(new Result.Success());
            }
            else{
                callback.onComplete(new Result.Error("Errore nella memorizzazione dei dati sul database locale"));
            }
        });
    }

    @Override
    public void updateUser(User user, Callback callback) {

    }

    @Override
    public void deleteUser(Callback callback) {

    }
}
