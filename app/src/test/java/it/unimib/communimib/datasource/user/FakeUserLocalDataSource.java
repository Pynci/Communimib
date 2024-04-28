package it.unimib.communimib.datasource.user;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.database.LocalDatabase;
import it.unimib.communimib.database.UserDAO;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.ErrorMapper;

public class FakeUserLocalDataSource implements IUserLocalDataSource {

    private final UserDAO userDAO;
    public List<String> usersFavoriteBuildings;

    public FakeUserLocalDataSource(UserDAO userDAO){
        this.userDAO = userDAO;
        usersFavoriteBuildings = new ArrayList<>();
    }

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
            if(userId != -1) {
                callback.onComplete(new Result.Success());
            }
            else{
                callback.onComplete(new Result.Error(ErrorMapper.LOCALDB_INSERT_ERROR));
            }
        });
    }

    @Override
    public void updateUser(User user, Callback callback) {
        LocalDatabase.databaseWriteExecutor.execute(() -> {
            int rows = userDAO.updateUser(user);
            if(rows == 1){
                callback.onComplete(new Result.Success());
            }
            else{
                callback.onComplete(new Result.Error(ErrorMapper.LOCALDB_UPDATE_ERROR));
            }
        });
    }

    @Override
    public void deleteUser(Callback callback) {
        LocalDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.clearUser();
            callback.onComplete(new Result.Success());
        });
    }

    @Override
    public void saveUserFavoriteBuildings(List<String> favoriteBuildings, Callback callback) {
        usersFavoriteBuildings = favoriteBuildings;
        callback.onComplete(new Result.Success());
    }

    @Override
    public void getUserFavoriteBuildings(Callback callback) {
        if(!usersFavoriteBuildings.isEmpty()){
            callback.onComplete(new Result.UserFavoriteBuildingsSuccess(usersFavoriteBuildings));
        }
        else{
            callback.onComplete(new Result.Error(ErrorMapper.LOCAL_READ_USER_FAVORITE_BUILDINGS_ERROR));
        }
    }
}
