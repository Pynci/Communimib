package it.unimib.communimib.datasource.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ErrorMapper;

public class UserRemoteDataSource implements IUserRemoteDataSource{

    private final DatabaseReference databaseReference;

    public UserRemoteDataSource() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance(Constants.DATABASE).getReference();
    }

    @Override
    public void storeUserParameters(String uid, String email, String name, String surname, Callback callback) {
        databaseReference
                .child(Constants.USERS_PATH)
                .child(uid)
                .setValue(new User(email, name, surname))
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        callback.onComplete(new Result.Success());
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_INSERT_ERROR));
                    }
                });
    }

    @Override
    public void getUserByEmail(String email, Callback callback) {
        databaseReference
                .child(Constants.USERS_PATH)
                .orderByChild("email")
                .equalTo(email)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot queryResult = task.getResult();
                        Iterator<DataSnapshot> iterator = queryResult.getChildren().iterator();
                        if(iterator.hasNext()){
                            callback.onComplete(new Result.UserSuccess(iterator.next().getValue(User.class)));
                        }
                        else{
                            callback.onComplete(new Result.Error(ErrorMapper.USER_NOT_FOUND_ERROR));
                        }
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
                    }
                });
    }

    @Override
    public void updateNameAndSurname(String name, String surname) {
        // scrivo questo commento altrimenti sonar mi picchia
    }
}
