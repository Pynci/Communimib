package it.unimib.communimib.datasource.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.database.LocalDatabase;
import it.unimib.communimib.database.UserDAO;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.ErrorMapper;

public class UserLocalDataSource implements IUserLocalDataSource{

    private final UserDAO userDAO;
    private final SharedPreferences sharedPreferences;

    public UserLocalDataSource(UserDAO userDAO, SharedPreferences sharedPreferences) {
        this.userDAO = userDAO;
        this.sharedPreferences = sharedPreferences;
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
    public void saveUserFavoriteBuildings(List<String> userInterests, Callback callback) {
        try{
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(userInterests);

            editor.putString("string_array_list", json);
            editor.apply();

            callback.onComplete(new Result.Success());
        }
        catch (Exception e) {
            callback.onComplete(new Result.Error(ErrorMapper.LOCAL_SAVE_USER_FAVORITE_BUILDINGS_ERROR));
        }
    }

    @Override
    public void getUserFavoriteBuildings(Callback callback) {

        try {
            String json = sharedPreferences.getString("string_array_list", null);
            if (json != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                ArrayList<String> favoriteBuildings = gson.fromJson(json, type);
                callback.onComplete(new Result.UserFavoriteBuildings(favoriteBuildings));
            }
            else{
                callback.onComplete(new Result.Error(ErrorMapper.LOCAL_READ_USER_FAVORITE_BUILDINGS_ERROR));
            }
        }
        catch (Exception e) {
            callback.onComplete(new Result.Error(ErrorMapper.LOCAL_READ_USER_FAVORITE_BUILDINGS_ERROR));
        }
    }
}
