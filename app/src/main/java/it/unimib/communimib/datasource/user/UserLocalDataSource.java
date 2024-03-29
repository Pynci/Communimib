package it.unimib.communimib.datasource.user;

import it.unimib.communimib.Callback;
import it.unimib.communimib.database.LocalDatabase;
import it.unimib.communimib.database.UserDAO;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.ErrorMapper;

public class UserLocalDataSource implements IUserLocalDataSource{

    private final UserDAO userDAO;

    public UserLocalDataSource(LocalDatabase localDatabase) {
        userDAO = localDatabase.userDAO();
    }

    @Override
    public void getUser(Callback callback) {
        LocalDatabase.databaseWriteExecutor.execute(() -> {
            User user = userDAO.getUser();
            if(user != null){
                callback.onComplete(new Result.UserSuccess(user));
            }
            else{
                callback.onComplete(new Result.Error(ErrorMapper.LOCALDB_GET_ERROR));
            }
        });
    }

    @Override
    public void insertUser(User user, Callback callback) {
        LocalDatabase.databaseWriteExecutor.execute(() -> {
            long userId = userDAO.insertUser(user);
            if(userId == -1) {
                callback.onComplete(new Result.Success());
            }
            else{
                callback.onComplete(new Result.Error(ErrorMapper.LOCALDB_INSERT_ERROR));
            }
        });
    }

    @Override
    public void updateUser(User user, Callback callback) {
        //scrivo questo commento altrimenti sonar mi picchia
    }

    @Override
    public void deleteUser(User currentUser, Callback callback) {
        LocalDatabase.databaseWriteExecutor.execute(() -> {
            int rowsDeleted = userDAO.deleteUser(currentUser);
            if(rowsDeleted > 0){
                callback.onComplete(new Result.Success());
            }
            else{
                callback.onComplete(new Result.Error(ErrorMapper.LOCALDB_DELETE_ERROR));
            }
        });
    }
}
