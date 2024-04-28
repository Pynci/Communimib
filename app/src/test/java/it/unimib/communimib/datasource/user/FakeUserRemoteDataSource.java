package it.unimib.communimib.datasource.user;

import android.net.Uri;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.ErrorMapper;

public class FakeUserRemoteDataSource implements IUserRemoteDataSource {

    public Map<String, User> users;
    public Map<String, List<String>> usersFavoriteBuildings;

    public FakeUserRemoteDataSource(){
        users = new HashMap<>();
        usersFavoriteBuildings = new HashMap<>();
    }

    @Override
    public void storeUserParameters(String uid, String email, String name, String surname, boolean isUnimibEmployee, Callback callback) {
        if(users.containsKey(uid)){
            users.replace(uid, new User(uid, email, name, surname, isUnimibEmployee));
        }
        else{
            users.put(uid, new User(uid, email, name, surname, isUnimibEmployee));
        }
        callback.onComplete(new Result.Success());
    }

    @Override
    public void getUserByEmail(String email, Callback callback) {
        User foundUser = null;
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                foundUser = user;
                break;
            }
        }
        if (foundUser != null) {
            callback.onComplete(new Result.UserSuccess(foundUser));
        } else {
            callback.onComplete(new Result.Error(ErrorMapper.USER_NOT_FOUND_ERROR));
        }
    }

    @Override
    public void updateNameAndSurname(String uid, String name, String surname, Callback callback) {
        if(users.containsKey(uid)){
            users.get(uid).setName(name);
            callback.onComplete(new Result.Success());
        }
        else{
            callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_UPDATE_ERROR));
        }
    }

    @Override
    public void uploadPropic(String uid, Uri uri, Callback callback) {
        if(users.containsKey(uid)){
            users.get(uid).setPropic(uri.toString());
            callback.onComplete(new Result.UriSuccess(uri.toString()));
        }
        else{
            callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_UPDATE_ERROR));
        }
    }

    @Override
    public void storeUserFavoriteBuildings(List<String> buildings, String userId, Callback callback) {
        if(usersFavoriteBuildings.containsKey(userId)){
            usersFavoriteBuildings.replace(userId, buildings);
        }
        else{
            usersFavoriteBuildings.put(userId, buildings);
        }
        callback.onComplete(new Result.Success());
    }

    @Override
    public void getUserFavoriteBuildings(String userId, Callback callback) {
        if(usersFavoriteBuildings.containsKey(userId)){
            callback.onComplete(new Result.UserFavoriteBuildings(usersFavoriteBuildings.get(userId)));
        }
        else{
            callback.onComplete(new Result.Error(ErrorMapper.REMOTE_READ_USER_FAVORITE_BUILDINGS_ERROR));
        }
    }
}
