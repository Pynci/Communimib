package it.unimib.communimib.datasource.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.ErrorMapper;

public class FakeUserRemoteDataSource implements IUserRemoteDataSource {

    public Map<String, User> users;

    public FakeUserRemoteDataSource(){
        users = new HashMap<>();
    }

    @Override
    public void storeUserParameters(String uid, String email, String name, String surname, Callback callback) {
        if(users.containsKey(uid)){
            users.replace(uid, new User(email, name, surname));
        }
        else{
            users.put(uid, new User(email, name, surname));
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
    public void updateNameAndSurname(String name, String surname) {
        //TODO: implementare questo metodo
    }
}
