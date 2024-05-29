package it.unimib.communimib.datasource.user;

import android.content.SharedPreferences;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.ErrorMapper;

public class UserLocalDataSource implements IUserLocalDataSource{

    private final SharedPreferences sharedPreferences;

    public UserLocalDataSource(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void saveUserFavoriteBuildings(List<String> favoriteBuildings, Callback callback) {
        try{
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(favoriteBuildings);

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
                callback.onComplete(new Result.UserFavoriteBuildingsSuccess(favoriteBuildings));
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
