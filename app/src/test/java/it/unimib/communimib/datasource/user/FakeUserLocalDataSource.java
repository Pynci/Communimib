package it.unimib.communimib.datasource.user;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.database.UserDAO;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.ErrorMapper;

public class FakeUserLocalDataSource implements IUserLocalDataSource {

    public List<String> usersFavoriteBuildings;

    public FakeUserLocalDataSource(){
        usersFavoriteBuildings = new ArrayList<>();
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
