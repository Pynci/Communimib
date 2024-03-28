package it.unimib.communimib.repository;

import android.util.Log;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.user.IAuthDataSource;
import it.unimib.communimib.model.Result;

public class UserRepository implements IUserRepository{

    private final IAuthDataSource authDataSource;

    public UserRepository(IAuthDataSource authDataSource){
        this.authDataSource = authDataSource;
    }

    @Override
    public void signUp(String email, String password, String name, String surname, Callback callback) {
        authDataSource.signUp(email, password, result -> {
            if(result.isSuccessful()){
                Log.d(this.getClass().getSimpleName(), "funziona");
            }
            else{
                Log.d(this.getClass().getSimpleName(), "NON funziona");
                Log.d(this.getClass().getSimpleName(), ((Result.Error) result).getMessage());
            }
        });
    }

    @Override
    public void signIn(String email, String password, Callback callback) {

    }

    @Override
    public void signOut(Callback callback) {

    }

    @Override
    public void sendEmailVerification(String email, Callback callback) {

    }

    @Override
    public void updateUserNameAndSurname(String name, String surname, Callback callback) {

    }

    @Override
    public void getUserByEmail(String email, Callback callback) {

    }

    @Override
    public void resetPassword(String email, Callback callback) {

    }
}
