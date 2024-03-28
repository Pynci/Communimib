package it.unimib.communimib.datasource.user;

import com.google.firebase.database.FirebaseDatabase;

import it.unimib.communimib.Callback;

public class UserRemoteDataSource implements IUserRemoteDataSource{

    private final FirebaseDatabase firebaseDatabase;

    public UserRemoteDataSource() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void storeUserParameters(String email, String password, String name, String surname, Callback callback) {

    }

    @Override
    public void getUserByEmail(String email) {

    }

    @Override
    public void updateNameAndSurname(String name, String surname) {

    }
}
