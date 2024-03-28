package it.unimib.communimib.datasource.user;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.Constants;

public class UserRemoteDataSource implements IUserRemoteDataSource{

    private final DatabaseReference databaseReference;

    public UserRemoteDataSource() {
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
                        callback.onComplete(new Result.Error("errorazzo clamoroso incredibile (placeholder)"));
                    }
                });
    }

    @Override
    public void getUserByEmail(String email) {

    }

    @Override
    public void updateNameAndSurname(String name, String surname) {

    }
}
